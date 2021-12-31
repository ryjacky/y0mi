package pref;

import java.util.prefs.Preferences;

public class BotPreferences {
    public static final String PREF_PATH = "jlwe80jiombfxmkqeapolfkmr3443";

    // in {key, defaultValue} format
    public static final String[] PREFIX = {"prefix", "!"};

    public static String getPrefix(){
        return Preferences.userRoot().node(BotPreferences.PREF_PATH).get(BotPreferences.PREFIX[0], BotPreferences.PREFIX[1]);
    }
}
