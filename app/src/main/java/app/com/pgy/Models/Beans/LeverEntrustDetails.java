package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YX on 2018/6/1.
 */

public class LeverEntrustDetails implements Serializable {
    /**
     * amount : 10
     * average : 100
     * dealAmount : 3.5
     * fee : 0.0000
     * feeCoinType : 0
     * id : 22
     * orderCoinType : 6
     * orderType : 1
     * price : 100
     * total : 350
     * type : 1
     * state : 0
     * unitCoinType : 0
     * createTime : 2018-1-15 20:20:47
     * list : [{"amount":"2.5","createTime":"2018-1-15 20:27:14","orderCoinType":6,"price":"100","total":"250","unitCoinType":0}]
     */

    private String amount;
    private String average;
    private String dealAmount;
    private String fee;
    private int feeCoinType;
    private int id;
    private int orderCoinType;
    private int orderType;
    private String price;
    private String total;
    private int type;
    private int state;
    private int unitCoinType;
    private String createTime;
    private List<ListBean> list;

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public void setDealAmount(String dealAmount) {
        this.dealAmount = dealAmount;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public void setFeeCoinType(int feeCoinType) {
        this.feeCoinType = feeCoinType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderCoinType(int orderCoinType) {
        this.orderCoinType = orderCoinType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setUnitCoinType(int unitCoinType) {
        this.unitCoinType = unitCoinType;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public String getAmount() {
        return amount;
    }

    public String getAverage() {
        return average;
    }

    public String getDealAmount() {
        return dealAmount;
    }

    public String getFee() {
        return fee;
    }

    public int getFeeCoinType() {
        return feeCoinType;
    }

    public int getId() {
        return id;
    }

    public int getOrderCoinType() {
        return orderCoinType;
    }

    public int getOrderType() {
        return orderType;
    }

    public String getPrice() {
        return price;
    }

    public String getTotal() {
        return total;
    }

    public int getType() {
        return type;
    }

    public int getState() {
        return state;
    }

    public int getUnitCoinType() {
        return unitCoinType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean {
        /**
         * amount : 2.5
         * createTime : 2018-1-15 20:27:14
         * orderCoinType : 6
         * price : 100
         * total : 250
         * unitCoinType : 0
         */

        private String amount;
        private String createTime;
        private int orderCoinType;
        private String price;
        private String total;
        private int unitCoinType;

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setOrderCoinType(int orderCoinType) {
            this.orderCoinType = orderCoinType;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public void setUnitCoinType(int unitCoinType) {
            this.unitCoinType = unitCoinType;
        }

        public String getAmount() {
            return amount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public int getOrderCoinType() {
            return orderCoinType;
        }

        public String getPrice() {
            return price;
        }

        public String getTotal() {
            return total;
        }

        public int getUnitCoinType() {
            return unitCoinType;
        }
    }

}
