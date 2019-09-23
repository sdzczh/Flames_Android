package app.com.pgy.Models.Beans.EventBean;

/**
 * Created by YX on 2018/7/27.
 */

public class EventMarketScene {
    public final static int TYPE_MARKET_YIBI = 0;//COIN行情
    public final static int TYPE_MARKET_WORlD = 1;//全球行情
    public final static int TYPE_TRADE_GOODS = 2;//现货交易
    public final static int TYPE_TRADE_LEVER = 3;//杠杆交易

    private int type;
    public EventMarketScene(int type){
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
