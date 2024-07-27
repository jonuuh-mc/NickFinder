package net.jonuuh.nickfinder.loggers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.List;

public class ChatLogger
{
    private static final IChatComponent header = new ChatComponentText("\u00a76[\u00a7fNickFinder\u00a76] ");

    public static void addLog(String log)
    {
        addLog(log, EnumChatFormatting.WHITE);
    }

    public static void addLogs(List<String> logs)
    {
        logs.forEach(ChatLogger::addLog);
    }

    public static void addBoolLog(String trueLog, String falseLog, boolean state)
    {
        addLog((state ? trueLog : falseLog), (state ? EnumChatFormatting.GREEN : EnumChatFormatting.RED));
    }

    public static void addLog(String log, EnumChatFormatting color)
    {
        addLog(new ChatComponentText(log), new ChatStyle().setColor(color), true);
    }

    public static void addCenteredLog(String log)
    {
        addCenteredLog(log, new ChatStyle(), ' ');
    }

    public static void addCenteredLog(String log, ChatStyle style, char paddingChar)
    {
        String padding = getPaddingToCenter(log, paddingChar);
        IChatComponent paddingCC = new ChatComponentText(padding).setChatStyle(style);

        // TODO: this is stupid but i cba to do it the right way rn
        IChatComponent logCC = new ChatComponentText(log).setChatStyle(new ChatStyle().setStrikethrough(false));
        addLog(paddingCC.createCopy().appendSibling(logCC).appendSibling(paddingCC), false);
    }

    public static void addLog(ChatComponentText log, ChatStyle style, boolean doHeader)
    {
        IChatComponent chatComponent = log.setChatStyle(style);
        addLog(chatComponent, doHeader);
    }

    public static void addLog(IChatComponent chatComponent, boolean doHeader)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (player != null)
        {
            player.addChatMessage(doHeader ? header.createCopy().appendSibling(chatComponent) : chatComponent);
        }
    }

    private static String getPaddingToCenter(String log, char paddingChar) // TODO: doesnt work sometimes with even #'ed logs?
    {
        int chatWidth = Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatWidth();
        int logWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(log);
        int paddingCharWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(String.valueOf(paddingChar));

        if (logWidth >= chatWidth)
        {
            return "";
        }

        char[] padding = new char[((chatWidth - logWidth) / paddingCharWidth) / 2];
        Arrays.fill(padding, paddingChar);
        return new String(padding);
    }
}
