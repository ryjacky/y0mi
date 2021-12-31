package pref;

import java.util.prefs.Preferences;

public class BotPreferences {
    public static final String PREF_PATH = "jlwe80jiombfxmkqeapolfkmr3443";

    private static final Preferences pref = Preferences.userRoot().node(PREF_PATH);

    // in {key, defaultValue} format
    public static final String[] PREFIX = {"prefix", "!"};
    public static String getPrefix(){
        return pref.get(PREFIX[0], PREFIX[1]);
    }
}
