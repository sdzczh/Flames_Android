package app.com.pgy.Models.Beans.EventBean;

/**
 * eventBus传递事件
 * C2C顶部币种选择
 * @author xuqingzhong
 */

public class EventC2cCoinType {
    private int coinType;
    public EventC2cCoinType(int coinType){
        this.coinType = coinType;
    }

    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }
}
