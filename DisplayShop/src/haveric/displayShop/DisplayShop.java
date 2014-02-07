package haveric.displayShop;

import haveric.displayShop.chests.ChestPlace;
import haveric.displayShop.chests.EntityExplode;
import haveric.displayShop.db.DB;
import haveric.displayShop.db.Database;
import haveric.displayShop.inventory.InventoryClose;
import haveric.displayShop.inventory.InventoryUtil;
import haveric.displayShop.item.ItemDespawn;
import haveric.displayShop.item.ShopItem;
import haveric.displayShop.item.ShopItems;
import haveric.displayShop.mcstats.Metrics;
import haveric.displayShop.shops.Shop;
import haveric.displayShop.shops.ShopInteract;
import haveric.displayShop.shops.ShopRemove;
import haveric.displayShop.shops.ShopSign;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class DisplayShop extends JavaPlugin {
    public Logger log;

    public static Logger staticLog;
    private Commands commands = new Commands(this);

    Metrics metrics;

    Database db;

    public void onEnable() {
        DisplayShop ds = this;
        log = getLogger();
        staticLog = getLogger();
        PluginManager pm = getServer().getPluginManager();

        // Register this plugin's events
        pm.registerEvents(new ShopInteract(ds), ds);
        pm.registerEvents(new ShopRemove(ds), ds);
        pm.registerEvents(new ItemDespawn(ds), ds);
        pm.registerEvents(new InventoryClose(ds), ds);
        pm.registerEvents(new ChestPlace(ds), ds);
        pm.registerEvents(new EntityExplode(), ds);

        setupDB();
        DB.init(ds);
        ShopItems.init();

        Config.init(ds);
        InventoryUtil.init(ds);
        Econ.init(ds);

        setupVault(pm);
        setupWorldGuard(pm);

        Config.setup();


        getCommand(Commands.getMain()).setExecutor(commands);
        getCommand(Commands.getBuy()).setExecutor(commands);
        getCommand(Commands.getSell()).setExecutor(commands);
        getCommand(Commands.getBoth()).setExecutor(commands);

        setupMetrics();

        List<Shop> shops = DB.getShops();
        Iterator<Shop> shopIter = shops.iterator();
        log.info("Shops to check: " + shops.size());
        while(shopIter.hasNext()) {
            Shop shop = shopIter.next();
            if (shop.getChest() == null) {
                shop.cleanupBeforeDestroy();
                DB.removeShop(shop);
            }
        }


        List<ShopItem> shopItems = DB.getShopItems();
        Iterator<ShopItem> iter = shopItems.iterator();
        log.info("Items to add: " + shopItems.size());
        while(iter.hasNext()) {
            ShopItem item = iter.next();
            item.dropItem();
        }
    }

    public void onDisable() {
        List<ShopItem> shopItems = DB.getShopItems();
        Iterator<ShopItem> iter = shopItems.iterator();
        log.info("Items to remove: " + shopItems.size());
        while(iter.hasNext()) {
            ShopItem item = iter.next();
            item.removeItem();
        }
    }

    private void setupDB() {
        getDataFolder().mkdir();
        File dbFile = new File(getDataFolder() + "/database.yml");
        FileConfiguration dbConfig = YamlConfiguration.loadConfiguration(dbFile);

        saveConfig(dbConfig, dbFile);
        db = new Database(this) {
            protected java.util.List<Class<?>> getDatabaseClasses() {
                List<Class<?>> list = new ArrayList<Class<?>>();
                list.add(Shop.class);
                list.add(ShopItem.class);
                list.add(ShopSign.class);

                return list;
            };
        };

        db.initializeDatabase(
            dbConfig.getString("database.driver"),
            dbConfig.getString("database.url"),
            dbConfig.getString("database.username"),
            dbConfig.getString("database.password"),
            dbConfig.getString("database.isolation"),
            dbConfig.getBoolean("database.logging", false),
            dbConfig.getBoolean("database.rebuild", true)
        );

        dbConfig.set("database.rebuild", false);
        try {
            dbConfig.save(dbFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig(FileConfiguration fileConfig, File file) {
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EbeanServer getDatabase() {
        return db.getDatabase();
    }

    private void setupVault(PluginManager pm) {
        if (pm.getPlugin("Vault") == null) {
            log.info("Vault not found. Disabling DisplayShop.");
            pm.disablePlugin(this);
        } else {
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null) {
                Econ.setEconomy(economyProvider.getProvider());
            }
        }
    }

    private void setupWorldGuard(PluginManager pm) {
        Plugin worldGuard = pm.getPlugin("WorldGuard");
        if (worldGuard == null || !(worldGuard instanceof WorldGuardPlugin)) {
            // No WorldGuard
        } else {
            Guard.setWorldGuard((WorldGuardPlugin) worldGuard);
        }
    }

    private void setupMetrics() {
        try {
            metrics = new Metrics(this);

            metrics.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
