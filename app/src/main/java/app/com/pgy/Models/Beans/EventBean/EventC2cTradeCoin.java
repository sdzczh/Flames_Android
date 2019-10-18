package app.com.pgy.Models.Beans.EventBean;

/**
 * Create by Android on 2019/10/18 0018
 */
public class EventC2cTradeCoin {
    private int coinType;
    public EventC2cTradeCoin(int coinType){
        this.coinType = coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    public int getCoinType() {
        return coinType;
    }
}
