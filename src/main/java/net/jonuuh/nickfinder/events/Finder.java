package net.jonuuh.nickfinder.events;

import net.jonuuh.nickfinder.config.Config;
import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.jonuuh.nickfinder.loggers.FileLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Finder
{
    private final Minecraft mc;
    private final Config config;
    private final ChatLogger chatLogger;

    private FileLogger fileLoggerNicks;
    private FileLogger fileLoggerNicksLatest;
    private boolean running;
    private int nicks;
    private int ticks;

    Finder(Minecraft mc, Config config, ChatLogger chatLogger)
    {
        this.mc = mc;
        this.config = config;
        this.chatLogger = chatLogger;
        this.running = false;
        this.nicks = 0;
        this.ticks = 0;
    }

    /**
     * Toggle the finder on or off.
     */
    public void toggle()
    {
        chatLogger.addLog("nickfinder toggled", EnumChatFormatting.GOLD, true);
        if (!running)
        {
            fileLoggerNicks = new FileLogger("nicks", true);
            fileLoggerNicksLatest = new FileLogger("nicks-latest", false);
            ticks = 0;
            nicks = 0;
            MinecraftForge.EVENT_BUS.register(this);
            chatLogger.addLog("start: new loggers, ticker + nicks reset, registered", EnumChatFormatting.YELLOW, false);
        }
        else
        {
            fileLoggerNicks.close();
            fileLoggerNicksLatest.close();
            MinecraftForge.EVENT_BUS.unregister(this);
            chatLogger.addLog("stop: loggers closed, unregistered", EnumChatFormatting.YELLOW, false);
        }
        running = !running;
    }

    /**
     * Client tick event handler.<br>
     * - Used to perform certain actions periodically (requesting a new nick or preventing AFK).
     *
     * @param event the event
     */
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START && !mc.isGamePaused())
        {
            ticks++;

            if (ticks % (20 * config.getAntiAFKDelaySecs()) == 0)
            {
                chatLogger.addLog("swapping lobby...", EnumChatFormatting.GRAY, false);
                int randomLobby = ThreadLocalRandom.current().nextInt(config.getLobbyMin(), config.getLobbyMax() + 1);
                Minecraft.getMinecraft().thePlayer.sendChatMessage("swaplobby " + randomLobby);
            }

            if (ticks % (20 * config.getReqNickDelaySecs()) == 0)
            {
                chatLogger.addLog("requesting new nick...", EnumChatFormatting.GRAY, false);
                Minecraft.getMinecraft().thePlayer.sendChatMessage("nick help setrandom");
            }
        }
    }

    /**
     * GUI opened event handler.<br>
     * - The "heart" of the mod, gets a nick from a written book and uses it if its "desirable".
     *
     * @param event the event
     */
    @SubscribeEvent
    public void onGUIOpened(GuiOpenEvent event)
    {
        if (event.gui instanceof GuiScreenBook)
        {
            ItemStack bookItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
            if (bookItem.getItem() instanceof ItemEditableBook)
            {
                String bookPagesString = bookItem.getTagCompound().getTagList("pages", 8).toString();
                if (bookPagesString.contains("generated a random username"))
                {
                    String nick = bookPagesString.substring(bookPagesString.indexOf("actuallyset") + "actuallyset".length() + 1, bookPagesString.indexOf("respawn") - 1);
                    chatLogger.addLog(++nicks + ": " + nick);
                    fileLoggerNicks.addLogLn(nick);
                    fileLoggerNicksLatest.addLogLn(nick);

                    if (nick.matches("^[A-Z][a-z]+$"))
                    {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/nick actuallyset " + nick + " respawn");
                        toggle();
                    }
                }
            }
            event.setCanceled(true);
        }
    }

    /**
     * Client chat received event handler.<br>
     * - Toggles off the mod if the player was sent to limbo while it was running.
     *
     * @param event the event
     */
    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
        if (config.getLimboStrings().contains(event.message.getUnformattedText()))
        {
            toggle();
        }
    }

    /**
     * Client disconnection from server event handler.<br>
     * - Toggles off the mod if the player logged out while it was running.
     *
     * @param event the event
     */
    @SubscribeEvent
    public void onClientDisconnectionFromServer(ClientDisconnectionFromServerEvent event)
    {
        toggle();
    }
}
