package net.jonuuh.nickfinder.config;

import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CommandNickFinder extends CommandBase
{
    private final Config config;
    private final String[] commandNames = new String[]{"info", "config", "setNickDelay", "setAFKDelay", "setLobbyMin", "setLobbyMax",
            "addTarget", "removeTarget", "clearTargets", "addFilter", "removeFilter", "clearFilters"};

    public CommandNickFinder()
    {
        this.config = ConfigHandler.getInstance().getConfig();
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
            displayCommands();
            return;
        }

        switch (args[0])
        {
            case "info":
                ChatLogger.addLog("[Mod Options] -> [NickFinder] -> [URL]", EnumChatFormatting.GREEN);
                return;

            case "config":
                config.displayConfig();
                return;

            case "clearTargets":
                config.clearTargetPatterns();
                ChatLogger.addLog("Cleared target patterns.", EnumChatFormatting.GREEN);
                break;

            case "clearFilters":
                config.clearFilterPatterns();
                ChatLogger.addLog("Cleared filter patterns.", EnumChatFormatting.GREEN);
                break;
        }

        if (args.length >= 2)
        {
            switch (args[0])
            {
                case "addTarget":
                    ChatLogger.addBoolLog("Added '" + args[1] + "'", "Failed to add '" + args[1] + "'", config.addTargetPattern(compilePattern(args[1])));
                    break;

                case "removeTarget":
                    ChatLogger.addBoolLog("Removed '" + args[1] + "'", "Failed to remove '" + args[1] + "'", config.removeTargetPattern(compilePattern(args[1])));
                    break;

                case "addFilter":
                    ChatLogger.addBoolLog("Added '" + args[1] + "'", "Failed to add '" + args[1] + "'", config.addFilterPattern(compilePattern(args[1])));
                    break;

                case "removeFilter":
                    ChatLogger.addBoolLog("Removed '" + args[1] + "'", "Failed to remove '" + args[1] + "'", config.removeFilterPattern(compilePattern(args[1])));
                    break;

                case "setNickDelay":
                    Double nickDelay = parseD(args[1], 0, 30);
                    if (nickDelay != null)
                    {
                        config.setNickDelaySecs(nickDelay);
                        ChatLogger.addLog("Set nick delay to: " + nickDelay + " seconds.", EnumChatFormatting.GREEN);
                    }
                    break;

                case "setAFKDelay":
                    Double afkDelay = parseD(args[1], 0, 60 * 5);
                    if (afkDelay != null)
                    {
                        config.setAFKDelaySecs(afkDelay);
                        ChatLogger.addLog("Set AFK delay to: " + afkDelay + " seconds.", EnumChatFormatting.GREEN);
                    }
                    break;

                case "setLobbyMin":
                    Integer min = parseI(args[1], 1, 100);
                    if (min != null && min < config.getLobbyMax())
                    {
                        config.setLobbyMin(min);
                        ChatLogger.addLog("Set min lobby to: " + min + ".", EnumChatFormatting.GREEN);
                        break;
                    }
                    ChatLogger.addLog("Invalid number. [min > max]", EnumChatFormatting.RED);
                    break;

                case "setLobbyMax":
                    Integer max = parseI(args[1], 1, 100);
                    if (max != null && max > config.getLobbyMin())
                    {
                        config.setLobbyMax(max);
                        ChatLogger.addLog("Set max lobby to: " + max + ".", EnumChatFormatting.GREEN);
                        break;
                    }
                    ChatLogger.addLog("Invalid number. [max < min]", EnumChatFormatting.RED);
                    break;
            }
        }

        ConfigHandler.getInstance().save();
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

    private void displayCommands()
    {
        ChatStyle headerStyle = new ChatStyle().setColor(EnumChatFormatting.BLUE).setStrikethrough(true);
        ChatStyle style = new ChatStyle().setColor(EnumChatFormatting.AQUA);

        ChatLogger.addCenteredLog(" NickFinder Commands ", headerStyle, '-');
        ChatLogger.addCenteredLog("/nickfinder info", style, ' ');
        ChatLogger.addCenteredLog("/nickfinder config", style, ' ');

        ChatLogger.addCenteredLog("/nickfinder setNickDelay", style, ' ');
        ChatLogger.addCenteredLog("/nickfinder setAFKDelay", style, ' ');
        ChatLogger.addCenteredLog("/nickfinder setLobbyMin", style, ' ');
        ChatLogger.addCenteredLog("/nickfinder setLobbyMax", style, ' ');

        ChatLogger.addCenteredLog("/nickfinder addTarget", style, ' ');
        ChatLogger.addCenteredLog("/nickfinder removeTarget", style, ' ');
        ChatLogger.addCenteredLog("/nickfinder clearTargets", style, ' ');

        ChatLogger.addCenteredLog("/nickfinder addFilter", style, ' ');
        ChatLogger.addCenteredLog("/nickfinder removeFilter", style, ' ');
        ChatLogger.addCenteredLog("/nickfinder clearFilters", style, ' ');
        ChatLogger.addCenteredLog("-", headerStyle, '-');
    }

    private Double parseD(String doubleStr, int min, int max)
    {
        try
        {
            return parseDouble(doubleStr, min, max);
        }
        catch (NumberInvalidException e)
        {
            ChatLogger.addLog("Invalid number. [bounds = (" + min + ", " + max + ")]", EnumChatFormatting.RED);
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
            ChatLogger.addLog("Invalid number. [bounds = (" + min + ", " + max + ")]", EnumChatFormatting.RED);
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
            ChatLogger.addLog("Invalid pattern syntax.", EnumChatFormatting.RED);
            return null;
        }
    }
}
