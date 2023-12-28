package net.jonuuh.nickfinder.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

/**
 * Perform random player movements.
 */
public class AntiAFK
{
    private final int[] movementKeyCodes;
    private int currentRandomMove;
    public int tickStart;
    public static boolean running = false;

    /**
     * Instantiates a new AntiAFK.
     */
    public AntiAFK()
    {
        int keyCodeForward = Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode();
        int keyCodeBack = Minecraft.getMinecraft().gameSettings.keyBindBack.getKeyCode();
        int keyCodeRight = Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode();
        int keyCodeLeft = Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode();
        this.movementKeyCodes = new int[]{keyCodeForward, keyCodeBack, keyCodeRight, keyCodeLeft};
    }

    /**
     * Start random move.
     *
     * @param ticks the current number of client ticks passed since last toggle on
     */
    public void startRandomMove(int ticks)
    {
        int randomMoveKeyCode = movementKeyCodes[(int) Math.floor(Math.random() * 4)];
        KeyBinding.setKeyBindState(randomMoveKeyCode, true);
        currentRandomMove = randomMoveKeyCode;
        tickStart = ticks;
        running = true;
    }

    /**
     * End random move.
     */
    public void endRandomMove()
    {
        KeyBinding.setKeyBindState(currentRandomMove, false);
        running = false;
    }
}
