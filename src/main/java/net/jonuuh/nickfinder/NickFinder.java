package net.jonuuh.nickfinder;

import net.jonuuh.nickfinder.events.FinderController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;

/**
 * NickFinder main class / entry point.
 */
@Mod(modid = NickFinder.MODID, version = NickFinder.VERSION)
public class NickFinder
{
    public static final String MODID = "nickfinder";
    public static final String VERSION = "1.1.0";

    private final Minecraft mc;
    private final KeyBinding debugKey;
    private final KeyBinding toggleKey;

    public NickFinder()
    {
        this.mc = Minecraft.getMinecraft();
        this.debugKey = new KeyBinding("Debug", Keyboard.KEY_MINUS, "NickFinder");
        this.toggleKey = new KeyBinding("Toggle NickFinder", Keyboard.KEY_EQUALS, "NickFinder");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        new File("nickfinder").mkdir();

        ClientRegistry.registerKeyBinding(debugKey);
        ClientRegistry.registerKeyBinding(toggleKey);

        MinecraftForge.EVENT_BUS.register(new FinderController(mc, debugKey, toggleKey));
    }
}
