package huoli.com.pgy.Models.Beans;

/**
 * Created by Administrator on 2018/2/25 0025.
 */

public class C2CBusinessCoinAvail {


    /**
     * minDealAmt : 200
     * latestPrice : 2
     * deposit : 1.0
     * availBalance : 0
     * platFee : 0
     */

    private String minDealAmt;
    private String latestPrice;
    private String deposit;
    private String availBalance;
    private String platFee;

    public String getMinDealAmt() {
        return minDealAmt;
    }

    public void setMinDealAmt(String minDealAmt) {
        this.minDealAmt = minDealAmt;
    }

    public String getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(String latestPrice) {
        this.latestPrice = latestPrice;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getAvailBalance() {
        return availBalance;
    }

    public void setAvailBalance(String availBalance) {
        this.availBalance = availBalance;
    }

    public String getPlatFee() {
        return platFee;
    }

    public void setPlatFee(String platFee) {
        this.platFee = platFee;
    }
}
