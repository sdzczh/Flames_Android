package huoli.com.pgy.Models.Beans;

/**
 * Created by Administrator on 2018/2/25 0025.
 */

public class CoinAvailbalance {

    private String availBalance;
    private double exchangeRate;

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getAvailBalance() {
        return availBalance;
    }

    public void setAvailBalance(String availBalance) {
        this.availBalance = availBalance;
    }

}
