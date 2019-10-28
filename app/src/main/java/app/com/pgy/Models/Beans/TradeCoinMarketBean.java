package app.com.pgy.Models.Beans;

/**
 * Create by Android on 2019/10/25 0025
 */
public class TradeCoinMarketBean {

    /**
     * orderCoinType : 1
     * high : 1.4989
     * unitCoinType : 0
     * newPriceCNY : 0.65
     * low : 0.6300
     * unitCoinName : ECN
     * chgPrice : 3.18
     * orderCoinName : ODIN
     * orderCoinCnName : ODIN
     * newPrice : 0.6500
     * sumAmount : 5625.1519
     */

    private int orderCoinType;
    private String high;
    private int unitCoinType;
    private String newPriceCNY;
    private String low;
    private String unitCoinName;
    private String chgPrice;
    private String orderCoinName;
    private String orderCoinCnName;
    private String newPrice;
    private String sumAmount;
    private boolean isCheck;

    public int getOrderCoinType() {
        return orderCoinType;
    }

    public void setOrderCoinType(int orderCoinType) {
        this.orderCoinType = orderCoinType;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public int getUnitCoinType() {
        return unitCoinType;
    }

    public void setUnitCoinType(int unitCoinType) {
        this.unitCoinType = unitCoinType;
    }

    public String getNewPriceCNY() {
        return newPriceCNY;
    }

    public void setNewPriceCNY(String newPriceCNY) {
        this.newPriceCNY = newPriceCNY;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getUnitCoinName() {
        return unitCoinName;
    }

    public void setUnitCoinName(String unitCoinName) {
        this.unitCoinName = unitCoinName;
    }

    public String getChgPrice() {
        return chgPrice;
    }

    public void setChgPrice(String chgPrice) {
        this.chgPrice = chgPrice;
    }

    public String getOrderCoinName() {
        return orderCoinName;
    }

    public void setOrderCoinName(String orderCoinName) {
        this.orderCoinName = orderCoinName;
    }

    public String getOrderCoinCnName() {
        return orderCoinCnName;
    }

    public void setOrderCoinCnName(String orderCoinCnName) {
        this.orderCoinCnName = orderCoinCnName;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isCheck() {
        return isCheck;
    }
}
