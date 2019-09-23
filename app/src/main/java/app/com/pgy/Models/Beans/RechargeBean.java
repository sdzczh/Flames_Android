package app.com.pgy.Models.Beans;

/**
 * Created by YX on 2018/5/18.
 */

public class RechargeBean {

    /**
     * rechargeInfo : KN充值
     * coinName : KN
     * cnName : 人民币
     * rechAddress : Lc84Z5ruo47nDiNMYUVt9WmtPTzNb4gPgQ
     */

    private String rechargeInfo;
    private String coinName;
    private String cnName;
    private String rechAddress;
    private String fee;
    private String onoff;

    public String getRechargeInfo() {
        return rechargeInfo;
    }

    public void setRechargeInfo(String rechargeInfo) {
        this.rechargeInfo = rechargeInfo;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getRechAddress() {
        return rechAddress;
    }

    public void setRechAddress(String rechAddress) {
        this.rechAddress = rechAddress;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFee() {
        return fee;
    }

    public void setOnoff(String onoff) {
        this.onoff = onoff;
    }

    public String getOnoff() {
        return onoff;
    }
}
