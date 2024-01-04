package net.jonuuh.nickfinder.events;

import net.jonuuh.nickfinder.config.Config;
import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.regex.Pattern;

public class FinderController
{
    private final KeyBinding debugKey;
    private final KeyBinding toggleKey;
    private final ChatLogger chatLogger;
    private final Finder finder;
    private final Config config;

    public FinderController(Minecraft mc, Config config, ChatLogger chatLogger, KeyBinding debugKey, KeyBinding toggleKey)
    {
        this.chatLogger = chatLogger;
        this.debugKey = debugKey;
        this.toggleKey = toggleKey;
        this.config = config;
        this.finder = new Finder(mc, config, chatLogger);
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
