package listeners;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public interface OnMessageListener {

    /**
     * Triggered when a mix message is received
     * A message is categorized as mix message if it contains more than alphabetic characters (e.g. emojis, links)
     *
     * @param msg The raw message received by the bot
     */
    void onMixedMessage(@NotNull Message msg);

    /**
     * Triggered when a private message is received
     * A message is categorized as private message if it contains || as prefix and suffix (i.e. ||something here||)
     *
     * @param msg The raw message received by the bot
     */
    void onHiddenMessage(@NotNull Message msg);

    /**
     * Triggered when a link message is received
     * A message is categorized as link message if and only if it contains only a link
     *
     * @param msg The raw message received by the bot
     */
    void onLinkMessage(@NotNull Message msg);

    /**
     * Triggered when an attachment message is received
     * A message is categorized as attachment message if and only if it contains only an attachment (e.g. image, video)
     *
     * @param msg The raw message received by the bot
     */
    void onAttachmentMessage(@NotNull Message msg);

    /**
     * Triggered when an emoji message is received
     * A message is categorized as emoji message if it contains only an emoji (i.e. :something:)
     *
     * @param msg The raw message received by the bot
     */
    void onEmojiMessage(@NotNull Message msg);
}
