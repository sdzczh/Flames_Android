package app.com.pgy.Models.Beans.EventBean;

/**
 * Create by Android on 2019/10/30 0030
 */
public class EventTradeCancelAll {
    boolean cancel = false;
    public EventTradeCancelAll(boolean cancel){
        this.cancel = cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCancel() {
        return cancel;
    }
}
