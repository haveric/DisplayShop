package haveric.displayShop.shops;

import haveric.displayShop.db.DB;

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
import org.bukkit.block.Sign;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "displayshop_shopsign")
public class ShopSign {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int signId;

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
    private String itemName;

    public ShopSign() { }

    public ShopSign(Shop newShop, Location newLocation, byte data, String newItemName) {
        setShopId(newShop.getShopId());
        setLocation(newLocation);
        setItemName(newItemName);

        createSign(data);
        updateSign();
    }

    public void setSignId(int newId) {
        signId = newId;
    }

    public int getSignId() {
        return signId;
    }
    public void setShopId(int newId) {
        shopId = newId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setItemName(String newName) {
        itemName = newName;
    }

    public String getItemName() {
        return itemName;
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
            world = newLocation.getWorld().getName();
            double newX = newLocation.getX();
            double newY = newLocation.getY();
            double newZ = newLocation.getZ();
            setX(newX);
            setY(newY);
            setZ(newZ);
        }
    }

    public void updateSign() {
        Block block = getLocation().getBlock();
        Sign sign = (Sign) block.getState();
        String[] lines = sign.getLines();

        lines[0] = "";
        lines[1] = getItemName();
        lines[2] = "";
        lines[3] = "";

        Shop shop = DB.getShop(getShopId());
        if (shop.isBuying() && shop.isSelling()) {
            lines[0] = "Buying/Selling:";

            lines[2] = "B: $" + shop.getBuyPrice();
            lines[3] = "S: $" + shop.getSellPrice();
        } else if (shop.isBuying()) {
            lines[0] = "Buying:";
            lines[2] = "$" + shop.getBuyPrice();
        } else if (shop.isSelling()) {
            lines[0] = "Selling:";
            lines[2] = "$" + shop.getSellPrice();
        }

        if (lines[2].endsWith(".0")) {
            lines[2] = lines[2].replace(".0", "");
        }
        if (lines[3].endsWith(".0")) {
            lines[3] = lines[3].replace(".0", "");
        }
        sign.setLine(0, lines[0]);
        sign.setLine(1, lines[1]);
        sign.setLine(2, lines[2]);
        sign.setLine(3, lines[3]);

        sign.update(true);
    }

    public void createSign(byte data) {
        Block block = getLocation().getBlock();
        block.setType(Material.WALL_SIGN);
        block.setData(data);
    }

    public void destroySign() {
        Block block = getLocation().getBlock();
        block.setType(Material.AIR);
    }
}
