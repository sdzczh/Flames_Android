package app.com.pgy.Models.Beans;

import java.util.List;

/**
 * Created by YX on 2018/7/17.
 */

public class CoinFlowDetail {
    /**
     * frozenBlance : 0
     * availBalance : 15000
     * availBalanceCny : 24
     * cnName : 大咖币
     * frozenBlanceCny : 0
     */

    private String frozenBlance;
    private String availBalance;
    private String availBalanceCny;
    private String cnName;
    private String frozenBlanceCny;

    private List<ListBean> FlowList;

    public void setFlowList(List<ListBean> flowList) {
        FlowList = flowList;
    }

    public List<ListBean> getFlowList() {
        return FlowList;
    }

    public String getFrozenBlance() {
        return frozenBlance;
    }

    public void setFrozenBlance(String frozenBlance) {
        this.frozenBlance = frozenBlance;
    }

    public String getAvailBalance() {
        return availBalance;
    }

    public void setAvailBalance(String availBalance) {
        this.availBalance = availBalance;
    }

    public String getAvailBalanceCny() {
        return availBalanceCny;
    }

    public void setAvailBalanceCny(String availBalanceCny) {
        this.availBalanceCny = availBalanceCny;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getFrozenBlanceCny() {
        return frozenBlanceCny;
    }

    public void setFrozenBlanceCny(String frozenBlanceCny) {
        this.frozenBlanceCny = frozenBlanceCny;
    }

    public static class ListBean {

        /**
         * amount : 200.0
         * createtime : 1531989951000
         * opertype : 交易挖矿--卖方
         * time : 07-19 16:45
         */

        private String resultAmount;
        private String opertype;
        private String time;

        public void setResultAmount(String resultAmount) {
            this.resultAmount = resultAmount;
        }

        public String getResultAmount() {
            return resultAmount;
        }

        public String getOpertype() {
            return opertype;
        }

        public void setOpertype(String opertype) {
            this.opertype = opertype;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
