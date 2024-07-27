package net.jonuuh.nickfinder.config;

import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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

    Config(Set<Pattern> targetPatterns, Set<Pattern> filterPatterns, double nickDelaySecs, double afkDelaySecs, int lobbyMin, int lobbyMax)
    {
        this.targetPatterns = targetPatterns;
        this.filterPatterns = filterPatterns;
        this.nickDelaySecs = nickDelaySecs;
        this.afkDelaySecs = afkDelaySecs;
        this.lobbyMin = lobbyMin;
        this.lobbyMax = lobbyMax;
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
        return isNotNull(patternIn) && !containsPattern(targetPatterns, patternIn) && targetPatterns.add(patternIn);
    }

    public boolean addFilterPattern(Pattern patternIn)
    {
        return isNotNull(patternIn) && !containsPattern(filterPatterns, patternIn) && filterPatterns.add(patternIn);
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

    private boolean containsPattern(Set<Pattern> set, Pattern patternIn)
    {
        for (Pattern pattern : set)
        {
            if (pattern.pattern().equals(patternIn.pattern()))
            {
                return true;
            }
        }
        return false;
    }

    public void displayConfig()
    {
        ChatStyle headerStyle = new ChatStyle().setColor(EnumChatFormatting.BLUE).setStrikethrough(true);
        ChatStyle style = new ChatStyle().setColor(EnumChatFormatting.AQUA);

        ChatLogger.addCenteredLog(" NickFinder Config ", headerStyle, '-');
        ChatLogger.addCenteredLog("Target patterns: " + targetPatterns, style, ' ');
        ChatLogger.addCenteredLog("Filter patterns: " + filterPatterns, style, ' ');
        ChatLogger.addCenteredLog("Nick delay (secs): " + nickDelaySecs, style, ' ');
        ChatLogger.addCenteredLog("AFK delay (secs): " + afkDelaySecs, style, ' ');
        ChatLogger.addCenteredLog("Lobby min: " + lobbyMin, style, ' ');
        ChatLogger.addCenteredLog("Lobby max: " + lobbyMax, style, ' ');
        ChatLogger.addCenteredLog("-", headerStyle, '-');
    }
}
