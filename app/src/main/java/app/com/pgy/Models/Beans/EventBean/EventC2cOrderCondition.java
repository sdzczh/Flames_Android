package app.com.pgy.Models.Beans.EventBean;

/**
 * eventBus传递事件
 * C2C商家订单条件
 * tradeType 交易类型，买、卖
 * stateType 类型
 * @author xuqingzhong
 */

public class EventC2cOrderCondition {
    private int tradeType;
    private int stateType;

    public EventC2cOrderCondition(int tradeType, int stateType) {
        this.tradeType = tradeType;
        this.stateType = stateType;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getStateType() {
        return stateType;
    }

    public void setStateType(int stateType) {
        this.stateType = stateType;
    }
}
