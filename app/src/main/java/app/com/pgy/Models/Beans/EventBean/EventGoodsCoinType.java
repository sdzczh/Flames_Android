package app.com.pgy.Models.Beans.EventBean;

/**
 * eventBus传递事件
 * 现货顶部币种选择
 * @author xuqingzhong
 */

public class EventGoodsCoinType {
    private int perCoinType;
    private int tradeCoinType;
    public EventGoodsCoinType(int perCoinType,int tradeCoinType){
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
