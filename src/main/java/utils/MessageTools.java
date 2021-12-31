package utils;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageTools {
    public enum MessageType {MIXED_MSG, LINK_MSG, EMOJI_MSG, ATTACHMENT_MSG, HIDDEN_MSG}

    public static MessageType getMessageType(@NotNull Message msg){
        String msgString = msg.getContentRaw();


        if (msgString.startsWith("||") && msgString.endsWith("||"))
            return MessageType.HIDDEN_MSG;
        else if (msgString.matches(":.*:"))
            return MessageType.EMOJI_MSG;
        else if (msgString.matches("[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)"))
            return MessageType.LINK_MSG;
        else if (msg.getAttachments().size() > 0)
            return MessageType.ATTACHMENT_MSG;
        else
            return MessageType.MIXED_MSG;
    }

    public static boolean isCommand(@NotNull Message msg){
        return msg.getContentRaw().matches(BotPreferences.getPrefix(msg.getGuild().getIdLong())
                + "[a-zA-Z]+");
    }

    /**
     * Convert message msg to an existing command value
     *
     * @param msg The message received by the bot
     * @return Returns NOT_FOUND when the command does not exist or msg is not a command
     */
    public static CommandContext extractCommand(Message msg){
        //TODO: Adapt case for parameter command
        return switch (msg.getContentRaw().replaceFirst(BotPreferences.getPrefix(msg.getGuild().getIdLong()), "")) {
            case "join" -> new CommandContext(CommandContext.Commands.JOIN, new ArrayList<>());
            case "ap" -> new CommandContext(CommandContext.Commands.PURIFY, new ArrayList<>());
            case "setvoice" -> new CommandContext(CommandContext.Commands.SET_VOICE, new ArrayList<>());
            case "setprefix" -> new CommandContext(CommandContext.Commands.SET_PREFIX, new ArrayList<>());
            case "help" -> new CommandContext(CommandContext.Commands.HELP, new ArrayList<>());
            default -> new CommandContext(CommandContext.Commands.NOT_FOUND, new ArrayList<>());
        };

    }
}
