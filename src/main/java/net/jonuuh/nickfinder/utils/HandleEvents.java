package net.jonuuh.nickfinder.utils;

import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.jonuuh.nickfinder.loggers.FileLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Handle various minecraft events.
 */
public class HandleEvents
{
    private static final double nickDelaySecs = 1;
    private static final double antiAFKIntervalSecs = (60 * 1.5);
    private static final double antiAFKDurationSecs = 0.25;

    private final Set<String> limboStrings = new HashSet<>(Arrays.asList("You are AFK. Move around to return from AFK.", "A kick occurred in your connection, so you have been routed to limbo!"));
    private final MiscUtils miscUtils;
    private final ChatLogger chatLogger;
    private final KeyBinding debugKey;
    private final KeyBinding toggleKey;

    private FileLogger fileLoggerNicks = null;
    private FileLogger fileLoggerNicksLatest = null;
    private AntiAFK antiAFK = null;
    private boolean running = false;
    private int ticks = 0;
    private int nicks = 0;

    /**
     * Instantiates a new HandleEvents.
     *
     * @param debugKey  the debug key
     * @param toggleKey the toggle key
     */
    public HandleEvents(KeyBinding debugKey, KeyBinding toggleKey)
    {
        this.miscUtils = new MiscUtils();
        this.chatLogger = new ChatLogger(EnumChatFormatting.GOLD);
        this.debugKey = debugKey;
        this.toggleKey = toggleKey;
    }

    /**
     * Key input event handler.
     *
     * @param event the event
     */
    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event)
    {
        if (debugKey.isPressed())
        {
            chatLogger.addLog("debugKey pressed");
        }

        if (toggleKey.isPressed())
        {
            toggle();
        }
    }

    /**
     * Client tick event handler.<br>
     * - Used to perform certain actions periodically (requesting a new nick or starting/stopping an antiAFK random move).
     *
     * @param event the event
     */
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (running && !Minecraft.getMinecraft().isGamePaused())
        {
            ticks++;
            if (ticks % (40 * nickDelaySecs) == 0 && !AntiAFK.running)
            {
                chatLogger.addLog("requesting new nick...", EnumChatFormatting.GRAY, false);
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/nick help setrandom");
            }

            if (ticks % (40 * antiAFKIntervalSecs) == 0 && !AntiAFK.running)
            {
                if (miscUtils.getNearbyPlayers(10).size() == 0)
                {
                    antiAFK.startRandomMove(ticks);
                }
                else
                {
                    chatLogger.addLog("player detected nearby", EnumChatFormatting.DARK_RED, true);
                    toggle();
                }
            }

            if (ticks == antiAFK.tickStart + (40 * antiAFKDurationSecs) && AntiAFK.running)
            {
                antiAFK.endRandomMove();
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
        if (running && event.gui instanceof GuiScreenBook)
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
        if (running && limboStrings.contains(event.message.getUnformattedText()))
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
        if (running)
        {
            toggle();
        }
    }

    /**
     * Toggle the mod on or off.
     */
    private void toggle()
    {
        chatLogger.addLog("nickfinder toggled", EnumChatFormatting.GOLD, true);
        if (!running)
        {
            fileLoggerNicks = new FileLogger("nicks", true);
            fileLoggerNicksLatest = new FileLogger("nicks-latest", false);
            antiAFK = new AntiAFK();
            chatLogger.addLog("new Loggers + AntiAFK, ticker + nicks reset, running", EnumChatFormatting.YELLOW, false);
        }
        else
        {
            fileLoggerNicks.close();
            fileLoggerNicksLatest.close();
            chatLogger.addLog("Loggers closed, ticker + nicks reset, !running", EnumChatFormatting.YELLOW, false);
        }
        ticks = 0;
        nicks = 0;
        running = !running;
    }
}
