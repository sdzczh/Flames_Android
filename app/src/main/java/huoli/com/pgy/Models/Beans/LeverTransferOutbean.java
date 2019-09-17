package huoli.com.pgy.Models.Beans;

/**
 * Created by YX on 2018/6/7.
 */

public class LeverTransferOutbean {
    String orderAvailBalance;// "0", 可转出交易币数量 String
    String unitAvailBalance;// "0" 可转出计价币数量 String

    public void setOrderAvailBalance(String orderAvailBalance) {
        this.orderAvailBalance = orderAvailBalance;
    }

    public String getOrderAvailBalance() {
        return orderAvailBalance;
    }

    public void setUnitAvailBalance(String unitAvailBalance) {
        this.unitAvailBalance = unitAvailBalance;
    }

    public String getUnitAvailBalance() {
        return unitAvailBalance;
    }
}
