package app.com.pgy.Models.Beans;

/**
 * *  @Description:描述
 * *  @Author: EDZ
 * *  @CreateDate: 2019/7/22 18:01
 */
public class OdinMyFollowBean {

    /**
     * create_time : 1563441295000
     * phone : 133****3284
     * amount : 1
     */

    private long create_time;
    private String phone;
    private String amount;

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
