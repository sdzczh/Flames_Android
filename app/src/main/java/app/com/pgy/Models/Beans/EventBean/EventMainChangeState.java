package app.com.pgy.Models.Beans.EventBean;

/**
 * Created by YX on 2018/7/19.
 * 主页广播切换页面
 */

public class EventMainChangeState {
    public static final int CHANGE_TO_GOODS = 0;
    public static final int CHANGE_TO_C2C = 1;
    public static final int CHANGE_TO_MARK = 2;
    public static final int CHANGE_TO_HOME = 3;

    int state;
    public EventMainChangeState(int state){
        this.state = state;
    }

    public int getState(){
        return state;
    }
}
