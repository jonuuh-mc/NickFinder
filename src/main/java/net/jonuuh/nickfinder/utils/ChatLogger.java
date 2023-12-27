package net.jonuuh.nickfinder.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class ChatLogger
{
    private static final String logPreString = "[NickFinder] > ";
    private final ChatStyle logPreStyle = new ChatStyle();
    private final ChatStyle logStyle = new ChatStyle();

    public ChatLogger(EnumChatFormatting preColor)
    {
        this.logPreStyle.setColor(preColor);
        this.logPreStyle.setBold(true);
    }

    public void addLog(String log)
    {
        addLog(log, EnumChatFormatting.WHITE, false);
    }

    public void addLog(String log, EnumChatFormatting color, boolean isBold)
    {
        ChatComponentText logPreComponent = new ChatComponentText(logPreString);
        ChatComponentText logComponent = new ChatComponentText(log);

        logPreComponent.setChatStyle(this.logPreStyle);
        logComponent.setChatStyle(this.logStyle.setColor(color).setBold(isBold));

        try
        {
            Minecraft.getMinecraft().thePlayer.addChatMessage(logPreComponent.appendSibling(logComponent));
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
}
