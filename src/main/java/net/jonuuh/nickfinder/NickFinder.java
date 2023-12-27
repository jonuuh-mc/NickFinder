package net.jonuuh.nickfinder;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = NickFinder.MODID, version = NickFinder.VERSION)
public class NickFinder
{
    public static final String MODID = "nickfinder";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("DIRT BLOCK: " + Blocks.dirt.getUnlocalizedName());
    }
}
