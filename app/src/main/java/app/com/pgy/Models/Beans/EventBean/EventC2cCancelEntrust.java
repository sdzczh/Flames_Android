package app.com.pgy.Models.Beans.EventBean;

/**
 * eventBus传递事件
 * 登录状态
 * @author xuqingzhong
 */

public class EventC2cCancelEntrust {
    private boolean isCanceled;

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public EventC2cCancelEntrust(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }
}
