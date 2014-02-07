package haveric.displayShop.item;

import haveric.displayShop.DisplayShop;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class ItemUtil {

    private static ChatColor enchantColor = ChatColor.AQUA;
    private static ChatColor defaultColor = ChatColor.WHITE;

    public static boolean isSameItem(ItemStack one, ItemStack two) {
        return isSameItem(one, two, false);
    }

    public static boolean isSameItem(ItemStack one, ItemStack two, boolean negativeDurAllowed) {
        boolean same = false;

        if (one != null && two != null) {
            boolean sameType = one.getType() == two.getType();
            boolean sameDur = one.getDurability() == two.getDurability();
            boolean negativeDur = (one.getDurability() == Short.MAX_VALUE) || (two.getDurability() == Short.MAX_VALUE);

            boolean sameEnchant = false;
            boolean noEnchant = one.getEnchantments() == null && two.getEnchantments() == null;
            if (!noEnchant) {
                sameEnchant = one.getEnchantments().equals(two.getEnchantments());
            }

            boolean sameMeta = false;
            boolean noMeta = one.getItemMeta() == null && two.getItemMeta() == null;

            if (!noMeta) {
                // Handles an empty slot being compared
                if (one.getItemMeta() == null || two.getItemMeta() == null) {
                    sameMeta = false;
                } else {
                    sameMeta = one.getItemMeta().equals(two.getItemMeta());
                }
            }

            if (sameType && (sameDur || (negativeDurAllowed && negativeDur)) && (sameEnchant || noEnchant) && (sameMeta || noMeta)) {
                same = true;
            }
        }
        return same;
    }

    private static boolean isAxe(Material mat) {
        boolean isAxe = false;

        switch(mat) {
            case WOOD_AXE:
            case STONE_AXE:
            case IRON_AXE:
            case GOLD_AXE:
            case DIAMOND_AXE:
                isAxe = true;
                break;
            default:
                break;
        }
        return isAxe;
    }

    private static boolean isHoe(Material mat) {
        boolean isHoe = false;

        switch(mat) {
            case WOOD_HOE:
            case STONE_HOE:
            case IRON_HOE:
            case GOLD_HOE:
            case DIAMOND_HOE:
                isHoe = true;
                break;
            default:
                break;
        }
        return isHoe;
    }

    private static boolean isPickaxe(Material mat) {
        boolean isPickaxe = false;

        switch(mat) {
            case WOOD_PICKAXE:
            case STONE_PICKAXE:
            case IRON_PICKAXE:
            case GOLD_PICKAXE:
            case DIAMOND_PICKAXE:
                isPickaxe = true;
                break;
            default:
                break;
        }
        return isPickaxe;
    }

    private static boolean isShovel(Material mat) {
        boolean isShovel = false;

        switch(mat) {
            case WOOD_SPADE:
            case STONE_SPADE:
            case IRON_SPADE:
            case GOLD_SPADE:
            case DIAMOND_SPADE:
                isShovel = true;
                break;
            default:
                break;
        }
        return isShovel;
    }

    private static boolean isSword(Material mat) {
        boolean isSword = false;

        switch(mat) {
            case WOOD_SWORD:
            case STONE_SWORD:
            case IRON_SWORD:
            case GOLD_SWORD:
            case DIAMOND_SWORD:
                isSword = true;
                break;
            default:
                break;
        }
        return isSword;
    }

    public static boolean isBoots(Material mat) {
        boolean isBoots = false;

        switch(mat) {
            case CHAINMAIL_BOOTS:
            case LEATHER_BOOTS:
            case IRON_BOOTS:
            case GOLD_BOOTS:
            case DIAMOND_BOOTS:
                isBoots = true;
                break;
            default:
                break;
        }
        return isBoots;
    }

    public static boolean isChestplate(Material mat) {
        boolean isChestplate = false;

        switch(mat) {
            case CHAINMAIL_CHESTPLATE:
            case LEATHER_CHESTPLATE:
            case IRON_CHESTPLATE:
            case GOLD_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
                isChestplate = true;
                break;
            default:
                break;
        }
        return isChestplate;
    }

    public static boolean isHelmet(Material mat) {
        boolean isHelmet = false;

        switch(mat) {
            case CHAINMAIL_HELMET:
            case LEATHER_HELMET:
            case IRON_HELMET:
            case GOLD_HELMET:
            case DIAMOND_HELMET:
                isHelmet = true;
                break;
            default:
                break;
        }
        return isHelmet;
    }

    public static boolean isLeggings(Material mat) {
        boolean isLeggings = false;

        switch(mat) {
            case CHAINMAIL_LEGGINGS:
            case LEATHER_LEGGINGS:
            case IRON_LEGGINGS:
            case GOLD_LEGGINGS:
            case DIAMOND_LEGGINGS:
                isLeggings = true;
                break;
            default:
                break;
        }
        return isLeggings;
    }

    public static boolean isTool(Material mat) {
        boolean isTool = false;

        if (isAxe(mat) || isHoe(mat) || isPickaxe(mat) || isShovel(mat)) {
            isTool = true;
        }

        return isTool;
    }

    public static boolean isWeapon(Material mat) {
        boolean isWeapon = false;

        if (isSword(mat) || mat == Material.BOW) {
            isWeapon = true;
        }

        return isWeapon;
    }

    public static boolean isArmor(Material mat) {
        boolean isArmor = false;

        if (isBoots(mat) || isChestplate(mat) || isHelmet(mat) || isLeggings(mat)) {
            isArmor = true;
        }

        return isArmor;
    }

    public static boolean isRepairable(Material mat) {
        boolean repairable = false;

        if (isTool(mat) || isWeapon(mat) || isArmor(mat)) {
            repairable = true;
        } else {
            switch(mat) {
                case FISHING_ROD:
                case FLINT_AND_STEEL:
                case SHEARS:
                    repairable = true;
                    break;

                default:
                    break;
            }
        }
        return repairable;
    }

    public static String getFriendlyName(ItemStack item) {
        String name = "";

        int id = item.getTypeId();
        short dur = item.getDurability();

        switch(id) {
            case 1:
                switch(dur) {
                    case 0: name="Stone"; break;
                    case 1: name="Granite"; break;
                    case 2: name="Polished Granite"; break;
                    case 3: name="Diorite"; break;
                    case 4: name="Polished Diorite"; break;
                    case 5: name="Andesite"; break;
                    case 6: name="Polished Andesite"; break;
                }
                break;
            case 3:
                switch(dur) {
                    case 0: name="Dirt"; break;
                    case 1: name="Grassless Dirt"; break;
                    case 2: name="Podzol"; break;
                }
                break;
            case 5:
                switch(dur) {
                    case 0: name="Oak"; break;
                    case 1: name="Spruce"; break;
                    case 2: name="Birch"; break;
                    case 3: name="Jungle"; break;
                    case 4: name="Acacia"; break;
                    case 5: name="Dark Oak"; break;
                }
                name += " Plank";
                break;
            case 6:
                switch(dur) {
                    case 0: name="Oak"; break;
                    case 1: name="Spruce"; break;
                    case 2: name="Birch"; break;
                    case 3: name="Jungle"; break;
                    case 4: name="Acacia"; break;
                    case 5: name="Dark Oak"; break;
                }
                name += " Sapling";
                break;
            case 12:
                switch(dur) {
                    case 0: name="Sand"; break;
                    case 1: name="Red Sand"; break;
                }
                break;
            case 17:
                switch(dur) {
                    case 0: name="Oak"; break;
                    case 1: name="Spruce"; break;
                    case 2: name="Birch"; break;
                    case 3: name="Jungle"; break;
                }
                name += " Log";
                break;
            case 18:
                switch(dur) {
                    case 12:
                    case 8:
                    case 4:
                    case 0: name="Oak"; break;
                    case 13:
                    case 9:
                    case 5:
                    case 1: name="Pine"; break;
                    case 14:
                    case 10:
                    case 6:
                    case 2: name="Birch"; break;
                    case 15:
                    case 11:
                    case 7:
                    case 3: name="Jungle"; break;
                }
                name += " Leaves";
                break;
            case 24:
                switch(dur) {
                    case 1: name="Chiseled"; break;
                    case 2: name="Smooth"; break;
                }
                name += " SandStone";
                break;
            case 29:
                name="Sticky Piston";
                break;
            case 30:
                name="Cobweb";
                break;
            case 31:
                switch(dur) {
                    case 0: name="Dead Shrub"; break;
                    case 1: name="Tall Grass"; break;
                    case 2: name="Fern"; break;
                }
                break;
            case 33:
                name="Piston";
                break;
            case 35:
                switch(dur) {
                    case 0: name="White"; break;
                    case 1: name="Orange"; break;
                    case 2: name="Magenta"; break;
                    case 3: name="Light Blue"; break;
                    case 4: name="Yellow"; break;
                    case 5: name="Lime"; break;
                    case 6: name="Pink"; break;
                    case 7: name="Gray"; break;
                    case 8: name="Light Gray"; break;
                    case 9: name="Cyan"; break;
                    case 10: name="Purple"; break;
                    case 11: name="Blue"; break;
                    case 12: name="Brown"; break;
                    case 13: name="Green"; break;
                    case 14: name="Red"; break;
                    case 15: name="Black"; break;
                }
                name += " Wool";
                break;
            case 37:
                name="Dandelion";
                break;
            case 38:
                switch(dur) {
                    case 0: name="Poppy"; break;
                    case 1: name="Blue Orchid"; break;
                    case 2: name="Allium"; break;
                    case 3: name="Azure Bluet"; break;
                    case 4: name="Red Tulip"; break;
                    case 5: name="Orange Tulip"; break;
                    case 6: name="White Tulip"; break;
                    case 7: name="Pink Tulip"; break;
                    case 8: name="Oxeye Daisy"; break;
                }
                break;
            case 43:
                name = "Double ";
                switch(dur) {
                    case 0: name+="Stone"; break;
                    case 1: name+="SandStone"; break;
                    case 2: name+="Wood"; break;
                    case 3: name+="Cobble"; break;
                    case 4: name+="Brick"; break;
                    case 5: name+="Stone Brick"; break;
                    case 6: name+="Nether Brick"; break;
                    case 7: name+="Quartz"; break;
                }
                name += " Slab";
                break;
            case 44:
                switch(dur) {
                    case 0: name="Stone"; break;
                    case 1: name="SandStone"; break;
                    case 2: name="Wood"; break;
                    case 3: name="Cobble"; break;
                    case 4: name="Brick"; break;
                    case 5: name="Stone Brick"; break;
                    case 6: name="Nether Brick"; break;
                    case 7: name="Quartz"; break;
                }
                name += " Slab";
                break;
            case 46:
                name="TNT";
                break;
            case 58:
                name="Crafting Table";
                break;
            case 82:
                name="Clay Block";
                break;
            case 95:
                switch(dur) {
                    case 0: name="White"; break;
                    case 1: name="Orange"; break;
                    case 2: name="Magenta"; break;
                    case 3: name="Light Blue"; break;
                    case 4: name="Yellow"; break;
                    case 5: name="Lime"; break;
                    case 6: name="Pink"; break;
                    case 7: name="Gray"; break;
                    case 8: name="Light Gray"; break;
                    case 9: name="Cyan"; break;
                    case 10: name="Purple"; break;
                    case 11: name="Blue"; break;
                    case 12: name="Brown"; break;
                    case 13: name="Green"; break;
                    case 14: name="Red"; break;
                    case 15: name="Black"; break;
                }
                name+=" Stained Glass";
                break;
            case 97:
                name = "Silverfish ";
                switch(dur) {
                    case 0: name+="Stone"; break;
                    case 1: name+="Cobble"; break;
                    case 2: name+="Stone Brick"; break;
                    case 3: name+="Mossy Stone Brick"; break;
                    case 4: name+="Cracked Stone Brick"; break;
                    case 5: name+="Chiseled Stone Brick"; break;
                }
                break;
            case 98:
                switch(dur) {
                    case 0: name="Stone"; break;
                    case 1: name="Mossy Stone"; break;
                    case 2: name="Cracked Stone"; break;
                    case 3: name="Chiseled Stone"; break;
                }
                name += " Brick";
                break;
            case 99:
                name="Huge Brown Mushroom";
                break;
            case 100:
                name="Huge Red Mushroom";
                break;
            case 101:
                name="Iron Bars";
                break;
            case 102:
                name="Glass Panes";
                break;
            case 109:
                name="Stone Brick Stairs";
                break;
            case 110:
                name="Mycelium";
                break;
            case 111:
                name="Lily Pad";
                break;
            case 123:
                name="Redstone Lamp";
                break;
            case 125:
                name = "Double ";
                switch(dur) {
                    case 0: name+="Oak"; break;
                    case 1: name+="Spruce"; break;
                    case 2: name+="Birch"; break;
                    case 3: name+="Jungle"; break;
                    case 4: name+="Acacia"; break;
                    case 5: name+="Dark Oak"; break;
                }
                name += " Slab";
                break;
            case 126:
                switch(dur) {
                    case 0: name="Oak"; break;
                    case 1: name="Pine"; break;
                    case 2: name="Birch"; break;
                    case 3: name="Jungle"; break;
                    case 4: name="Acacia"; break;
                    case 5: name="Dark Oak"; break;
                }
                name += " Slab";
                break;
            case 139:
                switch(dur) {
                    case 0: name="Cobble Wall"; break;
                    case 1: name="Mossy wall"; break;
                }
                break;
            case 155:
                switch(dur) {
                    case 0: name=""; break;
                    case 1: name="Chiseled"; break;
                    case 2: name="Pillar"; break;
                }
                name += " Quartz Block";
                break;
            case 159:
                switch(dur) {
                    case 0: name="White"; break;
                    case 1: name="Orange"; break;
                    case 2: name="Magenta"; break;
                    case 3: name="Light Blue"; break;
                    case 4: name="Yellow"; break;
                    case 5: name="Lime"; break;
                    case 6: name="Pink"; break;
                    case 7: name="Gray"; break;
                    case 8: name="Light Gray"; break;
                    case 9: name="Cyan"; break;
                    case 10: name="Purple"; break;
                    case 11: name="Blue"; break;
                    case 12: name="Brown"; break;
                    case 13: name="Green"; break;
                    case 14: name="Red"; break;
                    case 15: name="Black"; break;
                }
                name += " Stained Clay";
                break;
            case 160:
                switch(dur) {
                    case 0: name="White"; break;
                    case 1: name="Orange"; break;
                    case 2: name="Magenta"; break;
                    case 3: name="Light Blue"; break;
                    case 4: name="Yellow"; break;
                    case 5: name="Lime"; break;
                    case 6: name="Pink"; break;
                    case 7: name="Gray"; break;
                    case 8: name="Light Gray"; break;
                    case 9: name="Cyan"; break;
                    case 10: name="Purple"; break;
                    case 11: name="Blue"; break;
                    case 12: name="Brown"; break;
                    case 13: name="Green"; break;
                    case 14: name="Red"; break;
                    case 15: name="Black"; break;
                }
                name += " Stained Glass Pane";
                break;
            case 161:
                switch(dur) {
                    case 0: name="Acacia"; break;
                    case 1: name="Dark Oak"; break;
                }
                name += " Leaves";
                break;
            case 162:
                switch(dur) {
                    case 0: name="Acacia"; break;
                    case 1: name="Dark Oak"; break;
                }
                name += " Log";
                break;
            case 171:
                switch(dur) {
                    case 0: name="White"; break;
                    case 1: name="Orange"; break;
                    case 2: name="Magenta"; break;
                    case 3: name="Light Blue"; break;
                    case 4: name="Yellow"; break;
                    case 5: name="Lime"; break;
                    case 6: name="Pink"; break;
                    case 7: name="Gray"; break;
                    case 8: name="Light Gray"; break;
                    case 9: name="Cyan"; break;
                    case 10: name="Purple"; break;
                    case 11: name="Blue"; break;
                    case 12: name="Brown"; break;
                    case 13: name="Green"; break;
                    case 14: name="Red"; break;
                    case 15: name="Black"; break;
                }
                name += " Carpet";
                break;
            case 175:
                switch(dur) {
                    case 0: name="Sunflower"; break;
                    case 1: name="Lilac"; break;
                    case 2: name="Double Tallgrass"; break;
                    case 3: name="Large Fern"; break;
                    case 4: name="Rose Bush"; break;
                    case 5: name="Peony"; break;
                }
                break;
            case 256:
                name="Iron Shovel";
                break;
            case 263:
                switch(dur) {
                    case 0: name="Coal"; break;
                    case 1: name="Charcoal"; break;
                }
                break;
            case 269:
                name="Wood Shovel";
                break;
            case 273:
                name="Stone Shovel";
                break;
            case 277:
                name="Diamond Shovel";
                break;
            case 289:
                name="Gunpowder";
                break;
            case 300:
                name="Leather Pants";
                break;
            case 304:
                name="Chainmail Pants";
                break;
            case 308:
                name="Iron Pants";
                break;
            case 312:
                name="Diamond Pants";
                break;
            case 316:
                name="Gold Pants";
                break;
            case 319:
                name="Raw Porkchop";
                break;
            case 322:
                switch(dur) {
                    case 0: name="Golden Apple"; break;
                    case 1: name="Notch Apple"; break;
                }
                break;
            case 347:
                name="Clock";
                break;
            case 349:
                switch(dur) {
                    case 0: name="Raw Fish"; break;
                    case 1: name="Raw Salmon"; break;
                    case 2: name="Clownfish"; break;
                    case 3: name="Pufferfish"; break;
                }
                break;
            case 350:
                switch(dur) {
                    case 0: name="Cooked Fish"; break;
                    case 1: name="Cooked Salmon"; break;
                }
                break;
            case 351:
                switch(dur) {
                    case 0: name="Ink Sack"; break;
                    case 1: name="Red Dye"; break;
                    case 2: name="Green Dye"; break;
                    case 3: name="Cocoa Bean"; break;
                    case 4: name="Lapis Lazuli"; break;
                    case 5: name="Purple Dye"; break;
                    case 6: name="Cyan Dye"; break;
                    case 7: name="Light Gray Dye"; break;
                    case 8: name="Gray Dye"; break;
                    case 9: name="Pink Dye"; break;
                    case 10: name="Lime Dye"; break;
                    case 11: name="Yellow Dye"; break;
                    case 12: name="Light Blue Dye"; break;
                    case 13: name="Magenta Dye"; break;
                    case 14: name="Orange Dye"; break;
                    case 15: name="Bonemeal"; break;
                }
                break;
            case 356:
                name="Repeater";
                break;
            case 360:
                name="Melon slice";
                break;
            case 372:
                name="Nether Wart";
                break;
            case 373:
                // TODO Handle Potions
                Potion potion;
                try {
                    potion = Potion.fromItemStack(item);
                    //potion = Potion.fromDamage(dur);
                } catch (IllegalArgumentException e) {
                    potion = new Potion(PotionType.WATER);
                }

                DisplayShop.staticLog.info("Type: " + potion.getType());
                boolean extended = potion.hasExtendedDuration();
                boolean splash = potion.isSplash();
                if (extended && splash) {
                    name = "ES-";
                } else if (extended) {
                    name = "E-";
                } else if (splash) {
                    name = "S-";
                }

                int time = 0;
                boolean canhaveSecondLevel = true;

                int potionId = potion.getNameId();
                switch(potionId) {
                    case 0: name += "Water Bottle"; break;
                    case 1: name += "Regen"; time = 45; break;
                    case 2: name += "Swiftness"; time = 180; break;
                    case 3: name += "Fire Resistance"; time = 180; canhaveSecondLevel = false; break;
                    case 4: name += "Poison"; time = 45; break;
                    case 5: name += "Healing"; break;
                    case 6: name += "Night Vision"; time += 180; break;
                    case 7: name += "Clear"; break;
                    case 8: name += "Weakness"; time = 90; canhaveSecondLevel = false; break;
                    case 9: name += "Strength"; time = 180; break;
                    case 10: name += "Slowness"; time = 90; canhaveSecondLevel = false; break;
                    case 11: name += "Diffuse"; break;
                    case 12: name += "Harming"; break;
                    case 13: name += "Artless"; break;
                    case 14: name += "Invisibility"; time = 180; break;
                    case 15: name += "Thin"; break;
                    case 16: name += "Awkward"; break;
                    case 23: name += "Bungling"; break;
                    case 27: name += "Smooth"; break;
                    case 29: name += "Suave"; break;
                    case 31: name += "Debonair"; break;
                    case 32: name += "Thick"; break;
                    case 39: name += "Charming"; break;
                    case 43: name += "Refined"; break;
                    case 45: name += "Cordial"; break;
                    case 47: name += "Sparkling"; break;
                    case 48: name += "Potent"; break;
                    case 55: name += "Rank"; break;
                    case 59: name += "Acrid"; break;
                    case 61: name += "Gross"; break;
                    case 63: name += "Stinky"; break;
                }

                int level = potion.getLevel();
                if (level > 1 && canhaveSecondLevel) {
                    name += " " + level;
                }

                if (time > 0) {
                    DisplayShop.staticLog.info("BTime: " + time);
                    DisplayShop.staticLog.info("Level: " + level);
                    if (level == 2 && canhaveSecondLevel) {
                        time /= 2;
                    }

                    if (extended) {
                        time *= 8;
                        time /= 3;
                    }

                    if (splash) {
                        time *= 3;
                        time /= 4;
                    }
                    DisplayShop.staticLog.info("ATime: " + time);
                    int minutes = time / 60;
                    int seconds = time % 60;

                    String zero = "";
                    if (seconds < 10) {
                        zero += "0";
                    }
                    name += " " + minutes + ":" + zero + seconds;
                } else {
                    if (potionId > 0) {
                        name += " Potion";
                    }
                }

                break;
            case 383:
                switch(dur) {
                    case 50: name="Creeper"; break;
                    case 51: name="Skeleton"; break;
                    case 52: name="Spider"; break;
                    case 53: name="Giant"; break; // Shouldn't exist
                    case 54: name="Zombie"; break;
                    case 55: name="Slime"; break;
                    case 56: name="Ghast"; break;
                    case 57: name="Zombie Pigman"; break;
                    case 58: name="Enderman"; break;
                    case 59: name="Cave Spider"; break;
                    case 60: name="Silverfish"; break;
                    case 61: name="Blaze"; break;
                    case 62: name="Magma Cube"; break;
                    case 63: name="Ender Dragon"; break; // Shouldn't exist
                    case 64: name="Wither"; break; // Shouldn't exist
                    case 65: name="Bat"; break;
                    case 66: name="Witch"; break;
                    case 90: name="Pig"; break;
                    case 91: name="Sheep"; break;
                    case 92: name="Cow"; break;
                    case 93: name="Chicken"; break;
                    case 94: name="Squid"; break;
                    case 95: name="Wolf"; break;
                    case 96: name="Mooshroom"; break;
                    case 97: name="Snow Golem"; break; // Shouldn't exist
                    case 98: name="Ocelot"; break;
                    case 99: name="Iron Golem"; break; // Shouldn't exist
                    case 100: name="Horse"; break;
                    case 120: name="Villager"; break;
                    case 200: name="Ender Crystal"; break; // Should definitely not exist
                }
                name += " Egg";
                break;
            case 379:
                name="Brewing Stand";
                break;
            case 380:
                name="Cauldron";
                break;
            case 382:
                name="Glistering Melon";
                break;
            case 385:
                name="Fire Charge";
                break;
            case 397:
                switch(dur) {
                    case 0: name="Skeleton"; break;
                    case 1: name="Wither Skeleton"; break;
                    case 2: name="Zombie"; break;
                    case 3: name="Human"; break;
                    case 4: name="Creeper"; break;
                }
                name += " Head";
                break;
            case 2256: name="13 Record"; break;
            case 2257: name="Cat Record"; break;
            case 2258: name="Blocks Record"; break;
            case 2259: name="Chirp Record"; break;
            case 2260: name="Far Record"; break;
            case 2261: name="Mall Record"; break;
            case 2262: name="Mellohi Record"; break;
            case 2263: name="Stal Record"; break;
            case 2264: name="Strad Record"; break;
            case 2265: name="Ward Record"; break;
            case 2266: name="11 Record"; break;
            case 2267: name="Wait Record"; break;

            default: name = toMixed(item.getType().name().replaceAll("_", " "));
        }

        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        if (enchantments != null && enchantments.size() > 0) {
            // Don't care that Potions are Enchanted
            if (id != 373) {
                DisplayShop.staticLog.info("Enchantments: " + enchantments);
                name = "E+" + name;
            }
        }

        return name;
    }

    public static String getFriendlyEnchantments(ItemStack item) {
        String enchants = "";

        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        if (enchantments != null && enchantments.size() > 0) {
            Iterator<Entry<Enchantment, Integer>> iter = enchantments.entrySet().iterator();

            while (iter.hasNext()) {
                Entry<Enchantment, Integer> entry = iter.next();
                int nameId = entry.getKey().getId();
                String name = getFriendlyEnchantName(nameId);
                int level = entry.getValue();

                enchants += " " + enchantColor + name + defaultColor + ":" + level;
            }
        }

        return enchants;
    }

    private static String getFriendlyEnchantName(int enchantId) {
        String name = "";

        switch (enchantId) {
            case 0: name = "Protection"; break;
            case 1: name = "FireProtect"; break;
            case 2: name = "FeatherFall"; break;
            case 3: name = "BlastProtect"; break;
            case 4: name = "ProjectileProtect"; break;
            case 5: name = "Respiration"; break;
            case 6: name = "AquaAffinity"; break;
            case 16: name = "Sharpness"; break;
            case 17: name = "Smite"; break;
            case 18: name = "BaneOfAnthropods"; break;
            case 19: name = "Knockback"; break;
            case 20: name = "FireAspect"; break;
            case 21: name = "Looting"; break;
            case 32: name = "Efficiency"; break;
            case 33: name = "SilkTouch"; break;
            case 34: name = "Unbreaking"; break;
            case 35: name = "Fortune"; break;
            case 48: name = "Power"; break;
            case 49: name = "Punch"; break;
            case 50: name = "Flame"; break;
            case 51: name = "Infinity"; break;
            default:
                name = Enchantment.getById(enchantId).getName();
                break;
        }
        return name;
    }

    // Taken from Jim Yingst at http://www.coderanch.com/t/372746/java/java/method-convert-Mixed-Case
    public static String toMixed(String input) {
        StringBuffer buff = new StringBuffer(input.length());
        boolean isWordStarted = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isLetter(c)) {
                if (isWordStarted) {
                    c = Character.toLowerCase(c);
                } else {
                    c = Character.toUpperCase(c);
                }
                isWordStarted = true;
            } else {
                isWordStarted = false;
            }
            buff.append(c);
        }
        return buff.toString();
    }
}
