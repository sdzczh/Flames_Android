package app.com.pgy.Models.Beans.EventBean;

/**
 * Created by YX on 2018/5/24.
 */

public class EventLeverGoodsCoinType {
    private int perCoinType;
    private int tradeCoinType;
    public EventLeverGoodsCoinType(int perCoinType,int tradeCoinType){
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
