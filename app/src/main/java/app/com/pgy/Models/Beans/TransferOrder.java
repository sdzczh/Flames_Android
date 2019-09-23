package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/6 0006.
 * @author xuqingzhong
 * 转账订单
 */

public class TransferOrder {

    /**
     * list : [{"amount":"转账金额","coinType":0,"createTime":"创建时间","id":1231,"orderNum":"订单号","state":1,"toAccount":" 对方账户","type":0}]
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
         * amount : 转账金额
         * coinType : 0
         * createTime : 创建时间
         * id : 1231
         * orderNum : 订单号
         * state : 1
         * toAccount :  对方账户
         * type : 0
         */

        private String amount;
        private int coinType;
        private String createTime;
        private int id;
        private String orderNum;
        private int state;
        private String toAccount;
        private int type;

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

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setToAccount(String toAccount) {
            this.toAccount = toAccount;
        }

        public void setType(int type) {
            this.type = type;
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

        public String getOrderNum() {
            return orderNum;
        }

        public int getState() {
            return state;
        }

        public String getToAccount() {
            return toAccount;
        }

        public int getType() {
            return type;
        }
    }
}
