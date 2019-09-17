package huoli.com.pgy.Models.Beans.ResponseBean;

import java.util.List;

/**
 * @author xuqingzhong
 * k线图数据
 */
public class LastDealBean {
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * amount : 0.1
         * createTime : 22: 14: 40
         * price : 200
         * orderType : 0
         */

        private String amount;
        private String createTime;
        private String price;
        private int orderType;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }
    }
}

