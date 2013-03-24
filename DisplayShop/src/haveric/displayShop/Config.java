package haveric.displayShop;

public class Config {

    private static DisplayShop plugin;
    public static void init(DisplayShop displayShop) {
        plugin = displayShop;
    }
    public static void setup() {


    }

    public static DisplayShop getPlugin() {
        return plugin;
    }

}
