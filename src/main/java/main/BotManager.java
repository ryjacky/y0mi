package main;

import bots.ReadupBot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import utils.BotPreferences;
import utils.MessagePresets;
import utils.MessageTools;

import java.util.HashMap;
import java.util.prefs.Preferences;

public class BotManager extends ListenerAdapter {

    private Preferences pref;

    private HashMap<Long, ReadupBot> botInstances;

    public BotManager() {
        pref = Preferences.userRoot().node(BotPreferences.PREF_PATH);
        botInstances = new HashMap<>();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);

        Message msg = event.getMessage();
        Long msgGuildId = msg.getGuild().getIdLong();

        if (MessageTools.isCommand(msg)){
            MessageTools.Command extractedCommand = MessageTools.extractCommand(msg);
            if (extractedCommand == MessageTools.Command.NOT_FOUND) {

            } else {
                switch (MessageTools.extractCommand(msg)){
                    case JOIN -> botInstances.put(msgGuildId, new ReadupBot().joinVC(event));
                    case HELP -> {
                        msg.getAuthor().openPrivateChannel().queue((privateChannel ->
                                privateChannel.sendMessageEmbeds(MessagePresets.helpMsg).queue()));
                    }

                    // Bot instance specific commands go after this line
                    // -----------------------------------------------------------------------------------
                    case PURIFY -> {
                        if (botInstances.containsKey(msgGuildId)) botInstances.get(msgGuildId).onAirPurify(event);
                    }

                }
            }
        } else {
            if (botInstances.containsKey(msgGuildId)){
                switch (MessageTools.getMessageType(msg)) {
                    case MIXED_MSG -> botInstances.get(msgGuildId).onMixedMessage(msg);
                    case EMOJI_MSG -> botInstances.get(msgGuildId).onEmojiMessage(msg);
                    case LINK_MSG -> botInstances.get(msgGuildId).onLinkMessage(msg);
                    case ATTACHMENT_MSG -> botInstances.get(msgGuildId).onAttachmentMessage(msg);
                    case HIDDEN_MSG -> botInstances.get(msgGuildId).onHiddenMessage(msg);
                }
            }
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);

        Long eventGuildIdLong = event.getGuild().getIdLong();

        if (botInstances.containsKey(eventGuildIdLong)){
            if (event.getChannelLeft().getMembers().size() <= 1){
                botInstances.get(eventGuildIdLong).leaveVC();
                botInstances.remove(eventGuildIdLong);
            }
        }
    }
}
