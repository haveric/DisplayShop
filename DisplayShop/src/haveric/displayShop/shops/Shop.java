package haveric.displayShop.shops;

import haveric.displayShop.Econ;
import haveric.displayShop.chests.ChestUtil;
import haveric.displayShop.db.DB;
import haveric.displayShop.inventory.InventoryUtil;
import haveric.displayShop.item.ItemUtil;
import haveric.displayShop.item.ShopItem;

import java.util.ListIterator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "displayshop_shops")
public class Shop {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int shopId;

    @NotNull
    private String world;

    @NotNull
    private double x;
    @NotNull
    private double y;
    @NotNull
    private double z;

    @NotNull
    private String owner;

    @NotNull
    private double buyPrice;
    @NotNull
    private double sellPrice;

    @NotNull
    private boolean buying;
    @NotNull
    private boolean selling;
    @NotNull
    private boolean unlimited;

    @NotNull
    private int maxBuy = -1;

    public Shop() { }

    public Shop(String newOwner, Location newLocation, ItemStack newStack, boolean doubleChest, Location signLocation, byte data) {
        setOwner(newOwner);
        setLocation(newLocation);
        setBuyPrice(0);
        setSellPrice(0);
        setBuying(false);
        setSelling(false);
        setUnlimited(false);

        DB.add(this);

        ItemStack clone = newStack.clone();
        if (ItemUtil.isRepairable(newStack.getType())) {
            clone.setDurability((short) 0);
        }
        ShopItem item = new ShopItem(this, clone);
        item.dropItem();
        DB.update(item);

        ShopSign sign = new ShopSign(this, signLocation, data, ItemUtil.getFriendlyName(clone));
        DB.add(sign);
    }

    public void setShopId(int newId) {
        shopId = newId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setWorld(String newWorld) {
        world = newWorld;
    }

    public String getWorld() {
        return world;
    }

    public void setX(double newX) {
        x = newX;
    }

    public double getX() {
        return x;
    }

    public void setY(double newY) {
        y = newY;
    }

    public double getY() {
        return y;
    }

    public void setZ(double newZ) {
        z = newZ;
    }

    public double getZ() {
        return z;
    }

    public Location getLocation() {
        World world = Bukkit.getServer().getWorld(getWorld());
        Location location = new Location(world, x, y, z);
        return location;
    }

    public void setLocation(Location newLocation) {
        if (newLocation != null) {
            setWorld(newLocation.getWorld().getName());
            double tempX = newLocation.getX();
            double tempY = newLocation.getY();
            double tempZ = newLocation.getZ();
            setX(tempX);
            setY(tempY);
            setZ(tempZ);

            ShopItem item = DB.getItem(getShopId());
            if (item != null) {
                item.setChestLocation(newLocation);
            }
        }
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String newOwner) {
        if (newOwner != null) {
            owner = newOwner;
        }
    }

    public boolean isOwner(String player) {
        boolean isOwner = false;

        if (player != null) {
            if (player.equals(owner)) {
                isOwner = true;
            }
        }

        return isOwner;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double newPrice) {
        if (newPrice >= 0) {
            buyPrice = newPrice;
            setBuying(true);
        } else {
            buyPrice = -1;
            setBuying(false);
        }
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double newPrice) {
        if (newPrice >= 0) {
            sellPrice = newPrice;
            setSelling(true);
        } else {
            sellPrice = -1;
            setSelling(false);
        }
    }

    public boolean isBuying() {
        return buying;
    }

    public void setBuying(boolean newBuying) {
        buying = newBuying;
    }

    public boolean isSelling() {
        return selling;
    }

    public void setSelling(boolean newSelling) {
        selling = newSelling;
    }

    public boolean isUnlimited() {
        return unlimited;
    }

    public void setUnlimited(boolean newUnlimited) {
        unlimited = newUnlimited;
    }

    public int getMaxBuy() {
        return maxBuy;
    }

    public void setMaxBuy(int newMaxBuy) {
        maxBuy = newMaxBuy;
    }

    public Chest getChest() {
        Chest chest = null;
        Block block = getLocation().getBlock();
        if (block.getType() == Material.CHEST) {
            chest = (Chest) block.getState();
        }
        return chest;
    }

    public int getItemCount() {
        int itemCount = 0;
        ShopItem item = DB.getItem(getShopId());
        if (item != null) {
            ItemStack stack = item.getItemStack();
            Chest chest = getChest();

            if (chest != null) {
                Inventory inventory = chest.getInventory();

                ListIterator<ItemStack> iter = inventory.iterator();
                while (iter.hasNext()) {
                    ItemStack temp = iter.next();
                    if (ItemUtil.isSameItem(stack, temp, false)) {
                        itemCount += temp.getAmount();
                    }
                }
            }
        }
        return itemCount;
    }

    public int getMaxChestCanHold() {
        int maxItems = 0;
        boolean doubleChest = ChestUtil.isDoubleChest(getLocation().getBlock());

        // TODO: Find a smarter way to do this, rather than hardcoding.
        int invSize = 27;
        if (doubleChest) {
            invSize = 54;
        }

        Player player = Bukkit.getServer().getPlayerExact(owner);

        ShopItem item = DB.getItem(getShopId());
        Chest chest = getChest();
        int maxStack = 0;

        if (chest != null) {
            maxStack = InventoryUtil.getInventoryMax(player, chest.getInventory(), item.getItemTypeId(), (short)item.getItemData());
        }

        maxItems = invSize * maxStack;
        return maxItems;
    }

    public void refreshItem() {
        ShopItem item = DB.getItem(getShopId());
        item.resetItemPosition();
    }

    public void resetItemPosition() {
        ShopItem item = DB.getItem(getShopId());
        item.setChestLocation(getLocation());
        DB.update(item);
        item.resetItemPosition();
    }

    public void playerPurchase(Player player, int amount) {
        int available = getItemCount();

        if (available == 0) {
            player.sendMessage("The shop is empty. Come back when it has more.");
        } else {
            String playerName = player.getName();

            int canAfford = Econ.canAfford(playerName, getSellPrice());

            int actualBuy = 0;
            if (amount <= available) {
                if (canAfford >= amount) {
                    actualBuy = amount;
                } else {
                    actualBuy = canAfford;
                }
            } else {
                if (canAfford >= available) {
                    actualBuy = available;
                } else {
                    actualBuy = canAfford;
                }
            }

            ShopItem item = DB.getItem(getShopId());
            ItemStack itemStack = item.getItemStack();
            int playerSpace = InventoryUtil.getFreeSpaces(player, itemStack);

            if (playerSpace < actualBuy) {
                actualBuy = playerSpace;
            }
            if (actualBuy > 0) {
                double totalPrice = actualBuy * getSellPrice();
                String buyMessage = " bought " + actualBuy + " " + ItemUtil.getFriendlyName(itemStack) + " for " + totalPrice;
                player.sendMessage("You" + buyMessage + " from " + owner);
                Player ownerPlayer = Bukkit.getPlayerExact(owner);
                if (ownerPlayer.isOnline()) {
                    ownerPlayer.sendMessage(player.getName() + buyMessage);
                }

                Econ.pay(playerName, owner, totalPrice);
                InventoryUtil.removeItems(getChest().getInventory(), itemStack, actualBuy);
                InventoryUtil.addItems(player, itemStack, actualBuy);

                item.updateItemAmount();
                DB.update(item);
            }
        }
    }

    public void playerSell(Player player, int amount) {
        int current = getItemCount();
        int total = getMaxChestCanHold();
        int max = getMaxBuy();

        int canSell = 0;
        if (current == total) {
            player.sendMessage("The shop is full. Come back when it is emptied");
        } else if (max > -1 && current >= max) {
            player.sendMessage("The shop is not buying any more items");
        } else {
            if (max > -1) {
                canSell = max - current;
            } else {
                canSell = total - current;
            }

            double minMoney = 0; //PlayerSettings.getSettings(owner).getMinMoney();
            int canAfford = Econ.canAfford(owner, getBuyPrice(), minMoney);
            int actualSell = 0;

            if (amount <= canSell) {
                if (canAfford >= amount) {
                    actualSell = amount;
                } else {
                    actualSell = canAfford;
                }
            } else {
                if (canAfford >= canSell) {
                    actualSell = canSell;
                } else {
                    actualSell = canAfford;
                }
            }

            double totalPrice = actualSell * getBuyPrice();

            ShopItem item = DB.getItem(getShopId());
            ItemStack itemStack = item.getItemStack();

            int playerHas = InventoryUtil.hasItems(player.getInventory(), itemStack);
            if (playerHas < actualSell) {
                actualSell = playerHas;
            }

            if (actualSell > 0) {
                String sellMessage =  " sold " + actualSell + " " + ItemUtil.getFriendlyName(itemStack) + " for " + totalPrice;

                player.sendMessage("You" + sellMessage + " to " + owner);

                Player ownerPlayer = Bukkit.getPlayerExact(owner);

                if (ownerPlayer.isOnline()) {
                    ownerPlayer.sendMessage(player.getName() + sellMessage);
                }
                Econ.pay(owner, player.getName(), totalPrice);
                InventoryUtil.addItems(getChest().getInventory(), itemStack, actualSell);
                InventoryUtil.removeItems(player.getInventory(), itemStack, actualSell);

                item.updateItemAmount();
                DB.update(item);
            }
        }
    }

    public void cleanupBeforeDestroy() {
        ShopItem item = DB.getItem(getShopId());
        if (item != null) {
            item.removeItem();

        }
        ShopSign sign = DB.getSign(getShopId());
        if (sign != null) {
            sign.destroySign();
        }
    }
}
