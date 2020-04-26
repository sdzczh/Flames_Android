package app.com.pgy.Models;

import java.io.Serializable;

public class RenGouInitBan implements Serializable {

    /**
     * imgUrl : http://qr.topscan.com/api.php?text=dfsdgdsfagdfg
     * explain : 士大夫大师傅但是
     * coinType : 1
     * address : dfsdgdsfagdfg
     * defaultRadio : 5
     * countUsers : 2
     * percentage : 0
     * endTime : 2020-04-15 20:00:00
     * id : 1
     * coinName : BTC
     * buyingTotal : 0
     */

    private String imgUrl;
    private String explain;
    private int coinType;
    private String address;
    private int defaultRadio;
    private int countUsers;
    private int percentage;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDefaultRadio() {
        return defaultRadio;
    }

    public void setDefaultRadio(int defaultRadio) {
        this.defaultRadio = defaultRadio;
    }

    public int getCountUsers() {
        return countUsers;
    }

    public void setCountUsers(int countUsers) {
        this.countUsers = countUsers;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
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
