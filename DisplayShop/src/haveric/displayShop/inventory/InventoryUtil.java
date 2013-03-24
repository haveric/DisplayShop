package haveric.displayShop.inventory;

import haveric.displayShop.DisplayShop;
import haveric.displayShop.item.ItemUtil;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    private static DisplayShop plugin = null;
    public static void init(DisplayShop displayShop) {
        plugin = displayShop;
    }

    public static int getFreeSpaces(Player player, ItemStack itemToCheck) {
        return getFreeSpaces(player, itemToCheck, player.getInventory(), 0, 36);
    }

    public static int getFreeSpaces(Player player, ItemStack itemToCheck, Inventory inventory, int start, int end) {
        int free = 0;

        int inventoryLength = inventory.getContents().length;

        if (start < end && end <= inventoryLength) {
            Material type = itemToCheck.getType();
            short durability = itemToCheck.getDurability();

            int maxAmount = getInventoryMax(player, inventory, type, durability, start, end);

            for (int i = start; i < end; i++) {
                ItemStack slot = inventory.getItem(i);

                if (slot == null) {
                    free += maxAmount;
                } else if (ItemUtil.isSameItem(slot, itemToCheck)) {
                    int freeInSlot = maxAmount - slot.getAmount();
                    if (freeInSlot > 0) {
                        free += freeInSlot;
                    }
                }
            }
        }
        return free;
    }

    public static int getInventoryMax(Player player, Inventory inventory, int matId, short dur) {
        return getInventoryMax(player, inventory, Material.getMaterial(matId), dur);
    }

    public static int getInventoryMax(Player player, Inventory inventory, Material mat, short dur) {
        return getInventoryMax(player, inventory, mat, dur, 0, inventory.getSize());
    }

    private static int getInventoryMax(Player player, Inventory inventory, Material mat, short dur, int start, int end) {
        // TODO: Support for StackableItems
        return mat.getMaxStackSize();
    }


    public static void addItems(Player player, ItemStack itemStack, int toAdd) {
        addItems(player.getInventory(), itemStack, toAdd);
        updateInventory(player);
    }

    public static void addItems(Inventory inventory, ItemStack itemStack, int toAdd) {
        ItemStack add = itemStack.clone();
        add.setAmount(toAdd);
        inventory.addItem(add);
    }

    public static void removeItems(Inventory inventory, ItemStack stack, int toRemove) {
        ItemStack remove = stack.clone();
        remove.setAmount(toRemove);
        inventory.removeItem(remove);
    }

    public static int hasItems(Inventory inventory, ItemStack stack) {
        int items = 0;

        Iterator<ItemStack> iter = inventory.iterator();
        while(iter.hasNext()) {
            ItemStack inventItem = iter.next();
            if (ItemUtil.isSameItem(stack, inventItem)) {
                items += inventItem.getAmount();
            }
        }

        return items;
    }
    public static void updateInventory(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @SuppressWarnings("deprecation")
            @Override public void run() {
                player.updateInventory();
            }
        });
    }
}
