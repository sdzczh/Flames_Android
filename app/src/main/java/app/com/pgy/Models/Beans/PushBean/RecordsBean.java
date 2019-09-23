package app.com.pgy.Models.Beans.PushBean;

/**
 * 创建日期：2018/7/27 0027 on 下午 4:42
 * 描述:
 *
 * @author xu
 */

public class RecordsBean {

    /**
     * orderType : 0
     * amount : 0.02
     * createTime : 11:07:31
     * price : 0.62
     */

    private int orderType;
    private String amount;
    private String createTime;
    private String price;

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
