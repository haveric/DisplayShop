package haveric.displayShop.db;

import haveric.displayShop.DisplayShop;
import haveric.displayShop.item.ItemUtil;
import haveric.displayShop.item.ShopItem;
import haveric.displayShop.shops.Shop;
import haveric.displayShop.shops.ShopSign;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.avaje.ebean.EbeanServer;

public class DB {

    static DisplayShop plugin = null;
    static EbeanServer database = null;

    public static long addObject;
    public static long updateObject;
    public static long removeShop;
    public static long getShop;
    public static long search;
    public static long getShopById;
    public static long getShops;
    public static long getShopItems;
    public static long isAShop;
    public static long getShopFromEntity;
    public static long getShopFromEntityString;
    public static long getItem;
    public static long getSign;
    public static long getSignById;
    public static long setShopBuySell;
    public static long getItemsByChunk;
    private DB() {} // Private constructor for utility class

    public static void init(DisplayShop displayShop) {
        plugin = displayShop;

        database = plugin.getDatabase();

        addObject = 0;
        updateObject = 0;
        removeShop = 0;
        getShop = 0;
        search = 0;
        getShopById = 0;
        getShops = 0;
        getShopItems = 0;
        isAShop = 0;
        getShopFromEntity = 0;
        getShopFromEntityString = 0;
        getItem = 0;
        getSign = 0;
        getSignById = 0;
        setShopBuySell = 0;
        getItemsByChunk = 0;
    }

    public static void add(Object object) {
        addObject ++;
        database.save(object);
    }

    public static void update(final Object object) {
        updateObject ++;
        database.update(object);
    }

    public static void removeShop(Shop shop) {
        removeShop ++;
        ShopItem item = getItem(shop.getShopId());
        if (item != null) {
            database.delete(item);
        }

        ShopSign sign = getSign(shop.getShopId());
        if (sign != null) {
            database.delete(sign);
        }

        database.delete(shop);
    }

    public static Shop getShop(Location location) {
        getShop ++;
        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return database.find(Shop.class).where().ieq("world", world).eq("x", x).eq("y", y).eq("z", z).findUnique();
    }

    public static List<ShopItem> getItemsByChunk(Chunk chunk) {
        getItemsByChunk ++;
        String world = chunk.getWorld().getName();
        double chunkSize = 16.0;
        double x = chunk.getX() * chunkSize;
        double z = chunk.getZ() * chunkSize;


        List<ShopItem> items = database.find(ShopItem.class).where().ieq("world", world).between("x", x, x+chunkSize).between("z", z, z+chunkSize).findList();
        return items;
    }

    public static List<Shop> searchShopItemDistance(Location location, int distance, ItemStack stack) {
        search ++;
        String world = location.getWorld().getName();
        List<Shop> shops = database.find(Shop.class).where().ieq("world", world).findList();

        Iterator<Shop> iter = shops.iterator();

        List<Shop> shopsInRange = new ArrayList<Shop>();
        while(iter.hasNext()) {
            Shop shop = iter.next();
            Location shopLocation = shop.getLocation();
            if (location.distance(shopLocation) <= distance) {
                ShopItem item = getItem(shop.getShopId());
                if (ItemUtil.isSameItem(stack, item.getItemStack())) {
                    shopsInRange.add(shop);
                }
            }
        }
        return shops;
    }

    public static Shop getShop(int shopId) {
        getShopById ++;
        return database.find(Shop.class).where().eq("shopId", shopId).findUnique();
    }

    public static List<Shop> getShops() {
        getShops ++;
        return database.find(Shop.class).findList();
    }
    public static List<ShopItem> getShopItems() {
        getShopItems ++;
        return database.find(ShopItem.class).findList();
    }

    public static boolean isAShop(int shopId) {
        isAShop ++;
        return (getShop(shopId) != null);
    }

    public static Shop getShopFromEntityUUID(String entityUUID) {
        getShopFromEntity ++;
        Shop shop = null;
        ShopItem item = getShopItemFromUUIDString(entityUUID);
        if (item != null) {
            shop = getShop(item.getShopId());
        }
        return shop;
    }
    public static ShopItem getShopItemFromUUIDString(String entityUUID) {
        getShopFromEntityString ++;
        return database.find(ShopItem.class).where().ieq("entityUUIDString", entityUUID).findUnique();
    }

    public static ShopItem getItem(int shopId) {
        getItem ++;
        return database.find(ShopItem.class).where().eq("shopId", shopId).findUnique();
    }

    public static ShopSign getSign(Location location) {
        getSign ++;
        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        return database.find(ShopSign.class).where().ieq("world", world).eq("x", x).eq("y", y).eq("z", z).findUnique();
    }

    public static ShopSign getSign(int shopId) {
        getSignById ++;
        return database.find(ShopSign.class).where().eq("shopId", shopId).findUnique();
    }

    public static void setShopBuySell(int shopId, boolean isBuying, boolean isSelling) {
        setShopBuySell ++;
        Shop shop = getShop(shopId);
        shop.setBuying(isBuying);
        shop.setSelling(isSelling);
    }
}
