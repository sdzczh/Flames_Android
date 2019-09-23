package app.com.pgy.Models.Beans.EventBean;

/**
 * eventBus传递事件
 * 登录状态
 * @author xuqingzhong
 */

public class EventLoginState {
    private boolean isLoged;

    public EventLoginState(boolean isLoged) {
        this.isLoged = isLoged;
    }

    public boolean isLoged() {
        return isLoged;
    }

    public void setLoged(boolean loged) {
        isLoged = loged;
    }

    @Override
    public String toString() {
        return "EventLoginState{" +
                "isLoged=" + isLoged +
                '}';
    }
}
