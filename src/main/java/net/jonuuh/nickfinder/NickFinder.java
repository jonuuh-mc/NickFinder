package net.jonuuh.nickfinder;

import net.jonuuh.nickfinder.config.CommandNickFinder;
import net.jonuuh.nickfinder.config.ConfigHandler;
import net.jonuuh.nickfinder.event.Finder;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;

@Mod(modid = "nickfinder", version = "1.2.4")
public class NickFinder
{
    @EventHandler
    public void FMLPreInit(FMLPreInitializationEvent event)
    {
        File file = new File("nickfinder");

        if (!file.exists())
        {
            file.mkdir();
        }

        ConfigHandler.createInstance(file);
    }

    @EventHandler
    public void FMLInit(FMLInitializationEvent event)
    {
        KeyBinding toggleKey = new KeyBinding("Toggle NickFinder", Keyboard.KEY_EQUALS, "~NickFinder");

        ClientRegistry.registerKeyBinding(toggleKey);
        ClientCommandHandler.instance.registerCommand(new CommandNickFinder());
        MinecraftForge.EVENT_BUS.register(new Finder(toggleKey));
    }
}
