package huoli.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YX on 2018/5/31.
 */

public class LeverEntrust {
    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }
//                 "id": 7, 委托单id int
//                "amount": "10", 委托数量 String
//                "unitCoinType": 0, 计价币种  int
//                "createTime": "2018-5-3015: 04: 11", 委托时间 String
//                "price": "90", 委托价格 String
//                "orderCoinType": 1, 交易币种 int
//                "dealAmount": "10", 已成交数量 String
//                "state": 1, 状态 0未成交 1已成交 2交易撤销 3交易失败 int
//                "type": 1 0：买入 1：卖出 int
    public static class ListBean implements Serializable {
        private String amount;
        private String createTime;
        private String dealAmount;
        private int id;
        private int orderCoinType;
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
