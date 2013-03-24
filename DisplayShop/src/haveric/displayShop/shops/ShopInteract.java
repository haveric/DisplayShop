package haveric.displayShop.shops;

import haveric.displayShop.Commands;
import haveric.displayShop.DisplayShop;
import haveric.displayShop.Guard;
import haveric.displayShop.chests.ChestUtil;
import haveric.displayShop.db.DB;
import haveric.displayShop.item.ItemUtil;
import haveric.displayShop.item.ShopItem;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ShopInteract implements Listener {

    private String accentColor = "" + ChatColor.YELLOW;
    private String defaultColor = "" + ChatColor.WHITE;
    private String titleColor = "" + ChatColor.DARK_GREEN;
    private String errorColor = "" + ChatColor.RED;

    private DisplayShop plugin = null;
    public ShopInteract(DisplayShop displayShop) {
        plugin = displayShop;
    }

    @EventHandler
    public void playerShopInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.CREATIVE) {

            // Handles creating as well as buying
            if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
                Block block = event.getClickedBlock();
                Material blockType = block.getType();
                if (blockType == Material.CHEST) {
                    Location blockLocation = block.getLocation();
                    Location location = blockLocation;

                    String playerName = player.getName();

                    boolean isDoubleChest = false;
                    if (ChestUtil.isDoubleChest(block)) {
                        Block otherHalf = ChestUtil.getOtherHalfOfChest(block);
                        Location otherHalfLocation = otherHalf.getLocation();
                        location = ChestUtil.getMiddle(location, otherHalfLocation);
                        isDoubleChest = true;
                    }

                    // Chest is already a shop
                    Shop shop = DB.getShop(location);
                    if (shop != null) {
                        String owner = shop.getOwner();

                        // Player owns the shop
                        if (playerName.equals(owner)) {
                            if (action == Action.LEFT_CLICK_BLOCK) {
                                sendShopInfo(player, shop);
                                Commands.addShopEditWaiting(playerName, shop.getShopId());
                                player.sendMessage("Type " + accentColor + "/shop options " + defaultColor + "to see your options as the shop owner.");
                            }
                        // Buy or Sell
                        } else {
                            sendShopInfo(player, shop);
                            Commands.addBuySellWaiting(playerName, shop.getShopId());
                            if (shop.isBuying() && shop.isSelling()) {
                                player.sendMessage("Type " + accentColor + "/buy [#]" + defaultColor + " to buy from the shop or " + accentColor + "/sell [#]" + defaultColor + " to sell to the shop.");
                            } else if (shop.isBuying()) {
                                player.sendMessage("Type " + accentColor + "/sell [#]" + defaultColor + " to sell to the shop.");
                            } else if (shop.isSelling()) {
                                player.sendMessage("Type " + accentColor + "/buy [#]" + defaultColor + " to buy from the shop.");
                            }

                            event.setCancelled(true);
                        }
                    // Create a shop out of this chest
                    } else {
                        if (action == Action.LEFT_CLICK_BLOCK) {
                            if (Guard.canPlace(player, blockLocation)) {
                                ItemStack holding = event.getItem();

                                if (holding != null) {
                                    byte data = block.getData();
                                    Block sign = null;
                                    if (data == 2) {
                                        sign = block.getRelative(BlockFace.NORTH);
                                    } else if (data == 3) {
                                        sign = block.getRelative(BlockFace.SOUTH);
                                    } else if (data == 4) {
                                        sign = block.getRelative(BlockFace.WEST);
                                    } else if (data == 5) {
                                        sign = block.getRelative(BlockFace.EAST);
                                    }
                                    if (sign != null) {
                                        if (sign.getType() == Material.AIR) {
                                            player.sendMessage("Type " + accentColor + "/sell [#]" + defaultColor + ", " + accentColor + "/buy [#]" + defaultColor + ", or " + accentColor + "/both [sell#] [buy#]" + defaultColor + " where [#] is the price per item.");
                                            Location signLocation = sign.getLocation();
                                            Shop newShop = new Shop(playerName, location, holding, isDoubleChest, signLocation, data);
                                            Commands.addSetupWaiting(playerName, newShop.getShopId());
                                        } else {
                                            player.sendMessage("You need to remove the " + accentColor + ItemUtil.getFriendlyName(new ItemStack(sign.getType())) + defaultColor + " in front of this chest.");
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage("You do not have permission to create a shop at this location.");
                            }
                        }
                    }
                } else if (blockType == Material.WALL_SIGN) {
                    Location blockLocation = block.getLocation();
                    Location location = blockLocation;
                    final ShopSign sign = DB.getSign(location);

                    if (sign != null) {
                        int shopId = sign.getShopId();
                        Shop shop = DB.getShop(shopId);

                        if (shop != null) {
                            String owner = shop.getOwner();

                            String playerName = player.getName();
                            if (action == Action.RIGHT_CLICK_BLOCK) {
                                //event.setUseInteractedBlock(Result.DENY);
                                event.setCancelled(true);
                            }
                            if (playerName.equals(owner)) {
                                sendShopInfo(player, shop);
                                Commands.addShopEditWaiting(playerName, shop.getShopId());
                                player.sendMessage("Type " + accentColor + "/shop options " + defaultColor + "to see your options as the shop owner.");
                            } else {
                                sendShopInfo(player, shop);
                                Commands.addBuySellWaiting(playerName, shop.getShopId());
                                if (shop.isBuying() && shop.isSelling()) {
                                    player.sendMessage("Type " + accentColor + "/buy [#]" + defaultColor + " to buy from the shop or " + accentColor + "/sell [#]" + defaultColor + " to sell to the shop.");
                                } else if (shop.isBuying()) {
                                    player.sendMessage("Type " + accentColor + "/sell [#]" + defaultColor + " to sell to the shop.");
                                } else if (shop.isSelling()) {
                                    player.sendMessage("Type " + accentColor + "/buy [#]" + defaultColor + " to buy from the shop.");
                                }
                            }


                        }
                    }
                }
            }
        }
    }


    public void sendShopInfo(Player player, Shop shop) {
        int itemCount = shop.getItemCount();
        int maxChestHolds = shop.getMaxChestCanHold();
        int maxBuy = shop.getMaxBuy();

        player.sendMessage("╠══════════╣" + titleColor + " DisplayShop " + defaultColor + "╠═════════════════════════════════════╣");
        ShopItem item = DB.getItem(shop.getShopId());
        ItemStack itemStack = item.getItemStack();
        String friendlyName = ItemUtil.getFriendlyName(itemStack);
        if (friendlyName.startsWith("E+")) {
            friendlyName = friendlyName.replaceFirst("E\\+", "");
            // TODO: Handle displaying Enchantments
            friendlyName += ItemUtil.getFriendlyEnchantments(itemStack);
        }
        if (friendlyName.startsWith("ES-")) {
            friendlyName = friendlyName.replaceFirst("ES-", "Extended Splash ");
        } else if (friendlyName.startsWith("E-")) {
            friendlyName = friendlyName.replaceFirst("E-", "Extended ");
        } else if (friendlyName.startsWith("S-")) {
            friendlyName = friendlyName.replaceFirst("S-", "Splash ");
        }

        if (friendlyName.contains("(")) {
            friendlyName = friendlyName.replaceFirst("\\(", "Potion (");
        }
        player.sendMessage("║ Item: " + friendlyName);

        if (shop.isBuying()) {
            player.sendMessage("║ Shop is buying each for: " + shop.getBuyPrice());
            if (maxBuy > -1 && maxBuy < maxChestHolds) {
                maxChestHolds = maxBuy;
            }
        }
        if (shop.isSelling()) {
            player.sendMessage("║ Shop is selling each for: " + shop.getSellPrice());
        }



        String itemNum = "";
        if (itemCount == 0) {
            itemNum = "║ " + errorColor + "Empty" + defaultColor;
        } else {
            itemNum = "║ Available: " + itemCount + "/" + maxChestHolds;
        }



        player.sendMessage(itemNum);
    }
}
