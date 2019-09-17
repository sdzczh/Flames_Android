package huoli.com.pgy.Models.Beans;

/**
 * Created by YX on 2018/7/20.
 */

public class WithdrawBean {

    /**
     * availBalance : 0.195
     * fee : 1.0
     * info : kn提现
     */

    private String availBalance;
    private String fee;
    private String info;

    public String getAvailBalance() {
        return availBalance;
    }

    public void setAvailBalance(String availBalance) {
        this.availBalance = availBalance;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
