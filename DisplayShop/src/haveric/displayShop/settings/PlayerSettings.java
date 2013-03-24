package haveric.displayShop.settings;

import java.util.HashMap;
import java.util.Map;

public class PlayerSettings {
    /**
     * String: player name
     **/
    static Map<String, Settings> settings = null;

    public void loadPlayerSettings() {
        settings = new HashMap<String, Settings>();
    }

    public static Settings getSettings(String player) {
        Settings playerSettings = null;
        if (settings != null && player != null) {
            playerSettings = settings.get(player);
        }
        return playerSettings;
    }
}
