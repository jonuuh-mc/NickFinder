package net.jonuuh.nickfinder;

import net.jonuuh.nickfinder.utils.AntiAFK;
import net.jonuuh.nickfinder.utils.ChatLogger;
import net.jonuuh.nickfinder.utils.FileLogger;
import net.jonuuh.nickfinder.utils.HandleEvents;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;

@Mod(modid = NickFinder.MODID, version = NickFinder.VERSION)
public class NickFinder
{
    public static final String MODID = "nickfinder";
    public static final String VERSION = "1.0";

    public static final KeyBinding debugKey = new KeyBinding("Debug", Keyboard.KEY_MINUS, "NickFinder");
    public static final KeyBinding startStopKey = new KeyBinding("Start/Stop", Keyboard.KEY_EQUALS, "NickFinder");
    public static final ChatLogger chatLogger = new ChatLogger(EnumChatFormatting.GOLD);
    public static final double nickDelaySecs = 1;
    public static final double antiAFKIntervalSecs = (60 * 1.5);
    public static final double antiAFKDurationSecs = 0.25;

    public static FileLogger fileLoggerNicks = null;
    public static FileLogger fileLoggerNicksLatest = null;
    public static AntiAFK antiAFK = null;
    public static boolean running = false;
    public static int ticks = 0;
    public static int nicks = 0;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        new File ("nickfinder").mkdir();

        ClientRegistry.registerKeyBinding(debugKey);
        ClientRegistry.registerKeyBinding(startStopKey);

        FMLCommonHandler.instance().bus().register(new HandleEvents());
    }

    public static void toggle()
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

