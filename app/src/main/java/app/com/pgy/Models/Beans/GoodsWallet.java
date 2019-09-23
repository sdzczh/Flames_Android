package app.com.pgy.Models.Beans;

import java.io.Serializable;

/**
 * 现货余额
 * @author xuqingzhong
 */

public class GoodsWallet implements Serializable{

    /**
     * orderAmount : 0.0000
     * orderAvailBalance : 15120.55
     * orderFrozenBalance : 3
     * unitAvailBalance : 8083.88
     * unitFrozenBalance : 3
     */

    private String orderAmount;
    private String orderAvailBalance;
    private String orderFrozenBalance;
    private String unitAvailBalance;
    private String unitFrozenBalance;

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setOrderAvailBalance(String orderAvailBalance) {
        this.orderAvailBalance = orderAvailBalance;
    }

    public void setOrderFrozenBalance(String orderFrozenBalance) {
        this.orderFrozenBalance = orderFrozenBalance;
    }

    public void setUnitAvailBalance(String unitAvailBalance) {
        this.unitAvailBalance = unitAvailBalance;
    }

    public void setUnitFrozenBalance(String unitFrozenBalance) {
        this.unitFrozenBalance = unitFrozenBalance;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public String getOrderAvailBalance() {
        return orderAvailBalance;
    }

    public String getOrderFrozenBalance() {
        return orderFrozenBalance;
    }

    public String getUnitAvailBalance() {
        return unitAvailBalance;
    }

    public String getUnitFrozenBalance() {
        return unitFrozenBalance;
    }

    @Override
    public String toString() {
        return "GoodsWallet{" +
                "orderAmount='" + orderAmount + '\'' +
                ", orderAvailBalance='" + orderAvailBalance + '\'' +
                ", orderFrozenBalance='" + orderFrozenBalance + '\'' +
                ", unitAvailBalance='" + unitAvailBalance + '\'' +
                ", unitFrozenBalance='" + unitFrozenBalance + '\'' +
                '}';
    }
}
