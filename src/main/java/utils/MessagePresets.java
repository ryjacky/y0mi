package utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class MessagePresets {
    public static final MessageEmbed helpMsg = new EmbedBuilder()
            .setColor(0xF57B42)
            .setTitle("ヘルプ")
            .addField(BotPreferences.getPrefix() + "read", "読み上げを始まる", false)
            .addField(BotPreferences.getPrefix() + "ap", "空気清浄機をONにする", false).build();

    public static final String urlShortened = "URL省略";
    public static final String imageSent = "画像がそうしんされました";
    public static final String videoSent = "動画がそうしんされました";
    public static final String privateMsgSent = "隠された文字がそうしんされました";
}
