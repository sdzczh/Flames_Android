package huoli.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * 委托单
 * @author xuqingzhong
 */

public class Entrust {

    /**
     * list : [{"amount":"10","createTime":"2018-1-15 20:20:47","dealAmount":"3.5","id":22,"orderCoinType":6,"orderType":1,"price":"100","type":1,"unitCoinType":0}]
     */

    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean implements Serializable{
        /**
         * amount : 10
         * createTime : 2018-1-15 20:20:47
         * dealAmount : 3.5
         * id : 22
         * orderCoinType : 6
         * orderType : 1
         * price : 100
         * type : 1
         * state : 0,  状态 0未成效 1已成交 2交易撤销 3交易失败
         * unitCoinType : 0
         */

        private String amount;
        private String createTime;
        private String dealAmount;
        private int id;
        private int orderCoinType;
        private int orderType;
        private String price;
        private int type;
        private int state;
        private int unitCoinType;


        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setDealAmount(String dealAmount) {
            this.dealAmount = dealAmount;
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

        public void setType(int type) {
            this.type = type;
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

        public String getDealAmount() {
            return dealAmount;
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

        public int getType() {
            return type;
        }

        public int getUnitCoinType() {
            return unitCoinType;
        }

    }

}
