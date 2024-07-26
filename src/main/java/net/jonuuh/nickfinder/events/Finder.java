package net.jonuuh.nickfinder.events;

import net.jonuuh.nickfinder.config.Config;
import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.jonuuh.nickfinder.loggers.FileLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Finder
{
    private final Minecraft mc;
    private final Config config;
    private final FileLogger nicksLog;
    private final FileLogger nicksLatestLog;
    private final Pattern nickPattern;

    private int nicks;
    private int ticks;
    private int curLobby;

    boolean running;

    Finder(Minecraft mc, Config config, FileLogger nicksLog, FileLogger nicksLatestLog, Pattern nickPattern, int startingLobby)
    {
        this.mc = mc;
        this.config = config;
        this.nicksLog = nicksLog;
        this.nicksLatestLog = nicksLatestLog;
        this.nickPattern = nickPattern;
        curLobby = startingLobby;
        running = true;
    }

    void stop()
    {
        running = false;
        nicksLog.close();
        nicksLatestLog.close();
        MinecraftForge.EVENT_BUS.unregister(this);
        ChatLogger.addLog("NickFinder stopped", EnumChatFormatting.GOLD);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END || mc.isGamePaused())
        {
            return;
        }

        ticks++;

        if (ticks == 20 * config.getAFKDelaySecs())
        {
            int lobby = ThreadLocalRandom.current().nextInt(config.getLobbyMin(), config.getLobbyMax());
            int newLobby = (lobby >= curLobby) ? ++lobby : lobby; // https://stackoverflow.com/a/34184614
            ticks = 0;

            ChatLogger.addLog("Swapping lobby from " + curLobby + " to " + newLobby + "...", EnumChatFormatting.GRAY);
            curLobby = newLobby;
            mc.thePlayer.sendChatMessage("/swaplobby " + lobby);
        }
        else if (ticks % 20 * config.getNickDelaySecs() == 0)
        {
            ChatLogger.addLog("Requesting new nick...", EnumChatFormatting.GRAY);
            mc.thePlayer.sendChatMessage("/nick help setrandom");
        }
    }

    @SubscribeEvent
    public void onGuiOpened(GuiOpenEvent event)
    {
        if (event.gui instanceof GuiScreenBook && mc.thePlayer.getHeldItem().getItem() instanceof ItemEditableBook)
        {
            event.setCanceled(true);
        }
        else
        {
            return;
        }

        // Full text of the book item (json)
        String bookStr = mc.thePlayer.getHeldItem().getTagCompound().getTagList("pages", 8).toString();

        // Double check that a nick actually exists somewhere in the bookStr
        if (nickPattern.matcher(bookStr).find())
        {
            String nick = bookStr.substring(bookStr.indexOf("\u00a7l") + "\u00a7l".length(), bookStr.indexOf("\u00a7r"));

            ChatLogger.addLog(++nicks + ": " + nick/*+ " ticks: " + ticks*/);
            nicksLog.write(nick);
            nicksLatestLog.write(nick);

            // Early return if nick matches a filter pattern
            for (Pattern pattern : config.getFilterPatterns())
            {
                if (pattern.matcher(nick).find())
                {
                    return;
                }
            }

            // Default to claiming the nick if no targets (e.g if only using filters)
            if (config.getTargetPatterns().size() == 0)
            {
                mc.thePlayer.sendChatMessage("/nick actuallyset " + nick + " respawn");
                stop();
            }

            // Claim the nick and stop the bot if the nick matches a target pattern
            for (Pattern pattern : config.getTargetPatterns())
            {
                if (pattern.matcher(nick).find())
                {
                    mc.thePlayer.sendChatMessage("/nick actuallyset " + nick + " respawn");
                    stop();
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientDisconnectionFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        stop();
    }
}
