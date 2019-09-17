package huoli.com.pgy.Models.Beans;

/**
 * Created by YX on 2018/5/23.
 */

public class IMUnreadEvent {

    public int unread = 0;

    public IMUnreadEvent(int unread){
        this.unread = unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getUnread() {
        return unread;
    }
}
