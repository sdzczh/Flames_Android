package app.com.pgy.Models.Beans;

import java.io.Serializable;

/**
 * @author xuqingzhong
 * 邀请好友
 */

public class Invitation implements Serializable {

    /**
     * referAmount : 1
     * posterUrl : dfdfdf
     * refer : ewrerwe
     */

    private String referAmount;
    private String posterUrl;
    private String refer;

    public void setReferAmount(String referAmount) {
        this.referAmount = referAmount;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getReferAmount() {
        return referAmount;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getRefer() {
        return refer;
    }
}
