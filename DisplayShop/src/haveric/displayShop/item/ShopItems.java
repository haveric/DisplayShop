package haveric.displayShop.item;

import haveric.displayShop.db.DB;
import haveric.displayShop.shops.Shop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ShopItems {

    private static BiMap<String, Integer> shopItems;
    private static Map<String, Item> entityUUIDs;

    public static void init() {
        shopItems = HashBiMap.create();
        entityUUIDs = new HashMap<String, Item>();
    }

    public static void removeItem(String uuid) {
        if (shopItems.containsKey(uuid)) {
            Item item = entityUUIDs.get(uuid);
            item.remove();
            shopItems.remove(uuid);
            entityUUIDs.remove(uuid);
        }
    }

    public static void addItem(String uuid, Item item, int shopId) {
        shopItems.put(uuid, shopId);
        entityUUIDs.put(uuid, item);
    }

    public static Shop getShop(String uuid) {
        Shop shop = null;
        if (shopItems.containsKey(uuid)) {
            shop = DB.getShop(shopItems.get(uuid));
        }

        return shop;
    }

    public static Item getItem(int shopId) {
        String uuid = null;
        BiMap<Integer, String> inverse = shopItems.inverse();
        if (inverse.containsKey(shopId)) {
            uuid = inverse.get(shopId);
        }

        Item item = null;
        if (uuid != null) {
            if (entityUUIDs.containsKey(uuid)) {
                item = entityUUIDs.get(uuid);
            }
        }
        return item;
    }

    public static void removeAllItems() {
        Iterator<Entry<String, Item>> iter = entityUUIDs.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, Item> entry = iter.next();
            Item item = entry.getValue();
            item.remove();
        }
    }
}
