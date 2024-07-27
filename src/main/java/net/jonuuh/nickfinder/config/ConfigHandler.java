package net.jonuuh.nickfinder.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ConfigHandler
{
    private static ConfigHandler instance;
    private final Configuration configuration;
    private final Config config;

    public static void createInstance(File configDir)
    {
        if (instance == null)
        {
            instance = new ConfigHandler(configDir);
        }
    }

    public static ConfigHandler getInstance()
    {
        if (instance == null)
        {
            throw new NullPointerException("ConfigHandler instance has not been created");
        }

        return instance;
    }

    // Load config (read data from Configuration, write it into Config)
    private ConfigHandler(File configDir)
    {
        this.configuration = new Configuration(new File(configDir, "nickfinder.cfg"));

        Set<Pattern> targetPatterns = new HashSet<>();
        Set<Pattern> filterPatterns = new HashSet<>();
        double nickDelaySecs;
        double afkDelaySecs;
        int lobbyMin;
        int lobbyMax;

        for (String target : configuration.get("main", "targetPatterns", new String[]{}).getStringList())
        {
            targetPatterns.add(compilePattern(target));
        }

        for (String filter : configuration.get("main", "filterPatterns", new String[]{}).getStringList())
        {
            filterPatterns.add(compilePattern(filter));
        }

        nickDelaySecs = configuration.get("main", "nickDelaySecs", 1.0D).getDouble();
        afkDelaySecs = configuration.get("main", "afkDelaySecs", (60.0D * 4.0D)).getDouble();

        lobbyMin = configuration.get("main", "lobbyMin", 1).getInt();
        lobbyMax = configuration.get("main", "lobbyMax", 4).getInt();

        this.config = new Config(targetPatterns, filterPatterns, nickDelaySecs, afkDelaySecs, lobbyMin, lobbyMax);
        System.out.println("[NickFinder] Loaded config: " + config);
    }

    public Config getConfig()
    {
        return config;
    }

    // Save config (read data from Config, write it into Configuration)
    public void save()
    {
        String[] targets = config.getTargetPatterns().stream().map(Pattern::toString).toArray(String[]::new);
        String[] filters = config.getFilterPatterns().stream().map(Pattern::toString).toArray(String[]::new);

        configuration.get("main", "targetPatterns", targets).setValues(targets);
        configuration.get("main", "filterPatterns", filters).setValues(filters);

        configuration.get("main", "nickDelaySecs", config.getNickDelaySecs()).setValue(config.getNickDelaySecs());
        configuration.get("main", "afkDelaySecs", config.getAFKDelaySecs()).setValue(config.getAFKDelaySecs());

        configuration.get("main", "lobbyMin", config.getLobbyMin()).setValue(config.getLobbyMin());
        configuration.get("main", "lobbyMax", config.getLobbyMax()).setValue(config.getLobbyMax());

        if (configuration.hasChanged())
        {
            configuration.save();
        }
    }

    private Pattern compilePattern(String patternStr)
    {
        try
        {
            return Pattern.compile(patternStr);
        }
        catch (PatternSyntaxException e)
        {
            System.out.println("Invalid pattern syntax.");
            return null;
        }
    }
}
