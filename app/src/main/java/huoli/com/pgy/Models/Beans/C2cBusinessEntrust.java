package huoli.com.pgy.Models.Beans;

import java.util.List;

/**
 * C2C商家用户当前委托和历史委托单
 * @author xuqingzhong
 */

public class C2cBusinessEntrust {


    /**
     * list : [{"amount":"19","coinType":2,"createTime":"2018-02-24 09:47:30","id":3,"orderFlag":0,"orderType":1,"payType":7,"price":"10","remain":"19","state":0,"totalMax":"50","totalMin":"5"}]
     */

    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean {
        /**
         * amount : 19
         * coinType : 2
         * createTime : 2018-02-24 09:47:30
         * id : 3
         * orderFlag : 0
         * orderType : 1
         * payType : 7
         * price : 10
         * remain : 19
         * state : 0
         * totalMax : 50
         * totalMin : 5
         */

        private String amount;
        private int coinType;
        private String createTime;
        private int id;
        private boolean orderFlag;
        private int orderType;
        private int payType;
        private String price;
        private String remain;
        private int state;
        private String totalMax;
        private String totalMin;
        private String total;


        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setCoinType(int coinType) {
            this.coinType = coinType;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isOrderFlag() {
            return orderFlag;
        }

        public void setOrderFlag(boolean orderFlag) {
            this.orderFlag = orderFlag;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setRemain(String remain) {
            this.remain = remain;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setTotalMax(String totalMax) {
            this.totalMax = totalMax;
        }

        public void setTotalMin(String totalMin) {
            this.totalMin = totalMin;
        }

        public String getAmount() {
            return amount;
        }

        public int getCoinType() {
            return coinType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public int getId() {
            return id;
        }

        public int getOrderType() {
            return orderType;
        }

        public int getPayType() {
            return payType;
        }

        public String getPrice() {
            return price;
        }

        public String getRemain() {
            return remain;
        }

        public int getState() {
            return state;
        }

        public String getTotalMax() {
            return totalMax;
        }

        public String getTotalMin() {
            return totalMin;
        }
    }
}
