package main;

import bots.ReadupBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import utils.BotPreferences;
import utils.CommandContext;
import utils.MessagePresets;
import utils.MessageTools;

import java.util.Date;
import java.util.HashMap;

import static utils.Commands.*;

public class BotManager extends ListenerAdapter {

    private HashMap<Long, ReadupBot> botInstances;

    public BotManager() {
        botInstances = new HashMap<>();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);

        Message msg = event.getMessage();
        Long msgGuildId = msg.getGuild().getIdLong();

        if (botInstances.containsKey(msgGuildId)){
            switch (MessageTools.getMessageType(msg)) {
                case MIXED_MSG -> botInstances.get(msgGuildId).onMixedMessage(msg);
                case EMOJI_MSG -> botInstances.get(msgGuildId).onEmojiMessage(msg);
                case LINK_MSG -> botInstances.get(msgGuildId).onLinkMessage(msg);
                case ATTACHMENT_MSG -> botInstances.get(msgGuildId).onAttachmentMessage(msg);
                case HIDDEN_MSG -> botInstances.get(msgGuildId).onHiddenMessage(msg);
                case BOT_MSG -> botInstances.get(msgGuildId).onBotMessage(msg);
            }
        }

    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);

        Long eventGuildIdLong = event.getGuild().getIdLong();

        if (botInstances.containsKey(eventGuildIdLong)){
            if (botInstances.get(eventGuildIdLong).getVc().getIdLong() == event.getChannelLeft().getIdLong()
                    && event.getChannelLeft().getMembers().size() <= 1){
                botInstances.get(eventGuildIdLong).leaveVC();
                botInstances.remove(eventGuildIdLong);
                System.out.println(new Date() + "Disconnected from " + eventGuildIdLong);
            }
        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        super.onSlashCommand(event);

        if (!event.isFromGuild())
            return;

        Long cmdGuildId = event.getGuild().getIdLong();

        CommandContext extractedCommandContext = MessageTools.extractCommand(event);
        switch (extractedCommandContext.getCommand()){
            case JOIN -> {
                try {
                    botInstances.put(cmdGuildId, new ReadupBot().joinVC(event));
                    event.reply(MessagePresets.channelJoined).setEphemeral(false).queue();

                } catch (Exception e){
                    event.reply(MessagePresets.memberNotInChannel).setEphemeral(true).queue();

                }
            }
            case LEAVE -> {
                if (botInstances.containsKey(cmdGuildId)){
                        botInstances.get(cmdGuildId).leaveVC();
                        botInstances.remove(cmdGuildId);
                }
                event.reply(MessagePresets.channelLeaved).setEphemeral(false).queue();
            }
            case SET_VOICE -> {
                if (extractedCommandContext.getOptions()[0].matches("[0-9]")) {
                    BotPreferences.setVoice(cmdGuildId, extractedCommandContext.getOptions()[0]);
                    event.reply(MessagePresets.setVoiceSuccess).setEphemeral(false).queue();

                } else {
                    event.reply(MessagePresets.setVoiceFailed).setEphemeral(true).queue();
                }
            }
            case HELP -> event.replyEmbeds(generateHelp()).setEphemeral(true).queue();
            case CREDIT -> event.replyEmbeds(generateCredit()).setEphemeral(true).queue();

            // Bot instance specific commands go after this line
            // -----------------------------------------------------------------------------------
            case PURIFY -> {
                if (botInstances.containsKey(cmdGuildId)) {
                    botInstances.get(cmdGuildId).onAirPurify(event);
                    event.reply(MessagePresets.airPurify).setEphemeral(false).queue();
                }
            }

            default -> event.reply(MessagePresets.cmdNotFound).setEphemeral(false).queue();

        }

    }

    private MessageEmbed generateHelp(){
        return new EmbedBuilder()
                .setColor(0xF57B42)
                .setTitle("ヘルプ")
                .addField("/join", "読み上げを始める", false)
                .addField("/setvoice [id]", """
                        読み上げのボイスを変える、設定できるボイスは以下となります
                        [id] は　2, 3, 8, 9　の一つになります、詳細は下の説明をご覧ください
                        例：/setvoice 2
                        
                        四国めたん：2
                        ずんだもん：3
                        春日部つむぎ：8
                        波音リツ：9""", false)
                .addField("/ap", "空気清浄機をONにする", false).build();
    }

    private MessageEmbed generateCredit(){
        return new EmbedBuilder()
                .setColor(0xF57B42)
                .setTitle("クレジット")
                .addField("VOICEVOX:四国めたん", "", false)
                .addField("VOICEVOX:ずんだもん", "", false)
                .addField("VOICEVOX:春日部つむぎ", "", false)
                .addField("VOICEVOX:波音リツ", "", false)
                .addField("VOICEVOX", "voicevox.hiroshiba.jp", false)
                .build();
    }

    public void stop(){
        botInstances.forEach((key, value) -> {
            botInstances.get(key).leaveVC();
        });
    }
}
