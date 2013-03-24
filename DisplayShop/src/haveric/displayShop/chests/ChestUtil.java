package haveric.displayShop.chests;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class ChestUtil {

    public static boolean isDoubleChest(Block chest) {
        boolean doubleChest = false;
        if (isChest(chest.getRelative(BlockFace.EAST)) || isChest(chest.getRelative(BlockFace.WEST)) || isChest(chest.getRelative(BlockFace.SOUTH))|| isChest(chest.getRelative(BlockFace.NORTH)) ) {
            doubleChest = true;
        }
        return doubleChest;
    }

    public static Block getOtherHalfOfChest(Block chest) {
        Block otherHalf = null;

        if (isChest(chest.getRelative(BlockFace.EAST))) {
            otherHalf = chest.getRelative(BlockFace.EAST);
        } else if (isChest(chest.getRelative(BlockFace.WEST))) {
            otherHalf = chest.getRelative(BlockFace.WEST);
        } else if (isChest(chest.getRelative(BlockFace.SOUTH))) {
            otherHalf = chest.getRelative(BlockFace.SOUTH);
        } else if (isChest(chest.getRelative(BlockFace.NORTH))) {
            otherHalf = chest.getRelative(BlockFace.NORTH);
        }

        return otherHalf;
    }

    private static boolean isChest(Block block) {
        boolean isChest = false;

        if (block != null) {
            isChest = block.getType() == Material.CHEST;
        }

        return isChest;
    }

    public static Location getMiddle(Location one, Location two) {
        Location middle = one.clone();
        middle.setX((one.getX() + two.getX()) / 2);
        middle.setZ((one.getZ() + two.getZ()) / 2);

        return middle;
    }
}
