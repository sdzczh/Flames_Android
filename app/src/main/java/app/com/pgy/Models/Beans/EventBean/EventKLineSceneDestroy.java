package app.com.pgy.Models.Beans.EventBean;

/**
 * 创建日期：2018/8/3 0003 on 下午 3:31
 * 描述:K线图场景销毁，返回原来场景
 *
 * @author xu
 */

public class EventKLineSceneDestroy {
    private boolean isDestory;

    public EventKLineSceneDestroy(boolean isDestory) {
        this.isDestory = isDestory;
    }

    public void setDestory(boolean destory) {
        isDestory = destory;
    }

    public boolean isDestory() {
        return isDestory;
    }
}
