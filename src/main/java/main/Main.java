package main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import utils.BotPreferences;
import utils.FilePaths;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Scanner;
import static utils.BotPreferences.TOKEN;
import static utils.Commands.*;

public class Main {

    public static void main(String[] args) {
        createFolderIfNotExists(FilePaths.CACHE_PATH);
        createFolderIfNotExists(FilePaths.SFX);

        Scanner scanner = new Scanner(System.in);

        String botToken = BotPreferences.getToken(TOKEN);
        if (botToken.equals("")){
            System.out.print("Discord token not set, please enter your discord token: ");
            botToken = scanner.nextLine();

            BotPreferences.setToken(botToken);
        }

        final BotManager botManager = new BotManager();

        try {
            JDA jda = JDABuilder.createDefault(botToken).build();
            jda.addEventListener(botManager);

            jda.updateCommands()
                    .addCommands(new CommandData(HELP, "マニュアルを呼び出す"))
                    .addCommands(new CommandData(JOIN, "読み上げを始める"))
                    .addCommands(new CommandData(SET_VOICE, "設定できるボイスは/helpから").addOption(
                            OptionType.INTEGER, "id", "ボイスID"))
                    .addCommands(new CommandData(PURIFY, "空気清浄機"))
                    .addCommands(new CommandData(LEAVE, "読み上げを終了します"))
                    .addCommands(new CommandData(CREDIT, "読み上げを終了します"))
                    .queue();

        } catch (LoginException e) {
            e.printStackTrace();
        }

        while (true){
            String[] command = scanner.nextLine().split(" ");
            switch (command[0]){
                case T_STOP -> {
                    botManager.stop();
                    System.exit(0);
                }
                case T_SET -> {
                    if (command[1].equals(T_SET_TOKEN)){
                        BotPreferences.setToken(command[2]);
                    }
                }
            }
        }
    }

    private static boolean createFolderIfNotExists(String path) {
        File f = new File(path);
        if (!f.exists()){
            return f.mkdir();
        }
        return true;
    }

}