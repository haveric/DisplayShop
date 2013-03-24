package haveric.displayShop.item;

import haveric.displayShop.DisplayShop;
import haveric.displayShop.shops.Shop;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;

public class ItemDespawn implements Listener {

    private DisplayShop plugin;

    // (Integer.MAX_VALUE - 94000) - 6000 ticks
    // It should hit around 100000, so 20000 to catch it should be more than enough
    private int pickupMin = Integer.MAX_VALUE - 110000;
    private int pickupMax = Integer.MAX_VALUE - 90000;
    public ItemDespawn(DisplayShop displayShop) {
        plugin = displayShop;
    }

    @EventHandler
    public void itemDespawn(ItemDespawnEvent event) {
        Item item = event.getEntity();

        int pickupDelay = item.getPickupDelay();
        // Only DisplayShops should be using these numbers so it's our item
        if (pickupDelay > pickupMin && pickupDelay < pickupMax) {
            String entityUUID = item.getUniqueId().toString();
            Shop shop = ShopItems.getShop(entityUUID);

            if (shop != null) {
                shop.refreshItem();
            }
        }
    }
}
