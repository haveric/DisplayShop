package haveric.displayShop;

import net.milkbowl.vault.economy.Economy;

public class Econ {
    private static Economy econ = null;

    private Econ() { } // Private constructor for utility classes

    private static DisplayShop plugin = null;
    public static void init(DisplayShop displayShop) {
        plugin = displayShop;
    }
    public static void setEconomy(Economy newEcon) {
        econ = newEcon;
    }

    private static boolean econEnabled() {
        return (econ != null);
    }

    private static double getMoney(String player) {
        double money = 0;

        if (econEnabled()) {
            money = econ.getBalance(player);
        }
        return money;
    }

    public static int canAfford(String player, double cost) {
        int canAfford = 0;

        double money = getMoney(player);

        canAfford = (int) (money / cost);

        return canAfford;
    }

    public static int canAfford(String player, double cost, double minMoney) {
        int canAfford = 0;

        double money = getMoney(player);

        double usableMoney = money - minMoney;
        plugin.log.info("Usable Money: " + usableMoney);
        if (usableMoney > 0) {
            canAfford = (int) (usableMoney / cost);
        }

        return canAfford;
    }

    public static void pay(String playerPaying, String playerToPay, double amount) {
        if (econEnabled()) {
            econ.withdrawPlayer(playerPaying, amount);
            econ.depositPlayer(playerToPay, amount);
        }
    }
}
