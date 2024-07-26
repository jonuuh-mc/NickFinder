package net.jonuuh.nickfinder.config;

import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Config
{
    private final Set<Pattern> targetPatterns;
    private final Set<Pattern> filterPatterns;
    private double nickDelaySecs;
    private double afkDelaySecs;
    private int lobbyMin;
    private int lobbyMax;

    public Config()
    {
        this.targetPatterns = new HashSet<>(Collections.singletonList(Pattern.compile("^[A-Z][a-z]+$")));
        this.filterPatterns = new HashSet<>(Collections.singletonList(Pattern.compile("\\d")));
        this.nickDelaySecs = 1;
        this.afkDelaySecs = (60 * 4);
        this.lobbyMin = 1;
        this.lobbyMax = 4;
    }

    public Set<Pattern> getTargetPatterns()
    {
        return targetPatterns;
    }

    public Set<Pattern> getFilterPatterns()
    {
        return filterPatterns;
    }

    public double getNickDelaySecs()
    {
        return nickDelaySecs;
    }

    public double getAFKDelaySecs()
    {
        return afkDelaySecs;
    }

    public int getLobbyMin()
    {
        return lobbyMin;
    }

    public int getLobbyMax()
    {
        return lobbyMax;
    }

    public void setNickDelaySecs(double nickDelaySecs)
    {
        this.nickDelaySecs = nickDelaySecs;
    }

    public void setAFKDelaySecs(double afkDelaySecs)
    {
        this.afkDelaySecs = afkDelaySecs;
    }

    public void setLobbyMin(int lobbyMin)
    {
        this.lobbyMin = lobbyMin;
    }

    public void setLobbyMax(int lobbyMax)
    {
        this.lobbyMax = lobbyMax;
    }

    public boolean addTargetPattern(Pattern patternIn)
    {
        return isNotNull(patternIn) && targetPatterns.add(patternIn);
    }

    public boolean addFilterPattern(Pattern patternIn)
    {
        return isNotNull(patternIn) && filterPatterns.add(patternIn);
    }

    public boolean removeTargetPattern(Pattern patternIn)
    {
        return isNotNull(patternIn) && targetPatterns.removeIf(pattern -> pattern.pattern().equals(patternIn.pattern()));
    }

    public boolean removeFilterPattern(Pattern patternIn)
    {
        return isNotNull(patternIn) && filterPatterns.removeIf(pattern -> pattern.pattern().equals(patternIn.pattern()));
    }

    public void clearTargetPatterns()
    {
        targetPatterns.clear();
    }

    public void clearFilterPatterns()
    {
        filterPatterns.clear();
    }

    private boolean isNotNull(Pattern patternIn)
    {
        return patternIn != null;
    }

    public void displayConfig()
    {
        ChatLogger.addLog("Target patterns: " + targetPatterns, EnumChatFormatting.BLUE);
        ChatLogger.addLog("Filter patterns: " + filterPatterns, EnumChatFormatting.BLUE);
        ChatLogger.addLog("Nick delay (secs): " + nickDelaySecs, EnumChatFormatting.BLUE);
        ChatLogger.addLog("AFK delay (secs): " + afkDelaySecs, EnumChatFormatting.BLUE);
        ChatLogger.addLog("Lobby min: " + lobbyMin, EnumChatFormatting.BLUE);
        ChatLogger.addLog("Lobby max: " + lobbyMax, EnumChatFormatting.BLUE);
    }
}
