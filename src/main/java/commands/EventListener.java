package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface EventListener {
    void onMessage(GuildMessageReceivedEvent event);

    void onAirPurify(GuildMessageReceivedEvent event);

}
