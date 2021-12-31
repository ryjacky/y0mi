package commands;

import bots.ReadupBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pref.BotPreferences;

import java.util.HashMap;
import java.util.prefs.Preferences;

public class BotEventHandler extends ListenerAdapter {

    private Preferences pref;

    private HashMap<Long, ReadupBot> botInstances;

    public BotEventHandler() {
        pref = Preferences.userRoot().node(BotPreferences.PREF_PATH);
        botInstances = new HashMap<>();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);

        String rawMsg = event.getMessage().getContentRaw();
        Long msgGuildIdLong = event.getGuild().getIdLong();

        // Check for command
        if (rawMsg.startsWith(pref.get(BotPreferences.PREFIX[0], BotPreferences.PREFIX[1]))){
            if (rawMsg.endsWith(Commands.READ)) {
                botInstances.put(msgGuildIdLong, new ReadupBot().joinVC(event));
            } else if (rawMsg.endsWith(Commands.HELP)) {
                event.getAuthor().openPrivateChannel().queue((privateChannel -> {
                    EmbedBuilder messageEmbed = new EmbedBuilder();
                    messageEmbed.setColor(0xF57B42);
                    messageEmbed.setTitle("ヘルプ");
                    messageEmbed.addField(BotPreferences.getPrefix() + "read", "読み上げを始まる", false);
                    messageEmbed.addField(BotPreferences.getPrefix() + "ap", "空気を浄化する", false);
                    privateChannel.sendMessageEmbeds(messageEmbed.build()).queue();
                }));
            } else if (botInstances.containsKey(msgGuildIdLong)){   //if the bot is connected in the guild
                // bot instance specific commands go here
                if (Commands.isCommand(rawMsg)){
                    switch (rawMsg.replace("!", "")){
                        case "ap":
                            botInstances.get(msgGuildIdLong).onAirPurify(event);
                            break;
                    }
                }
            }
        } else if (botInstances.containsKey(msgGuildIdLong)) {
            if (!rawMsg.matches("::[a-zA-Z]+::")) {
                botInstances.get(msgGuildIdLong).onMessage(event);
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
