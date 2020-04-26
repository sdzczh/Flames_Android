package app.com.pgy.Models;

import java.io.Serializable;

public class RenGouRecord implements Serializable {

    /**
     * usdtAmount : 0
     * amount : 100
     * createTime : 2020-04-24 14:38:03
     * realityAmount : 0
     * id : 1
     * state : 0
     * userId : 1
     */

    private int usdtAmount;
    private int amount;
    private String createTime;
    private int realityAmount;
    private int id;
    private int state;
    private int userId;

    public int getUsdtAmount() {
        return usdtAmount;
    }

    public void setUsdtAmount(int usdtAmount) {
        this.usdtAmount = usdtAmount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getRealityAmount() {
        return realityAmount;
    }

    public void setRealityAmount(int realityAmount) {
        this.realityAmount = realityAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
