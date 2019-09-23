package app.com.pgy.Models.Beans.EventBean;

/**
 * Created by YX on 2018/6/25.
 */

public class EventGoodsCoinChange {
    private int perCoinType;
    private int tradeCoinType;
    public EventGoodsCoinChange(int perCoinType,int tradeCoinType){
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

    @Override
    public String toString() {
        return "EventGoodsCoinChange{" +
                "perCoinType=" + perCoinType +
                ", tradeCoinType=" + tradeCoinType +
                '}';
    }
}
