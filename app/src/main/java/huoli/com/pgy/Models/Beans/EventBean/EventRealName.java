package huoli.com.pgy.Models.Beans.EventBean;

/**
 * Created by YX on 2018/5/2.
 */

public class EventRealName {
    boolean isSuccess = false;

    public EventRealName(boolean success){
        this.isSuccess = success;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
    public boolean getSuccess(){
        return this.isSuccess;
    }
}
