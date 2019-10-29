package app.com.pgy.Models.Beans.EventBean;

/**
 * Create by Android on 2019/10/29 0029
 */
public class EventGoodsEntrustChange {
    private int perCoinType;
    private int tradeCoinType;
    public EventGoodsEntrustChange(int perCoinType,int tradeCoinType){
        this.perCoinType = perCoinType;
        this.tradeCoinType = tradeCoinType;
    }

    public int getPerCoinType() {
        return perCoinType;
    }

    public void setPerCoinType(int perCoinType) {
        this.perCoinType = perCoinType;
    }

    public int getTradeCoinType() {
        return tradeCoinType;
    }

    public void setTradeCoinType(int tradeCoinType) {
        this.tradeCoinType = tradeCoinType;
    }
}
