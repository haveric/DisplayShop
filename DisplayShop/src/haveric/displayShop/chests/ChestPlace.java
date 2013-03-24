package haveric.displayShop.chests;

import haveric.displayShop.DisplayShop;
import haveric.displayShop.db.DB;
import haveric.displayShop.shops.Shop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ChestPlace implements Listener{

    private DisplayShop plugin;
    public ChestPlace(DisplayShop ds) {
        plugin = ds;
    }

    @EventHandler
    public void chestPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.CHEST) {

            // TODO: Find which block is a shop, if any and handle accordingly
            Shop shop = DB.getShop(block.getRelative(BlockFace.NORTH).getLocation());
            if (shop == null) {
                shop = DB.getShop(block.getRelative(BlockFace.EAST).getLocation());

                if (shop == null) {
                    shop = DB.getShop(block.getRelative(BlockFace.SOUTH).getLocation());

                    if (shop == null) {
                        shop = DB.getShop(block.getRelative(BlockFace.WEST).getLocation());
                    }
                }
            }

            if (shop != null) {
                Location location = ChestUtil.getMiddle(block.getLocation(), shop.getLocation());
                shop.setLocation(location);
                DB.update(shop);
                shop.resetItemPosition();
            }
        }
    }
}
