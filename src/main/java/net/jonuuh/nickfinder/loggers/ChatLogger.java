package net.jonuuh.nickfinder.loggers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;

public class ChatLogger
{
    private static final IChatComponent header = new ChatComponentText("\u00a76\u00a7l[\u00a78\u00a7lNickFinder\u00a76\u00a7l] ");

    public static void addLogs(String... logs)
    {
        Arrays.stream(logs).forEach(ChatLogger::addLog);
    }

    public static void addLog(String log)
    {
        addLog(log, EnumChatFormatting.WHITE);
    }

    public static void addLog(String log, EnumChatFormatting color)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (player != null)
        {
            IChatComponent chatComponent = new ChatComponentText(log).setChatStyle(new ChatStyle().setColor(color));
            player.addChatMessage(header.createCopy().appendSibling(chatComponent));
        }
    }
}
