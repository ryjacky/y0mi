package bots;

import listeners.CommandListener;
import listeners.OnMessageListener;
import lavaplayer.PlayerManager;
import utils.FilePaths;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import utils.MessagePresets;
import voicevox.VoicevoxHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class ReadupBot implements OnMessageListener, CommandListener {

    private TextChannel initTextChannel;

    private VoiceChannel vc;
    private AudioManager audioManager;

    public ReadupBot() {
    }

    public ReadupBot joinVC(@NotNull GuildMessageReceivedEvent event) {
        VoiceChannel newVC = event.getMember().getVoiceState().getChannel();

        if (vc == null && newVC != null) {
            vc = newVC;

            audioManager = event.getGuild().getAudioManager();
            audioManager.setSelfDeafened(true);

            final VoiceChannel memberChannel = newVC;

            audioManager.openAudioConnection(memberChannel);

            initTextChannel = event.getChannel();
        }

        return this;
    }

    public void leaveVC() {
        if (audioManager != null)
            audioManager.closeAudioConnection();
    }

    public void readMessage(String msg, TextChannel msgTextChannel){
        System.out.println(msg);
        try {
            String fileName = FilePaths.CACHE_PATH + "/" + (msg.length() < 30
                    ? msg
                    : String.valueOf(new Random().nextInt(100)));

            if (msg.length() >= 30 || !(new File(fileName).exists()))
                new FileOutputStream(fileName).write(VoicevoxHelper.getWav(VoicevoxHelper.getQuery(msg)));

            PlayerManager.getInstance().loadAndPlay(msgTextChannel, fileName);
        } catch (Exception e){
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
    public void onEmojiMessage(@NotNull Message msg) {

    }

    @Override
    public void onHiddenMessage(@NotNull Message msg) {
        readMessage(MessagePresets.privateMsgSent, msg.getTextChannel());
    }

    @Override
    public void onMixedMessage(@NotNull Message msg) {
        if (msg.getChannel() == initTextChannel) {
            String msgWithoutEmoji = msg.getContentRaw().replaceAll("<.*>", "");
            readMessage(msgWithoutEmoji, msg.getTextChannel());
        }
    }

    @Override
    public void onAirPurify(GenericGuildMessageEvent event) {
        if (event.getChannel() == initTextChannel) {
            try {
                PlayerManager.getInstance().loadAndPlay(event.getChannel(), FilePaths.SRC + "/airpurify.mp3");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
