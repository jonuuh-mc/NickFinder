package net.jonuuh.nickfinder.config;

import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Config
{
    private final Minecraft mc;
    private final ChatLogger chatLogger;

    private Set<Pattern> targetRegexps;
    private Set<String> limboStrings;
    private double reqNickDelaySecs;
    private double antiAFKDelaySecs;
    private int lobbyMin;
    private int lobbyMax;

    public Config(Minecraft mc, ChatLogger chatLogger)
    {
        this.mc = mc;
        this.chatLogger = chatLogger;
        this.targetRegexps = new HashSet<>(Collections.singletonList(Pattern.compile("^[A-Z][a-z]+$")));
        this.limboStrings = new HashSet<>(Arrays.asList("You are AFK. Move around to return from AFK.", "A kick occurred in your connection, so you have been routed to limbo!", "You were spawned in Limbo."));
        this.reqNickDelaySecs = 1;
        this.antiAFKDelaySecs = (60 * 2);
        this.lobbyMin = 2;
        this.lobbyMax = 10;
    }

    public Set<Pattern> getTargetRegexps()
    {
        return targetRegexps;
    }

    public Set<String> getLimboStrings()
    {
        return limboStrings;
    }

    public double getReqNickDelaySecs()
    {
        return reqNickDelaySecs;
    }

    public double getAntiAFKDelaySecs()
    {
        return antiAFKDelaySecs;
    }

    public int getLobbyMin()
    {
        return lobbyMin;
    }

    public int getLobbyMax()
    {
        return lobbyMax;
    }

    public boolean addTargetRegexp(Pattern targetRegexp)
    {
        boolean alreadyPresent = targetRegexps.stream().anyMatch(pattern -> pattern.pattern().equals(targetRegexp.pattern()));
        return !alreadyPresent ? targetRegexps.add(targetRegexp) : false;
    }

    public boolean removeTargetRegexp(Pattern targetRegexp)
    {
        return targetRegexps.removeIf(pattern -> pattern.pattern().equals(targetRegexp.pattern()));
    }

    public void clearTargetRegexps()
    {
        targetRegexps.clear();
    }

    public void setReqNickDelaySecs(double reqNickDelaySecs)
    {
        this.reqNickDelaySecs = reqNickDelaySecs;
    }

    public void setAntiAFKDelaySecs(double antiAFKDelaySecs)
    {
        this.antiAFKDelaySecs = antiAFKDelaySecs;
    }

    public void setLobbyMin(int lobbyMin)
    {
        this.lobbyMin = lobbyMin;
    }

    public void setLobbyMax(int lobbyMax)
    {
        this.lobbyMax = lobbyMax;
    }

    public void displayConfig()
    {
        chatLogger.addLog("Target regexps: " + targetRegexps, EnumChatFormatting.BLUE, false);
        chatLogger.addLog("Nick delay (secs): " + reqNickDelaySecs, EnumChatFormatting.BLUE, false);
        chatLogger.addLog("AntiAFK delay (secs): " + antiAFKDelaySecs, EnumChatFormatting.BLUE, false);
        chatLogger.addLog("Lobby min: " + lobbyMin, EnumChatFormatting.BLUE, false);
        chatLogger.addLog("Lobby max: " + lobbyMax, EnumChatFormatting.BLUE, false);
    }
}
