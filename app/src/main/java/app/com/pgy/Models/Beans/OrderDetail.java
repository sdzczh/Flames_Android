package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * @description 充值提现转账详情
 * @author xuqingzhong
 */

public class OrderDetail{

    /**
     * list : [{"amount":"1.89","createTime":"01-05 21:38","remark":"","state":0}]
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
         * amount : 1.89
         * type 提现类型  3提现到其他账户   4提现到现货账户
         * createTime : 01-05 21:38
         * onlineNum
         * orderNum
         * remark :
         * state : 0
         * coinType
         * updateTime
         */

        private String amount;
        private int type;
        private String createTime;
        private String remark;
        private int state;
        private String orderNum;
        private String onlineNum;
        private int coinType;
        private String updateTime;

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getAmount() {
            return amount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getRemark() {
            return remark;
        }

        public int getState() {
            return state;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getOnlineNum() {
            return onlineNum;
        }

        public void setOnlineNum(String onlineNum) {
            this.onlineNum = onlineNum;
        }

        public int getCoinType() {
            return coinType;
        }

        public void setCoinType(int coinType) {
            this.coinType = coinType;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}

