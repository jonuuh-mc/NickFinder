package net.jonuuh.nickfinder.utils;

import net.jonuuh.nickfinder.NickFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class HandleEvents
{
    @SubscribeEvent
    public void onKeyPressed(KeyInputEvent event)
    {
        if (NickFinder.debugKey.isPressed())
        {
            NickFinder.chatLogger.addLog("debugKey pressed");
        }

        if (NickFinder.startStopKey.isPressed())
        {
            NickFinder.toggle();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (NickFinder.running)
        {
            NickFinder.ticks++;
            if (NickFinder.ticks % (40 * NickFinder.nickDelaySecs) == 0 && !AntiAFK.running)
            {
                if (!Minecraft.getMinecraft().isGamePaused())
                {
                    NickFinder.chatLogger.addLog("requesting new nick...", EnumChatFormatting.GRAY, false);
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/nick help setrandom");
                }
            }

            if (NickFinder.ticks % (40 * NickFinder.antiAFKIntervalSecs) == 0 && !AntiAFK.running)
            {
                NickFinder.antiAFK.startRandomMove(NickFinder.ticks);
            }

            if (NickFinder.ticks == NickFinder.antiAFK.tickStart + (40 * NickFinder.antiAFKDurationSecs) && AntiAFK.running)
            {
                NickFinder.antiAFK.endRandomMove();
            }
        }
    }

    @SubscribeEvent
    public void onGUIOpened(GuiOpenEvent event)
    {
        if (event.gui instanceof GuiScreenBook && NickFinder.running)
        {
//            NickFinder.chatLogger.addLog("book gui open event", EnumChatFormatting.GREEN, false);
            ItemStack bookItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
            if (bookItem.getItem() instanceof ItemEditableBook && bookItem.hasTagCompound())
            {
//                NickFinder.chatLogger.addLog("written book detected" , EnumChatFormatting.GREEN, false);
                String bookPagesString = bookItem.getTagCompound().getTagList("pages", 8).toString();
                if (bookPagesString.contains("generated a random username"))
                {
//                    NickFinder.chatLogger.addLog("nick detected" , EnumChatFormatting.GREEN, false);
                    String nick = bookPagesString.substring(bookPagesString.indexOf("actuallyset") + "actuallyset".length() + 1, bookPagesString.indexOf("respawn") - 1);
                    NickFinder.chatLogger.addLog(++NickFinder.nicks + ": " + nick);
                    NickFinder.fileLoggerNicks.addLogLn(nick);
                    NickFinder.fileLoggerNicksLatest.addLogLn(nick);

                    if (nick.matches("^[A-Z][a-z]+$")){
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/nick actuallyset " + nick + " respawn");
                        NickFinder.toggle();
                    }
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onClientChat(ClientChatReceivedEvent event)
    {
        if (event.message.getUnformattedText().equals("You are AFK. Move around to return from AFK."))
        {
            NickFinder.running = false;
            attemptFileLoggerClose(NickFinder.fileLoggerNicks);
            attemptFileLoggerClose(NickFinder.fileLoggerNicksLatest);
            NickFinder.chatLogger.addLog("AFK detected, Loggers closed, !running");
        }
    }

//    @SubscribeEvent
//    public void onServerLogin(FMLNetworkEvent.ClientConnectedToServerEvent event)
//    {
//        NickFinder.chatLogger.addLog("player logged in!");
//    }

    @SubscribeEvent
    public void onServerLogout(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
//        NickFinder.chatLogger.addLog("player logged out!");
        NickFinder.running = false;
        attemptFileLoggerClose(NickFinder.fileLoggerNicks);
        attemptFileLoggerClose(NickFinder.fileLoggerNicksLatest);
    }

    private void attemptFileLoggerClose(FileLogger logger){
        if (logger != null)
        {
            logger.close();
        }
    }
}
