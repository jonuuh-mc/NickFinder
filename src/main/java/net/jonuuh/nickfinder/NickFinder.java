package net.jonuuh.nickfinder;

import net.jonuuh.nickfinder.utils.HandleEvents;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;

@Mod(modid = NickFinder.MODID, version = NickFinder.VERSION)
public class NickFinder
{
    public static final String MODID = "nickfinder";
    public static final String VERSION = "1.0";

    private final KeyBinding debugKey;
    private final KeyBinding startStopKey;

    public NickFinder()
    {
        this.debugKey = new KeyBinding("Debug", Keyboard.KEY_MINUS, "NickFinder");
        this.startStopKey = new KeyBinding("Start/Stop", Keyboard.KEY_EQUALS, "NickFinder");
    }

//    @EventHandler
//    public void preInit(FMLPreInitializationEvent event)
//    {
//
//    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        new File("nickfinder").mkdir();

        ClientRegistry.registerKeyBinding(debugKey);
        ClientRegistry.registerKeyBinding(startStopKey);

        FMLCommonHandler.instance().bus().register(new HandleEvents(debugKey, startStopKey));
    }
}
