package app.com.pgy.Models;

public class TakerinitBean {
    /**
     * buyPrice : 7.0
     * minAmount : 10.0
     * explainText : 大师傅但是
     * maxAmount : 2000.0
     * salePrice : 7.01
     */

    private double buyPrice;
    private double minAmount;
    private String explainText;
    private double maxAmount;
    private double salePrice;

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public String getExplainText() {
        return explainText;
    }

    public void setExplainText(String explainText) {
        this.explainText = explainText;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }
}
