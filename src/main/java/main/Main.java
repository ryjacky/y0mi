package main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.File;

public class Main {
    public static final String TOKEN = "ODY3NDM2NjA4OTg1NDk3NjAw.YPhFSw.loafVGjIMX3HxYUqgD_Zm8cTx9s";

    public static void main(String[] args) {
        createFolderIfNotExists(FilePaths.CACHE_PATH);

        try {
            JDA jda = JDABuilder.createDefault(TOKEN).build();
            jda.addEventListener(new BotEventHandler());
        } catch (LoginException e) {
            e.printStackTrace();
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