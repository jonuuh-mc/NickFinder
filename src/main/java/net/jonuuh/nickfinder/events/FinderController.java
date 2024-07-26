package net.jonuuh.nickfinder.events;

import net.jonuuh.nickfinder.config.Config;
import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.jonuuh.nickfinder.loggers.FileLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class FinderController
{
    private final Minecraft mc;
    private final Config config;
    private final KeyBinding toggleKey;
    private final Set<String> limboStrings = new HashSet<>(Arrays.asList("You are AFK. Move around to return from AFK.", "You were spawned in Limbo."));
    private final Pattern nickPattern = Pattern.compile("\u00a7l\\w{3,16}\u00a7r"); // bold format code, 3-16 chars: [a-zA-Z0-9_], reset format code

    private Finder finder;
    private boolean waitingForLocraw;

    public FinderController(Config config, KeyBinding toggleKey)
    {
        this.mc = Minecraft.getMinecraft();
        this.config = config;
        this.toggleKey = toggleKey;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if (toggleKey.isPressed())
        {
//            if (finder == null)
//            {
//                mc.thePlayer.sendChatMessage("/locraw");
//                waitingForLocraw = true;
//                return;
//            }

            if (finder == null || !finder.running)
            {
                mc.thePlayer.sendChatMessage("/locraw");
                waitingForLocraw = true;
            }
            else
            {
                finder.stop();
            }
        }
    }

    // TODO: will still run if finder has been created prev (never != null after 1st time)
    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
        String msg = event.message.getUnformattedText();

        if (waitingForLocraw && msg.matches("^\\{.+}$") && msg.contains("server") && msg.contains("gametype") && msg.contains("lobbyname"))
        {
            waitingForLocraw = false;
            FileLogger nicksLog = new FileLogger("nicks", true);
            FileLogger nicksLatestLog = new FileLogger("nicks-latest", false);

            finder = new Finder(mc, config, nicksLog, nicksLatestLog, nickPattern, parseStartingLobby(msg));
            MinecraftForge.EVENT_BUS.register(finder);
            ChatLogger.addLog("NickFinder started", EnumChatFormatting.GOLD);
        }

        if (finder != null && finder.running && limboStrings.contains(msg))
        {
            finder.stop();
        }
    }

    private int parseStartingLobby(String locraw)
    {
        int lobby = 0;

        try
        {
            String match = "lobbyname";
            String lobbyStr = locraw.substring(locraw.indexOf(match) + match.length()).replaceAll("\\D+", "");
            lobby = Integer.parseInt(lobbyStr);
        }
        catch (IndexOutOfBoundsException | NumberFormatException e) // both RuntimeException
        {
            e.printStackTrace();
            ChatLogger.addLog("Unable to parse starting lobby", EnumChatFormatting.RED);
        }

        ChatLogger.addLog("Starting lobby: " + lobby);
        return lobby;
    }
}
