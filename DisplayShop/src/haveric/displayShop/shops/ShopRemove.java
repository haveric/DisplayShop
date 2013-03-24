package haveric.displayShop.shops;

import haveric.displayShop.DisplayShop;
import haveric.displayShop.chests.ChestUtil;
import haveric.displayShop.db.DB;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ShopRemove implements Listener {

    private DisplayShop plugin = null;
    public ShopRemove(DisplayShop displayShop) {
        plugin = displayShop;
    }

    @EventHandler
    public void destroyShop(BlockBreakEvent event) {
        Block block = event.getBlock();

        Material type = block.getType();

        if (type == Material.CHEST) {
            Location location = block.getLocation();

            if (ChestUtil.isDoubleChest(block)) {
                Block otherHalf = ChestUtil.getOtherHalfOfChest(block);
                Location otherHalfLocation = otherHalf.getLocation();
                location = ChestUtil.getMiddle(location, otherHalfLocation);

                Shop shop = DB.getShop(location);
                if (shop != null) {
                    shop.setLocation(otherHalfLocation);
                    DB.update(shop);
                    shop.resetItemPosition();
                }
            } else {
                Shop shop = DB.getShop(location);
                if (shop != null) {
                    shop.cleanupBeforeDestroy();
                    DB.removeShop(shop);
                }
            }


        } else if (type == Material.WALL_SIGN) {
            Location location = block.getLocation();
            ShopSign sign = DB.getSign(location);
            if (sign != null) {
                int shopId = sign.getShopId();
                Shop shop = DB.getShop(shopId);
                String owner = shop.getOwner();
                if (event.getPlayer().getName().equals(owner)) {
                    shop.cleanupBeforeDestroy();
                    DB.removeShop(shop);
                } else {
                    event.setCancelled(true);
                    sign.updateSign();
                }
            }
        }
    }
}
