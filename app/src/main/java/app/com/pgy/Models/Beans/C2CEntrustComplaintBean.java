package app.com.pgy.Models.Beans;

import java.util.List;

/**
 * Create by Android on 2019/10/22 0022
 */
public class C2CEntrustComplaintBean {

    /**
     * imgUrl : ["https://www.baidu.com/","https://www.baidu.com/","https://www.baidu.com/"]
     * reason : 原因原因原因
     * time : 2019-10-08 16:45:46
     * reply : 回复回复回复
     * orderId : 1
     */

    private String reason;
    private String time;
    private String reply;
    private String orderId;
    private List<String> imgUrl;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }
}
