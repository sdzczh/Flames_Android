package huoli.com.pgy.Models.Beans.EventBean;

/**
 * Created by YX on 2018/7/28.
 */

public class EventForceUpBanner {
    public static final int TYPE_WITHOUTBLOCK = 1;
    public static final int TYPE_ALL= 2;

    private int type;
    public EventForceUpBanner(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
