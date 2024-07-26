package net.jonuuh.nickfinder;

import net.jonuuh.nickfinder.config.CommandNickFinder;
import net.jonuuh.nickfinder.config.Config;
import net.jonuuh.nickfinder.events.FinderController;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;

@Mod(modid = "nickfinder", version = "1.2.3")
public class NickFinder
{
    private final Config config;
    private final KeyBinding toggleKey;

    public NickFinder()
    {
        this.config = new Config();
        this.toggleKey = new KeyBinding("Toggle NickFinder", Keyboard.KEY_EQUALS, "NickFinder");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        new File("nickfinder").mkdir();

        ClientRegistry.registerKeyBinding(toggleKey);
        ClientCommandHandler.instance.registerCommand(new CommandNickFinder(config));
        MinecraftForge.EVENT_BUS.register(new FinderController(config, toggleKey));
    }
}
