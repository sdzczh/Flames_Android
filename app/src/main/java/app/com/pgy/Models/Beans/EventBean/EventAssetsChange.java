package app.com.pgy.Models.Beans.EventBean;

/**
 * Created by YX on 2018/7/30.
 */

public class EventAssetsChange {
    private int accountType;

    public EventAssetsChange(int accountType){
        this.accountType = accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getAccountType() {
        return accountType;
    }
}
