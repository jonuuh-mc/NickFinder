package net.jonuuh.nickfinder.config;

import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.BlockPos;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CommandNickFinder extends CommandBase
{
    private final Config config;
    private final String[] commandNames = new String[]{"config", "setNickDelay", "setAFKDelay", "setLobbyMin", "setLobbyMax",
            "addTarget", "removeTarget", "clearTargets", "addFilter", "removeFilter", "clearFilters"};

    public CommandNickFinder(Config config)
    {
        this.config = config;
    }

    @Override
    public String getCommandName()
    {
        return "nickfinder";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayerSP))
        {
            return;
        }

        if (args.length == 0)
        {
            ChatLogger.addLog("Commands: " + Arrays.toString(commandNames));
            return;
        }

        switch (args[0])
        {
            case "config":
                config.displayConfig();
                return;

            case "clearTargets":
                config.clearTargetPatterns();
                ChatLogger.addLog("Cleared target patterns.");
                return;

            case "clearFilters":
                config.clearFilterPatterns();
                ChatLogger.addLog("Cleared filter patterns.");
                return;
        }

        if (args.length != 2)
        {
            return;
        }

        switch (args[0])
        {
            case "addTarget":
                ChatLogger.addLog((config.addTargetPattern(compilePattern(args[1])) ? "Added: " : "Failed to add: ") + args[1]);
                break;

            case "removeTarget":
                ChatLogger.addLog((config.removeTargetPattern(compilePattern(args[1])) ? "Removed: " : "Failed to remove: ") + args[1]);
                break;

            case "addFilter":
                ChatLogger.addLog((config.addFilterPattern(compilePattern(args[1])) ? "Added: " : "Failed to add: ") + args[1]);
                break;

            case "removeFilter":
                ChatLogger.addLog((config.removeFilterPattern(compilePattern(args[1])) ? "Removed: " : "Failed to remove: ") + args[1]);
                break;

            case "setNickDelay":
                Double nickDelay = parseD(args[1], 0, 30);
                if (nickDelay != null)
                {
                    config.setNickDelaySecs(nickDelay);
                    ChatLogger.addLog("Set nick delay to: " + nickDelay + " seconds.");
                }
                break;

            case "setAFKDelay":
                Double afkDelay = parseD(args[1], 0, 60 * 5);
                if (afkDelay != null)
                {
                    config.setAFKDelaySecs(afkDelay);
                    ChatLogger.addLog("Set AFK delay to: " + afkDelay + " seconds.");
                }
                break;

            case "setLobbyMin":
                Integer min = parseI(args[1], 1, 100);
                if (min != null && min < config.getLobbyMax())
                {
                    config.setLobbyMin(min);
                    ChatLogger.addLog("Set min lobby to: " + min + ".");
                    break;
                }
                ChatLogger.addLog("Invalid number. [min > max]");
                break;

            case "setLobbyMax":
                Integer max = parseI(args[1], 1, 100);
                if (max != null && max > config.getLobbyMin())
                {
                    config.setLobbyMax(max);
                    ChatLogger.addLog("Set max lobby to: " + max + ".");
                    break;
                }
                ChatLogger.addLog("Invalid number. [max < min]");
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, commandNames);
        }
        return null;
    }

    private Double parseD(String doubleStr, int min, int max)
    {
        try
        {
            return parseDouble(doubleStr, min, max);
        }
        catch (NumberInvalidException e)
        {
            ChatLogger.addLog("Invalid number. [bounds = (" + min + ", " + max + ")]");
            return null;
        }
    }

    private Integer parseI(String intStr, int min, int max)
    {
        try
        {
            return parseInt(intStr, min, max);
        }
        catch (NumberInvalidException e)
        {
            ChatLogger.addLog("Invalid number. [bounds = (" + min + ", " + max + ")]");
            return null;
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
            ChatLogger.addLog("Invalid pattern syntax.");
            return null;
        }
    }
}
