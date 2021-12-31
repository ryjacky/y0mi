import commands.BotEventHandler;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Main {
    public static final String TOKEN = "ODY3NDM2NjA4OTg1NDk3NjAw.YPhFSw.loafVGjIMX3HxYUqgD_Zm8cTx9s";

    public static void main(String[] args) {
        try {
            JDA jda = JDABuilder.createDefault(TOKEN).build();
            jda.addEventListener(new BotEventHandler());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}