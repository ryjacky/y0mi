package bots;

import commands.EventListener;
import lavaplayer.PlayerManager;
import main.FilePaths;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import voicevox.VoicevoxHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class ReadupBot implements EventListener {

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

    @Override
    public void onMessage(GuildMessageReceivedEvent event) {
        if (event.getChannel() == initTextChannel) {
            try {
                String fileName = FilePaths.CACHE_PATH + "/" + (event.getMessage().getContentRaw().length() < 30
                        ? event.getMessage().getContentRaw()
                        : String.valueOf(new Random().nextInt(100)));

                if ((event.getMessage().getContentRaw().length() < 30 && !(new File(fileName).exists()))
                        || event.getMessage().getContentRaw().length() >= 30)
                    new FileOutputStream(fileName).write(VoicevoxHelper.getWav(VoicevoxHelper.getQuery(event.getMessage().getContentRaw())));

                PlayerManager.getInstance().loadAndPlay(event.getChannel(), fileName);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAirPurify(GuildMessageReceivedEvent event) {
        if (event.getChannel() == initTextChannel) {
            try {
                PlayerManager.getInstance().loadAndPlay(event.getChannel(), ClassLoader.getSystemClassLoader().getResource("airpurify.mp3").getPath());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
