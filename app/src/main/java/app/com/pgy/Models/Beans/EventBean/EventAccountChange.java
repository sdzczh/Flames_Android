package app.com.pgy.Models.Beans.EventBean;

/**
 * Create by Android on 2019/12/11 0011
 */
public class EventAccountChange {
    private int index = 0;
    public EventAccountChange(int account){
        this.index = account;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
