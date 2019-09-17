package huoli.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * C2C普通用户当前委托和历史委托单
 * @author xuqingzhong
 */

public class C2cNormalEntrust {


    /**
     * list : [{"amount":"5","coinType":2,"createTime":"2018-02-24 11:04:07","id":1,"inactiveTime":"11:04:07","orderType":0,"price":"10","state":1,"total":"50"}]
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
         * amount : 5
         * coinType : 2
         * createTime : 2018-02-24 11:04:07
         * id : 1
         * inactiveTime : 11:04:07
         * orderType : 0
         * price : 10
         * state : 1
         * total : 50
         */

        private String amount;
        private int coinType;
        private String createTime;
        private int id;
        private String inactiveTime;
        private int orderType;
        private String price;
        private int state;
        private String total;

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

        public void setInactiveTime(String inactiveTime) {
            this.inactiveTime = inactiveTime;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setTotal(String total) {
            this.total = total;
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

        public String getInactiveTime() {
            return inactiveTime;
        }

        public int getOrderType() {
            return orderType;
        }

        public String getPrice() {
            return price;
        }

        public int getState() {
            return state;
        }

        public String getTotal() {
            return total;
        }
    }
}
