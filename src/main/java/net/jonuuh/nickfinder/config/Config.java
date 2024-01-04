package net.jonuuh.nickfinder.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Config
{
    private Set<String> limboStrings;
    private double reqNickDelaySecs;
    private double antiAFKDelaySecs;
    private int lobbyMin;
    private int lobbyMax;

    public Config()
    {
        this.limboStrings = new HashSet<>(Arrays.asList("You are AFK. Move around to return from AFK.", "A kick occurred in your connection, so you have been routed to limbo!"));
        this.reqNickDelaySecs = 1;
        this.antiAFKDelaySecs = (10);
        this.lobbyMin = 1;
        this.lobbyMax = 5;
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
}
