package haveric.displayShop.chests;

import haveric.displayShop.db.DB;
import haveric.displayShop.shops.Shop;
import haveric.displayShop.shops.ShopSign;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {

    public EntityExplode() { }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled=true)
    public void entityExplode(EntityExplodeEvent event) {
        List<Block> blocks = event.blockList();

        Iterator<Block> iter = blocks.iterator();
        while(iter.hasNext()) {
            Block block = iter.next();
            Material type = block.getType();

            if (type == Material.CHEST) {
                Shop shop = DB.getShop(block.getLocation());
                if (shop != null) {
                    iter.remove();
                }
            } else if (type == Material.WALL_SIGN) {
                ShopSign sign = DB.getSign(block.getLocation());
                if (sign != null) {
                    iter.remove();
                }
            }
        }

    }
}
