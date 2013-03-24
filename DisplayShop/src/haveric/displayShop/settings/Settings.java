package haveric.displayShop.settings;

public class Settings {

    double minMoney = 0;

    public double getMinMoney() {
        return minMoney;
    }

    public boolean setMinMoney(int newMinMoney) {
        boolean success = false;
        if (newMinMoney >= 0) {
            minMoney = newMinMoney;
            success = true;
        }
        return success;
    }
}
