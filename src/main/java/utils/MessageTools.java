package utils;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.Commands.*;

public class MessageTools {
    public enum MessageType {MIXED_MSG, LINK_MSG, EMOJI_MSG, ATTACHMENT_MSG, HIDDEN_MSG, BOT_MSG}

    public static MessageType getMessageType(@NotNull Message msg){
        String msgString = msg.getContentRaw();


        if (msgString.startsWith("||") && msgString.endsWith("||"))
            return MessageType.HIDDEN_MSG;
        else if (msg.getAuthor().isBot())
            return MessageType.BOT_MSG;
        else if (msgString.matches(":.*:") || EmojiDetector.isEmojiOnly(msgString))
            return MessageType.EMOJI_MSG;
        else if (msgString.matches("[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)"))
            return MessageType.LINK_MSG;
        else if (msg.getAttachments().size() > 0)
            return MessageType.ATTACHMENT_MSG;
        else
            return MessageType.MIXED_MSG;
    }

    /**
     * Convert message msg to an existing command value
     *
     * @param event The message received by the bot
     * @return Returns NOT_FOUND when the command does not exist or msg is not a command
     */
    public static CommandContext extractCommand(SlashCommandEvent event){
        return switch (event.getName()) {
            case JOIN -> new CommandContext(JOIN);
            case PURIFY -> new CommandContext(Commands.PURIFY);
            case LEAVE -> new CommandContext(LEAVE);
            case SET_VOICE -> new CommandContext(Commands.SET_VOICE,
                    event.getOptionsByName("id").get(0).getAsString());
            case HELP -> new CommandContext(Commands.HELP);
            case CREDIT -> new CommandContext(CREDIT);
            default -> new CommandContext(Commands.NA);
        };

    }
}
