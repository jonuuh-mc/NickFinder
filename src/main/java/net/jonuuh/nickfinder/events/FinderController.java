package net.jonuuh.nickfinder.events;

import net.jonuuh.nickfinder.config.Config;
import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class FinderController
{
    private final KeyBinding debugKey;
    private final KeyBinding toggleKey;
    private final ChatLogger chatLogger;
    private final Finder finder;

    public FinderController(Minecraft mc, KeyBinding debugKey, KeyBinding toggleKey)
    {
        this.debugKey = debugKey;
        this.toggleKey = toggleKey;
        this.chatLogger = new ChatLogger("NickFinder", EnumChatFormatting.GRAY, EnumChatFormatting.GOLD, false);
        this.finder = new Finder(mc, new Config(), chatLogger);
    }

    /**
     * Key input event handler.
     *
     * @param event the event
     */
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if (debugKey.isPressed())
        {
            chatLogger.addLog("debugKey pressed");
            chatLogger.addLog(Minecraft.getMinecraft().thePlayer.getUniqueID().toString());
        }

        if (toggleKey.isPressed())
        {
            finder.toggle();
        }
    }
}
