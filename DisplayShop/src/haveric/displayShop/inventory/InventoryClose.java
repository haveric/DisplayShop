package haveric.displayShop.inventory;

import haveric.displayShop.DisplayShop;
import haveric.displayShop.db.DB;
import haveric.displayShop.item.ShopItem;
import haveric.displayShop.shops.Shop;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClose implements Listener {

    private DisplayShop plugin = null;
    public InventoryClose(DisplayShop displayShop) {
        plugin = displayShop;
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof Chest) {
            Chest chest = (Chest) holder;

            Location location = chest.getLocation();

            Shop shop = DB.getShop(location);
            if (shop != null) {
                ShopItem item = DB.getItem(shop.getShopId());

                if (item != null) {
                    item.updateItemAmount();
                    DB.update(item);
                }
            }
        } else if (holder instanceof DoubleChest) {
            DoubleChest chest = (DoubleChest) holder;

            Location location = chest.getLocation();

            Shop shop = DB.getShop(location);
            if (shop != null) {
                ShopItem item = DB.getItem(shop.getShopId());

                if (item != null) {
                    item.updateItemAmount();
                    DB.update(item);
                }
            }
        }
    }
}
