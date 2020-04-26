package app.com.pgy.Models;

import java.io.Serializable;

public class RenGouInitBan implements Serializable {

    /**
     * imgUrl : http://qr.topscan.com/api.php?text=dfsdgdsfagdfg
     * explain : 认购玩法说明认购玩法说明认购玩法说明
     * coinType : 0
     * total : 10000.0
     * address : dfsdgdsfagdfg
     * defaultRadio : 5.0
     * countUsers : 4
     * percentage : 10.0
     * endTime : 2020-04-15 20:00:00
     * id : 1
     * coinName : CNHT
     * buyingTotal : 1000
     */

    private String imgUrl;
    private String explain;
    private int coinType;
    private double total;
    private String address;
    private double defaultRadio;
    private int countUsers;
    private double percentage;
    private String endTime;
    private int id;
    private String coinName;
    private String buyingTotal;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDefaultRadio() {
        return defaultRadio;
    }

    public void setDefaultRadio(double defaultRadio) {
        this.defaultRadio = defaultRadio;
    }

    public int getCountUsers() {
        return countUsers;
    }

    public void setCountUsers(int countUsers) {
        this.countUsers = countUsers;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getBuyingTotal() {
        return buyingTotal;
    }

    public void setBuyingTotal(String buyingTotal) {
        this.buyingTotal = buyingTotal;
    }
}
