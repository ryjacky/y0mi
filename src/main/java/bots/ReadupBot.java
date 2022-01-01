package bots;

import lavaplayer.PlayerManager;
import listeners.CommandListener;
import listeners.OnMessageListener;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import utils.BotPreferences;
import utils.FilePaths;
import utils.MessagePresets;
import voicevox.VoicevoxHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Time;
import java.util.Date;
import java.util.Random;

public class ReadupBot implements OnMessageListener, CommandListener {

    private TextChannel initTextChannel;

    private VoiceChannel vc;
    private AudioManager audioManager;

    public ReadupBot() {
    }

    public ReadupBot joinVC(@NotNull SlashCommandEvent event) throws Exception {
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            throw new Exception("Member not in channel");
        }

        VoiceChannel newVC = event.getMember().getVoiceState().getChannel();

        if (vc == null && newVC != null) {
            vc = newVC;

            audioManager = event.getGuild().getAudioManager();
            audioManager.setSelfDeafened(true);

            audioManager.openAudioConnection(newVC);

            initTextChannel = event.getTextChannel();
        }

        return this;
    }

    public void leaveVC() {
        if (audioManager != null)
            audioManager.closeAudioConnection();
    }

    public void readMessage(String msg, TextChannel msgTextChannel) {
        Long msgGuildId = msgTextChannel.getGuild().getIdLong();

        try {
            String fileName = FilePaths.CACHE_PATH + "/" + BotPreferences.getVoice(msgGuildId) + (msg.length() < 30
                    ? msg
                    : String.valueOf(new Random().nextInt(100)));

            System.out.println(new Date() + " ::::: File [" + fileName +  "] - " + msg);

            if (msg.length() >= 30 || !(new File(fileName).exists())) {
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(
                        VoicevoxHelper.getWav(
                                msgGuildId,
                                VoicevoxHelper.getQuery(
                                        msgGuildId,
                                        msg)
                        ));
                fos.close();
                fos.flush();
            }

            PlayerManager.getInstance().loadAndPlay(msgTextChannel, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLinkMessage(@NotNull Message msg) {
        readMessage(MessagePresets.urlShortened, msg.getTextChannel());
    }

    @Override
    public void onAttachmentMessage(@NotNull Message msg) {
        if (msg.getAttachments().get(0).isImage())
            readMessage(MessagePresets.imageSent, msg.getTextChannel());
        else if (msg.getAttachments().get(0).isVideo())
            readMessage(MessagePresets.videoSent, msg.getTextChannel());
    }

    @Override
    public void onBotMessage(@NotNull Message msg) {

    }

    @Override
    public void onEmojiMessage(@NotNull Message msg) {

    }

    @Override
    public void onHiddenMessage(@NotNull Message msg) {
        readMessage(MessagePresets.privateMsgSent, msg.getTextChannel());
    }

    @Override
    public void onMixedMessage(@NotNull Message msg) {
        if (msg.getTextChannel() == initTextChannel) {
            String msgWithoutEmoji = msg.getContentRaw().replaceAll("<.*>", "");
            readMessage(msgWithoutEmoji, msg.getTextChannel());
        }
    }

    @Override
    public void onAirPurify(SlashCommandEvent event) {
        if (event.getTextChannel() == initTextChannel) {
            try {
                PlayerManager.getInstance().loadAndPlay(event.getTextChannel(), FilePaths.SFX + "/airpurify.mp3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
