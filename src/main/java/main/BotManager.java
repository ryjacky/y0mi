package main;

import bots.ReadupBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import utils.BotPreferences;
import utils.CommandContext;
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
            CommandContext extractedCommandContext = MessageTools.extractCommand(msg);
            if (CommandContext.Commands.NOT_FOUND == extractedCommandContext.getCommand()) {

            } else {
                switch (MessageTools.extractCommand(msg).getCommand()){
                    case JOIN -> botInstances.put(msgGuildId, new ReadupBot().joinVC(event));
                    case SET_PREFIX -> BotPreferences.setPrefix(msgGuildId, MessageTools.extractCommand(msg).getParameters().get(0));
                    case SET_VOICE -> {
                        if (msg.getContentRaw().matches("[0-9]+"))
                            BotPreferences.setVoice(msgGuildId, Integer.parseInt(MessageTools.extractCommand(msg).getParameters().get(0)));
                        else {
                            //TODO: When input parameter not int
                        }
                    }
                    case HELP -> {
                        MessageEmbed helpMsg = new EmbedBuilder()
                                .setColor(0xF57B42)
                                .setTitle("ヘルプ")
                                .addField(BotPreferences.getPrefix(msgGuildId) + "join", "読み上げを始まる", false)
                                .addField(BotPreferences.getPrefix(msgGuildId) + "ap", "空気清浄機をONにする", false).build();

                        msg.getAuthor().openPrivateChannel().queue((privateChannel ->
                                privateChannel.sendMessageEmbeds(helpMsg).queue()));
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
