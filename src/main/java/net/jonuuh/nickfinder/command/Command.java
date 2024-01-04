package net.jonuuh.nickfinder.command;

import net.jonuuh.nickfinder.config.Config;
import net.jonuuh.nickfinder.loggers.ChatLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.regex.Pattern;

public class Command extends CommandBase
{
    private final Minecraft mc;
    private final Config config;
    private final ChatLogger chatLogger;

    public Command(Minecraft mc, Config config, ChatLogger chatLogger)
    {
        this.mc = mc;
        this.config = config;
        this.chatLogger = chatLogger;
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName()
    {
        return "nickfinder";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender The command sender that executed the command
     */
    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "";
    }

    /**
     * Gets the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender The command sender that executed the command
     * @param args   The arguments that were passed
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayerSP) || args.length == 0)
        {
            return;
        }

        Commands command = Commands.none;

        try
        {
            command = Commands.valueOf(args[0]);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            addCommandFailureChatLog("Unknown command");
        }

        switch (command)
        {
            case none:
                break;

            case config:
                config.displayConfig();
                break;

            case addregex:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }

                if (config.addTargetRegexp(Pattern.compile(args[1])))
                {
                    addCommandSuccessChatLog("Added \"" + args[1] + "\" to target regexps.");
                }
                else
                {
                    addCommandFailureChatLog("Failed to add \"" + args[1] + "\".");
                }
                break;

            case removeregex:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }

                if (config.removeTargetRegexp(Pattern.compile(args[1])))
                {
                    addCommandSuccessChatLog("Removed \"" + args[1] + "\" from target regexps.");
                }
                else
                {
                    addCommandFailureChatLog("Failed to remove \"" + args[1] + "\".");
                }
                break;

            case clearregex:
                config.clearTargetRegexps();
                addCommandSuccessChatLog("Cleared target regexps.");
                break;

            case setnickdelay:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }

                try
                {
                    double newDelay = parseDouble(args[1], 0, 30);
                    config.setReqNickDelaySecs(newDelay);
                    addCommandSuccessChatLog("Set nick delay to: " + newDelay + " seconds.");
                }
                catch (NumberInvalidException e)
                {
                    e.printStackTrace();
                    addCommandFailureChatLog("Invalid number. [bounds = (0.0, 30.0)]");
                }
                break;

            case setantiafkdelay:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }

                try
                {
                    double newDelay = parseDouble(args[1], 0, 60 * 5);
                    config.setAntiAFKDelaySecs(newDelay);
                    addCommandSuccessChatLog("Set antiAFK delay to: " + newDelay + " seconds.");
                }
                catch (NumberInvalidException e)
                {
                    e.printStackTrace();
                    addCommandFailureChatLog("Invalid number. [bounds = (0.0, 300.0)]");
                }
                break;

            case setlobbymin:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }

                try
                {
                    int newMin = parseInt(args[1], 1, 100);
                    // prevents ThreadLocalRandom IllegalArgumentException
                    if (newMin > config.getLobbyMax())
                    {
                        addCommandFailureChatLog("Invalid number. [min > max]");
                        break;
                    }
                    config.setLobbyMin(newMin);
                    addCommandSuccessChatLog("Set minimum lobby to: " + newMin + ".");
                }
                catch (NumberInvalidException e)
                {
                    e.printStackTrace();
                    addCommandFailureChatLog("Invalid number. [bounds = (1, 100)]");
                }
                break;

            case setlobbymax:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }

                try
                {
                    int newMax = parseInt(args[1], 1, 100);
                    // prevents ThreadLocalRandom IllegalArgumentException
                    if (newMax < config.getLobbyMin())
                    {
                        addCommandFailureChatLog("Invalid number. [min > max]");
                        break;
                    }
                    config.setLobbyMax(newMax);
                    addCommandSuccessChatLog("Set maximum lobby to: " + newMax + ".");
                }
                catch (NumberInvalidException e)
                {
                    e.printStackTrace();
                    addCommandFailureChatLog("Invalid number. [bounds = (1, 100)]");
                }
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, Commands.getNames());
        }
        return null;
    }

    private boolean isArgsLenUnexpected(String[] args, int expectedLen)
    {
        if (args.length != expectedLen)
        {
            addCommandFailureChatLog("Command failed, wrong number of arguments.");
            return true;
        }
        return false;
    }

    private void addCommandSuccessChatLog(String log)
    {
        chatLogger.addLog(log, EnumChatFormatting.GREEN, false);
    }

    private void addCommandFailureChatLog(String log)
    {
        chatLogger.addLog(log, EnumChatFormatting.RED, false);
    }

//    private Pattern parseRegex(String regexStr)
//    {
//        try
//        {
//            return Pattern.compile(regexStr);
//        }
//        catch (PatternSyntaxException e)
//        {
//            e.printStackTrace();
//            addCommandLog("Invalid regex syntax.");
//            return null;
//        }
//    }

    private enum Commands
    {
        none,
        config,
        addregex,
        removeregex,
        clearregex,
        setnickdelay,
        setantiafkdelay,
        setlobbymin,
        setlobbymax;

        private static String[] getNames()
        {
            return new String[]{config.name(), addregex.name(), removeregex.name(), clearregex.name(),
                    setnickdelay.name(), setantiafkdelay.name(), setlobbymin.name(), setlobbymax.name()};
        }
    }
}
