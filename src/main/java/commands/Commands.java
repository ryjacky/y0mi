package commands;

import pref.BotPreferences;

public class Commands {
    public static final String READ = "read";
    public static final String HELP = "help";

    public static boolean isCommand(String msg){
        if (msg.matches("::[a-zA-Z]+::"))
            return false;

        return msg.matches(BotPreferences.getPrefix()
                + "[a-zA-Z]+");
    }
}
