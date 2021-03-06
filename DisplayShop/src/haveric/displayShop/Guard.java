package haveric.displayShop;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Guard {

    private static WorldGuardPlugin worldGuard = null;

    public static void setWorldGuard(WorldGuardPlugin newWorldGuard) {
        worldGuard = newWorldGuard;

    }

    private static boolean worldGuardEnabled() {
        return (worldGuard != null);
    }

    public static boolean canPlace(Player player, Location location) {
        boolean canPlace = true;

        if (worldGuardEnabled()) {
            canPlace = worldGuard.canBuild(player, location);
        }

        return canPlace;
    }
}
