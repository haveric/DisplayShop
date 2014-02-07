package haveric.displayShop.item;

import haveric.displayShop.DisplayShop;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDespawn implements Listener {

    private DisplayShop plugin;

    public ItemDespawn(DisplayShop displayShop) {
        plugin = displayShop;
    }

    @EventHandler
    public void itemDespawn(ItemDespawnEvent event) {
        Item item = event.getEntity();

        ItemStack stack = item.getItemStack();

        String itemName = stack.getItemMeta().getDisplayName();
        if (itemName != null && itemName.equals("DisplayShop Item")) {
            item.setPickupDelay(Integer.MAX_VALUE);
            event.setCancelled(true);
        }
    }

}
