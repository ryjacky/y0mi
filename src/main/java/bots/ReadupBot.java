package bots;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.CommandListener;
import commands.BotEventHandler;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import voicevox.VoicevoxHelper;

import java.io.FileOutputStream;
import java.util.Random;

public class ReadupBot implements CommandListener {

    private TextChannel initTextChannel;

    private VoiceChannel vc;
    private AudioManager audioManager;

    public ReadupBot() {
    }

    public ReadupBot joinVC(GuildMessageReceivedEvent event) {
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
        audioManager.closeAudioConnection();
    }

    @Override
    public void onMessage(GuildMessageReceivedEvent event) {
        if (event.getChannel() == initTextChannel) {
            try {
                String fileNumber = String.valueOf(new Random().nextInt(100));
                new FileOutputStream(fileNumber).write(VoicevoxHelper.getWav(VoicevoxHelper.getQuery(event.getMessage().getContentRaw())));
                PlayerManager.getInstance().loadAndPlay(event.getChannel(), fileNumber);
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
