package app.com.pgy.Models.Beans;

/**
 * Created by YX on 2018/6/1.
 */

public class LeverManagerInfo {
    String content;// 点击风险率弹出的提示内容 String
    String risk;// 0.0%", 风险率 String
    String orderFrozenBalance;// 0", 交易币冻结数量 String
    String title;// 已爆仓", 点击风险率弹出的提示标题 String
    int riskLevel;// 3, 风险等级 1：安全（风险率>130%） 2：警告（110%<风险率<130%） 3：爆仓（风险率<110%）
    String orderArrears;// 0", 未还交易币数量 String
    String unitArrears;// 0", 未还计价币数量 String
    String unitAvailBalance;// 3594.5", 计价币可用数量 String
    String orderAvailBalance;// 270", 交易币可用数量 String
    String unitFrozenBalance;// 0" 计价币冻结数量 String

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getRisk() {
        return risk;
    }

    public void setOrderFrozenBalance(String orderFrozenBalance) {
        this.orderFrozenBalance = orderFrozenBalance;
    }

    public String getOrderFrozenBalance() {
        return orderFrozenBalance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public void setOrderArrears(String orderArrears) {
        this.orderArrears = orderArrears;
    }

    public String getOrderArrears() {
        return orderArrears;
    }

    public void setUnitArrears(String unitArrears) {
        this.unitArrears = unitArrears;
    }

    public String getUnitArrears() {
        return unitArrears;
    }

    public void setUnitAvailBalance(String unitAvailBalance) {
        this.unitAvailBalance = unitAvailBalance;
    }

    public String getUnitAvailBalance() {
        return unitAvailBalance;
    }

    public void setOrderAvailBalance(String orderAvailBalance) {
        this.orderAvailBalance = orderAvailBalance;
    }

    public String getOrderAvailBalance() {
        return orderAvailBalance;
    }

    public void setUnitFrozenBalance(String unitFrozenBalance) {
        this.unitFrozenBalance = unitFrozenBalance;
    }

    public String getUnitFrozenBalance() {
        return unitFrozenBalance;
    }
}
