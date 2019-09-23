package app.com.pgy.Models.Beans;

import java.io.Serializable;

/**
 * 交易界面信息
 * @author xuqingzhong
 */

public class TradeMessage implements Serializable{


    /**
     * rmbRate : 1
     * unit : 99
     * order : 15000
     */

    private String rmbRate;
    private String unit;
    private String order;
    private int priceScale;
    private int amountScale;

    public int getPriceScale() {
        return priceScale;
    }

    public void setPriceScale(int priceScale) {
        this.priceScale = priceScale;
    }

    public int getAmountScale() {
        return amountScale;
    }

    public void setAmountScale(int amountScale) {
        this.amountScale = amountScale;
    }

    public String getRmbRate() {
        return rmbRate;
    }

    public void setRmbRate(String rmbRate) {
        this.rmbRate = rmbRate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
