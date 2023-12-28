package net.jonuuh.nickfinder.loggers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

/**
 * Logs info client-side only in the in-game chat
 */
public class ChatLogger
{
    private static final String logPreString = "[NickFinder] > ";

    private final ChatStyle logPreStyle = new ChatStyle();
    private final ChatStyle logStyle = new ChatStyle();

    /**
     * Instantiates a new ChatLogger.
     *
     * @param preColor the log prefix color
     */
    public ChatLogger(EnumChatFormatting preColor)
    {
        this.logPreStyle.setColor(preColor);
        this.logPreStyle.setBold(true);
    }

    /**
     * Add a log to the ChatLogger.
     *
     * @param log the log
     */
    public void addLog(String log)
    {
        addLog(log, EnumChatFormatting.WHITE, false);
    }

    /**
     * Add a log to the ChatLogger.
     *
     * @param log    the log
     * @param color  the log color
     * @param isBold whether the log is bold
     */
    public void addLog(String log, EnumChatFormatting color, boolean isBold)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null)
        {
            return;
        }

        ChatComponentText logPreComponent = new ChatComponentText(logPreString);
        ChatComponentText logComponent = new ChatComponentText(log);

        logPreComponent.setChatStyle(this.logPreStyle);
        logComponent.setChatStyle(this.logStyle.setColor(color).setBold(isBold));

        player.addChatMessage(logPreComponent.appendSibling(logComponent));
    }
}
