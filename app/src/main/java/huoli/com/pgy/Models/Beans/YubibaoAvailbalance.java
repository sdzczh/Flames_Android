package huoli.com.pgy.Models.Beans;

/**
 * Created by Administrator on 2018/2/25 0025.
 */

public class YubibaoAvailbalance {

    /**
     * minTransAmount : 0.0
     * availBalance : 1
     * profitReturnDate : ,06-15(星期五)
     * profitDay : T+1
     * minProfitAmount : 0
     */

    private String minTransAmount;//最小转账数量
    private String availBalance;//可用余额
    private String profitReturnDate;//预计到账时间
    private String profitDay;//收益计算时间
    private String minProfitAmount;//最小产息数量

    public String getMinTransAmount() {
        return minTransAmount;
    }

    public void setMinTransAmount(String minTransAmount) {
        this.minTransAmount = minTransAmount;
    }

    public String getAvailBalance() {
        return availBalance;
    }

    public void setAvailBalance(String availBalance) {
        this.availBalance = availBalance;
    }

    public String getProfitReturnDate() {
        return profitReturnDate;
    }

    public void setProfitReturnDate(String profitReturnDate) {
        this.profitReturnDate = profitReturnDate;
    }

    public String getProfitDay() {
        return profitDay;
    }

    public void setProfitDay(String profitDay) {
        this.profitDay = profitDay;
    }


    public String getMinProfitAmount() {
        return minProfitAmount;
    }

    public void setMinProfitAmount(String minProfitAmount) {
        this.minProfitAmount = minProfitAmount;
    }
}
