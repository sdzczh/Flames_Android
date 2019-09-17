package huoli.com.pgy.Models.Beans.EventBean;

/**
 * Created by YX on 2018/5/4.
 */

public class EventGoodsChange {
    public final static int TYPE_BUY = 0;
    public final static int TYPE_SALE = 1;

    private int type;

    public EventGoodsChange(int type){
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
