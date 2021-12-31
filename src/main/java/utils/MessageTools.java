package utils;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class MessageTools {
    public enum MessageType {MIXED_MSG, LINK_MSG, EMOJI_MSG, ATTACHMENT_MSG, HIDDEN_MSG}
    public enum Command {JOIN, PURIFY, HELP, NOT_FOUND}

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
        return msg.getContentRaw().matches(BotPreferences.getPrefix()
                + "[a-zA-Z]+");
    }

    /**
     * Convert message msg to an existing command value
     *
     * @param msg The message received by the bot
     * @return Returns NOT_FOUND when the command does not exist or msg is not a command
     */
    public static Command extractCommand(Message msg){
        return switch (msg.getContentRaw().replaceFirst(BotPreferences.getPrefix(), "")) {
            case "join" -> Command.JOIN;
            case "ap" -> Command.PURIFY;
            case "help" -> Command.HELP;
            default -> Command.NOT_FOUND;
        };

    }
}
