package net.jonuuh.nickfinder.event;

import net.jonuuh.nickfinder.config.Config;
import net.jonuuh.nickfinder.config.ConfigHandler;
import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.jonuuh.nickfinder.loggers.FileLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Finder
{
    private final Minecraft mc;
    private final Config config;
    private final KeyBinding toggleKey;
    private final Set<String> limboStrings = new HashSet<>(Arrays.asList("You are AFK. Move around to return from AFK.", "You were spawned in Limbo."));
    private final Pattern nickPattern = Pattern.compile("\u00a7l\\w{3,16}\u00a7r"); // bold format code, 3-16 chars: [a-zA-Z0-9_], reset format code

    private FileLogger nicksLog;
    private FileLogger nicksLatestLog;
    private boolean waitingForLoc;
    private int nicks;
    private int ticks;
    private int curLobby;

    boolean running;

    public Finder(KeyBinding toggleKey)
    {
        this.mc = Minecraft.getMinecraft();
        this.config = ConfigHandler.getInstance().getConfig();
        this.toggleKey = toggleKey;
        this.running = false;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if (toggleKey.isPressed())
        {
            if (!running)
            {
                mc.thePlayer.sendChatMessage("/locraw");
                waitingForLoc = true;
            }
            else
            {
                stop();
            }
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
        String msg = event.message.getUnformattedText();

        if (waitingForLoc && msg.matches("^\\{.+}$") && msg.contains("server") && msg.contains("gametype") && msg.contains("lobbyname"))
        {
            waitingForLoc = false;
            start(msg);
        }

        if (running && limboStrings.contains(msg))
        {
            stop();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (!running || event.phase == TickEvent.Phase.END || mc.isGamePaused())
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
        if (running && event.gui instanceof GuiScreenBook && mc.thePlayer.getHeldItem().getItem() instanceof ItemEditableBook)
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
        if (running)
        {
            stop();
        }
    }

    private void start(String loc)
    {
        running = true;
        nicksLog = new FileLogger("nicks", true);
        nicksLatestLog = new FileLogger("nicks-latest", false);
        curLobby = parseStartingLobby(loc);
        ChatLogger.addLog("NickFinder started", EnumChatFormatting.GOLD);
    }

    private void stop()
    {
        running = false;
        nicksLog.close();
        nicksLatestLog.close();
        ChatLogger.addLog("NickFinder stopped", EnumChatFormatting.GOLD);
    }

    private int parseStartingLobby(String loc)
    {
        int lobby = 0;

        try
        {
            String match = "lobbyname";
            String lobbyStr = loc.substring(loc.indexOf(match) + match.length()).replaceAll("\\D+", "");
            lobby = Integer.parseInt(lobbyStr);
        }
        catch (IndexOutOfBoundsException | NumberFormatException e) // both RuntimeException
        {
            e.printStackTrace();
            ChatLogger.addLog("Unable to parse starting lobby", EnumChatFormatting.DARK_RED);
        }

        return lobby;
    }
}
