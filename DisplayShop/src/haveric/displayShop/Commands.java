package haveric.displayShop;

import haveric.displayShop.db.DB;
import haveric.displayShop.shops.Shop;
import haveric.displayShop.shops.ShopSign;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private DisplayShop plugin;

    private static String cmdMain = "displayshop";
    private static String cmdMainAlt = "ds";
    private static String cmdMainAlt2 = "dshop";
    private static String cmdMainAlt3 = "shop";
    private static String cmdHelp = "help";

    private static String cmdBuy = "buy";
    private static String cmdSell = "sell";
    private static String cmdBoth = "both";

    private static String cmdSet = "set";
    private static String cmdOptions = "options";
    private static String cmdMaxBuy = "maxbuy";

    private static String cmdDBCalls = "dbcalls";

    private static String repo = "github.com/haveric/DisplayShop";

    ChatColor msgColor = ChatColor.DARK_AQUA;
    ChatColor accentColor = ChatColor.YELLOW;
    ChatColor defaultColor = ChatColor.WHITE;
    ChatColor errorColor = ChatColor.RED;

    private static Map<String, Integer> setupWaiting;
    private static Map<String, Integer> buySellWaiting;
    private static Map<String, Integer> shopEditWaiting;

    private double distanceToSell = 5;

    public Commands(DisplayShop displayShop) {
        plugin = displayShop;

        setupWaiting = new HashMap<String, Integer>();
        buySellWaiting = new HashMap<String, Integer>();
        shopEditWaiting = new HashMap<String, Integer>();
    }
    public static String getMain() {
        return cmdMain;
    }

    public static String getBuy() {
        return cmdBuy;
    }

    public static String getSell() {
        return cmdSell;
    }

    public static String getBoth() {
        return cmdBoth;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        String title = msgColor + "[" + ChatColor.GRAY + plugin.getName() + msgColor + "] ";

        if (commandLabel.equalsIgnoreCase(cmdMain) || commandLabel.equalsIgnoreCase(cmdMainAlt) || commandLabel.equalsIgnoreCase(cmdMainAlt2) || commandLabel.equalsIgnoreCase(cmdMainAlt3)) {
            if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase(cmdHelp))) {
                sender.sendMessage(title + repo + " - v" + plugin.getDescription().getVersion());
            } else if (args.length == 1 && args[0].equalsIgnoreCase(cmdOptions)) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String playerName = player.getName();
                    if (shopEditWaiting.containsKey(playerName)) {
                        sender.sendMessage(title + "OPTIONS");
                        sender.sendMessage(accentColor + "/" + cmdMainAlt3 + " " + cmdSet + " " + cmdBuy + defaultColor + "/" + accentColor + cmdSell + " # " + defaultColor + "- Set buy/sell price. (-1 to disable)");
                        sender.sendMessage(accentColor + "/" + cmdMainAlt3 + " " + cmdSet + " " + cmdMaxBuy + " # " + defaultColor + "- Set max # this shop will buy. (-1 to reset)");
                    }
                }
            } else if (args.length >= 1 && args[0].equalsIgnoreCase(cmdSet)) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String playerName = player.getName();
                    if (shopEditWaiting.containsKey(playerName)) {
                        if (args.length == 1) {
                            // TODO: Provide detailed info about set command
                        } else if (args.length == 2) {
                            // TODO: Provide detailed info about buy/sell/maxBuy command
                        } else if (args.length == 3) {
                            try {
                            double amount = Double.parseDouble(args[2]);
                                int shopId = shopEditWaiting.get(playerName);
                                Shop shop = DB.getShop(shopId);
                                double distance = player.getLocation().distance(shop.getLocation());

                                if (distance <= distanceToSell) {
                                    if (args[1].equals(cmdBuy)) {
                                        shop.setBuyPrice(amount);
                                        DB.update(shop);

                                        ShopSign sign = DB.getSign(shopId);
                                        sign.updateSign();
                                    } else if (args[1].equals(cmdSell)) {
                                        shop.setSellPrice(amount);
                                        DB.update(shop);

                                        ShopSign sign = DB.getSign(shopId);
                                        sign.updateSign();
                                    } else if (args[1].equals(cmdMaxBuy)) {
                                        shop.setMaxBuy((int) amount);
                                        DB.update(shop);
                                    }
                                } else {
                                    player.sendMessage(errorColor + "You are not close enough to the shop to edit it");
                                }
                            } catch (NumberFormatException e) {
                                player.sendMessage(errorColor + "Invalid Number");
                            }
                        }
                    }
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase(cmdDBCalls)) {
                sender.sendMessage("Add Object: " + DB.addObject);
                sender.sendMessage("Update Obj: " + DB.updateObject);
                sender.sendMessage("Rmove Shop: " + DB.removeShop);
                sender.sendMessage("Get Shop  : " + DB.getShop);
                sender.sendMessage("Search    : " + DB.search);
                sender.sendMessage("Get ShopID: " + DB.getShopById);
                sender.sendMessage("Get Shops : " + DB.getShops);
                sender.sendMessage("Get Items : " + DB.getShopItems);
                sender.sendMessage("Is A Shop : " + DB.isAShop);
                sender.sendMessage("By Entity : " + DB.getShopFromEntity);
                sender.sendMessage("Entity Str: " + DB.getShopFromEntityString);
                sender.sendMessage("Get ItemID: " + DB.getItem);
                sender.sendMessage("Get Sign  : " + DB.getSign);
                sender.sendMessage("Get SignID: " + DB.getSignById);
                sender.sendMessage("SetBuySell: " + DB.setShopBuySell);
            }
        } else if (commandLabel.equalsIgnoreCase(cmdBuy)) {
            if (args.length == 0) {
                // Provide usage or something
            } else if (args.length == 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String playerName = player.getName();

                    if (setupWaiting.containsKey(playerName)) {
                        try {
                            double buyPrice = Double.parseDouble(args[0]);
                            int shopId = setupWaiting.get(playerName);
                            Shop shop = DB.getShop(shopId);

                            double distance = player.getLocation().distance(shop.getLocation());

                            if (distance <= distanceToSell) {
                                ShopSign sign = DB.getSign(shopId);
                                shop.setBuying(true);
                                shop.setSelling(false);
                                shop.setBuyPrice(buyPrice);
                                DB.update(shop);
                                sign.updateSign();

                                player.sendMessage("Shop set to buy.");
                                removeSetupWaiting(playerName);
                            } else {
                                player.sendMessage(errorColor + "You are not close enough to setup the shop. Move closer");
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(errorColor + "Not a number." + defaultColor + " Try " + accentColor + "/buy #");
                        }
                    } else if (buySellWaiting.containsKey(playerName)) {
                        try {
                            int numToBuy = Integer.parseInt(args[0]);
                            int shopId = buySellWaiting.get(playerName);
                            Shop shop = DB.getShop(shopId);

                            double distance = player.getLocation().distance(shop.getLocation());

                            if (distance <= distanceToSell) {
                                shop.playerPurchase(player, numToBuy);
                            } else {
                                player.sendMessage(errorColor + "You are not close enough to buy from this shop.");
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(errorColor + "Not a number." + defaultColor + " Try " + accentColor + "/buy #");
                        }
                    }
                }
            }
        } else if (commandLabel.equalsIgnoreCase(cmdSell)) {
            if (args.length == 0) {
                // Provide usage or something
            } else if (args.length == 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String playerName = player.getName();
                    if (setupWaiting.containsKey(playerName)) {
                        try {
                            double sellPrice = Double.parseDouble(args[0]);
                            int shopId = setupWaiting.get(playerName);
                            Shop shop = DB.getShop(shopId);

                            double distance = player.getLocation().distance(shop.getLocation());

                            if (distance <= distanceToSell) {
                                ShopSign sign = DB.getSign(shopId);
                                shop.setBuying(false);
                                shop.setSelling(true);
                                shop.setSellPrice(sellPrice);
                                DB.update(shop);
                                sign.updateSign();

                                player.sendMessage("Shop set to sell.");
                                removeSetupWaiting(playerName);
                            } else {
                                player.sendMessage(errorColor + "You are not close enough to setup the shop. Move closer");
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(errorColor + "Not a number." + defaultColor + " Try " + accentColor + "/sell #");
                        }
                    } else if (buySellWaiting.containsKey(playerName)) {
                        try {
                            int numToSell = Integer.parseInt(args[0]);
                            int shopId = buySellWaiting.get(playerName);
                            Shop shop = DB.getShop(shopId);

                            double distance = player.getLocation().distance(shop.getLocation());

                            if (distance <= distanceToSell) {
                                shop.playerSell(player, numToSell);
                            } else {
                                player.sendMessage(errorColor + "You are not close enough to sell to this shop.");
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(errorColor + "Not a number." + defaultColor + " Try " + accentColor + "/sell #");
                        }
                    }
                }
            }
        } else if (commandLabel.equalsIgnoreCase(cmdBoth)) {
            if (args.length == 0) {
                // Provide usage or something
            } else if (args.length == 2) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String playerName = player.getName();
                    if (setupWaiting.containsKey(playerName)) {
                        try {
                            double sellPrice = Double.parseDouble(args[0]);
                            double buyPrice = Double.parseDouble(args[1]);
                            int shopId = setupWaiting.get(playerName);
                            Shop shop = DB.getShop(shopId);

                            double distance = player.getLocation().distance(shop.getLocation());

                            if (distance <= distanceToSell) {
                                ShopSign sign = DB.getSign(shopId);
                                shop.setBuying(true);
                                shop.setSelling(true);
                                shop.setBuyPrice(buyPrice);
                                shop.setSellPrice(sellPrice);
                                DB.update(shop);
                                sign.updateSign();

                                player.sendMessage("Shop set to both buy and sell.");
                                removeSetupWaiting(playerName);
                            } else {
                                player.sendMessage(errorColor + "You are not close enough to setup the shop. Move closer");
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(errorColor + "Buy or sell price is not a number." + defaultColor + " Try " + accentColor + "/both 10 5");
                        }
                    }
                }
            }
        }

        return false;
    }

    public static void addSetupWaiting(String player, int shopId) {
        clearWaiting(player);
        setupWaiting.put(player, shopId);
    }

    private static void removeSetupWaiting(String player) {
        if (setupWaiting.containsKey(player)) {
            setupWaiting.remove(player);
        }
    }

    public static void addBuySellWaiting(String player, int shopId) {
        clearWaiting(player);
        buySellWaiting.put(player, shopId);
    }

    private static void removeBuySellWaiting(String player) {
        if (buySellWaiting.containsKey(player)) {
            buySellWaiting.remove(player);
        }
    }

    public static void addShopEditWaiting(String player, int shopId) {
        clearWaiting(player);
        shopEditWaiting.put(player, shopId);
    }

    private static void removeShopEditWaiting(String player) {
        if (shopEditWaiting.containsKey(player)) {
            shopEditWaiting.remove(player);
        }
    }

    public static void clearWaiting(String name) {
        removeSetupWaiting(name);
        removeBuySellWaiting(name);
        removeShopEditWaiting(name);
    }

}
