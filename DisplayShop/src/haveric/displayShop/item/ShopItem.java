package haveric.displayShop.item;

import haveric.displayShop.db.DB;
import haveric.displayShop.shops.Shop;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "displayshop_shopitem")
public class ShopItem {

    @NotNull
    private int itemTypeId;
    @NotNull
    private int itemData;
    private String itemEnchants;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int itemId;

    private int shopId;

    @NotNull
    private String world;
    @NotNull
    private String xyz;
    @NotNull
    private String displayXYZ;

    public ShopItem() { }

    public ShopItem(Shop newShop, ItemStack itemStack) {
        setShopId(newShop.getShopId());
        setChestLocation(newShop.getLocation());
        setItemStack(itemStack);
        DB.add(this);
    }

    public void setWorld(String newWorld) {
        world = newWorld;
    }

    public String getWorld() {
        return world;
    }

    public void setXyz(String newXYZ) {
        xyz = newXYZ;
    }

    public String getXyz() {
        return xyz;
    }

    public Location getChestLocation() {
        String[] coords = getXyz().split(",");
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        double z = Double.parseDouble(coords[2]);

        World world = Bukkit.getServer().getWorld(getWorld());
        Location location = new Location(world, x, y, z);
        return location;
    }

    public void setChestLocation(Location newLocation) {
        if (newLocation != null) {
            setWorld(newLocation.getWorld().getName());
            double x = newLocation.getX();
            double y = newLocation.getY();
            double z = newLocation.getZ();
            setXyz(x + "," + y + "," + z);

            resetDisplayLocation();
        }
    }

    public void setDisplayXYZ(String newXYZ) {
        displayXYZ = newXYZ;
    }

    public String getDisplayXYZ() {
        return displayXYZ;
    }

    public Location getDisplayLocation() {
        String[] coords = getDisplayXYZ().split(",");
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        double z = Double.parseDouble(coords[2]);

        World world = Bukkit.getServer().getWorld(getWorld());
        Location location = new Location(world, x, y, z);
        return location;
    }

    public void setDisplayLocation(Location newLocation) {
        if (newLocation != null) {
            double x = newLocation.getX();
            double y = newLocation.getY();
            double z = newLocation.getZ();
            setDisplayXYZ(x + "," + y + "," + z);
        }
    }


    public void resetDisplayLocation() {
        Location displayLocation = getChestLocation().clone();
        displayLocation.add(new Vector(0.5, 1.3, 0.5));
        setDisplayLocation(displayLocation);
    }

    public void setItemId(int newId) {
        itemId = newId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setShopId(int newId) {
        shopId = newId;
    }
    public int getShopId() {
        return shopId;
    }

    public void setItemStack(ItemStack newItemStack) {
        if (newItemStack != null) {
            setItemTypeId(newItemStack.getType().getId());
            setItemData(newItemStack.getDurability());
            setItemEnchants(newItemStack.getEnchantments());
        }
    }

    public void setItemTypeId(int newId) {
        itemTypeId = newId;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemData(int newData) {
        itemData = newData;
    }

    public int getItemData() {
        return itemData;
    }

    public void setItemEnchants(Map<Enchantment, Integer> newEnchants) {
        String enchants = "";
        Iterator<Entry<Enchantment, Integer>> iter = newEnchants.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Enchantment, Integer> entry = iter.next();
            int enchantId  = entry.getKey().getId();
            int level = entry.getValue();
            enchants += enchantId + ":" + level + "|";
        }
        setItemEnchants(enchants);
    }

    public void setItemEnchants(String newItemEnchants) {
        itemEnchants = newItemEnchants;
    }

    public String getItemEnchants() {
        return itemEnchants;
    }

    public ItemStack getItemStack() {
        ItemStack stack = new ItemStack(itemTypeId);
        stack.setDurability((short)itemData);
        String enchantString = getItemEnchants();

        if (!enchantString.isEmpty()) {
            String[] enchantList = enchantString.split("\\|");

            for (String enchantGroup : enchantList) {
                String[] enchant = enchantGroup.split(":");
                if (enchant.length == 2) {
                    Enchantment e = Enchantment.getById(Integer.parseInt(enchant[0]));
                    int level = Integer.parseInt(enchant[1]);
                    stack.addUnsafeEnchantment(e, level);
                }
            }
        }
        return stack;
    }

    public void dropItem() {
        final int itemAmount = getItemAmount();

        if (itemAmount > 0) {
            ItemStack fakeItem = getItemStack().clone();
            fakeItem.setAmount(itemAmount);
            ItemMeta meta = fakeItem.getItemMeta();
            meta.setDisplayName("DisplayShop Item");
            fakeItem.setItemMeta(meta);

            Item item = getChestLocation().getWorld().dropItem(getDisplayLocation(), fakeItem);
            item.setVelocity(new Vector(0, 0.25, 0));
            item.setPickupDelay(Integer.MAX_VALUE);
            ShopItems.addItem(item.getUniqueId().toString(), item, shopId);
        }

    }

    public void resetItemPosition() {
        removeItem();
        dropItem();
    }

    public int getItemAmount() {
        int itemAmount = 0;
        Shop shop = DB.getShop(shopId);
        if (shop != null) {
            int current = shop.getItemCount();
            int total = shop.getMaxChestCanHold();

            double percent = (double) current/total;

            if (current == 0) {
                itemAmount = 0;
            } else if (current > 0 && percent < .33) {
                itemAmount = 1;
            } else if (percent >= .33 && percent < .66) {
                itemAmount = 2; // stack of 2
            } else if (current < total) {
                itemAmount = 6; // stack of 3
            } else if (current >= total) {
                itemAmount = 21; // stack of 4
            }
        }
        return itemAmount;
    }


    public void updateItemAmount() {
        int itemAmount = getItemAmount();
        Item item = ShopItems.getItem(shopId);
        if (item != null) {
            removeItem();
        }

        if (itemAmount > 0) {
            dropItem();
        }
    }

    public void removeItem() {
        Item item = ShopItems.getItem(shopId);
        if (item != null) {
            ShopItems.removeItem(item.getUniqueId().toString());
            item.remove();
        }
    }

}
