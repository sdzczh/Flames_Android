package app.com.pgy.Models.Beans;

/**
 * Created by YX on 2018/5/30.
 */

public class LeverPagerInfo {
    String risk;//  风险率 String"
    String blastPrice;// 爆仓价格 String
    String unitAvailBalance;// 可用计价币数量 String
    String orderAvailBalance;// 可用交易币数量 String
    String exchangeRate;// 计价币对人民币的汇率 String

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getRisk() {
        return risk;
    }

    public void setBlastPrice(String blastPrice) {
        this.blastPrice = blastPrice;
    }

    public String getBlastPrice() {
        return blastPrice;
    }

    public void setUnitAvailBalance(String unitAvailBalance) {
        this.unitAvailBalance = unitAvailBalance;
    }

    public String getUnitAvailBalance() {
        return unitAvailBalance;
    }

    public void setOrderAvailBalance(String orderAvailBalance) {
        this.orderAvailBalance = orderAvailBalance;
    }

    public String getOrderAvailBalance() {
        return orderAvailBalance;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }
}
