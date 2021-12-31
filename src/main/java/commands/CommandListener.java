package commands;

import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface CommandListener {
    void onMessage(GuildMessageReceivedEvent event);
    void onAirPurify(GuildMessageReceivedEvent event);

}
