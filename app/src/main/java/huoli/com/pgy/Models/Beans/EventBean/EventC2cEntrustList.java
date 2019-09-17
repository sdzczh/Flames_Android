package huoli.com.pgy.Models.Beans.EventBean;

/**
 * 创建日期：2018/7/30 0030 on 下午 4:17
 * 描述:
 *
 * @author xu
 */

public class EventC2cEntrustList {
    private boolean isRefresh;

    public EventC2cEntrustList(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
