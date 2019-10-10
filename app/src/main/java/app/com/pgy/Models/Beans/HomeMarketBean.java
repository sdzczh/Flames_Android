package app.com.pgy.Models.Beans;

/**
 * Create by Android on 2019/10/10 0010
 */
public class HomeMarketBean {
    String coinType;
    String newPriceCNY;
    String chgPrice;
    String orderCoinType;

    String unitCoinType;
    String newPrice;

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setNewPriceCNY(String newPriceCNY) {
        this.newPriceCNY = newPriceCNY;
    }

    public String getNewPriceCNY() {
        return newPriceCNY;
    }

    public void setChgPrice(String chgPrice) {
        this.chgPrice = chgPrice;
    }

    public String getChgPrice() {
        return chgPrice;
    }

    public void setOrderCoinType(String orderCoinType) {
        this.orderCoinType = orderCoinType;
    }

    public String getOrderCoinType() {
        return orderCoinType;
    }

    public void setUnitCoinType(String unitCoinType) {
        this.unitCoinType = unitCoinType;
    }

    public String getUnitCoinType() {
        return unitCoinType;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }
}
