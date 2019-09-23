package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/6 0006.
 * @author xuqingzhong
 * 提现订单
 */

public class WithdrawOrder {

    /**
     * list : [{"account":"银行卡号","amount":"提现金额","bankName":"开户行名称","coinType":0,"createTime":"创建时间","id":1231,"onlineNum":"微信或支付宝账号","orderNum":"提现订单号","remark":"处理结果","state":1,"type":1}]
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
         * account : 银行卡号
         * amount : 提现金额
         * bankName : 开户行名称
         * coinType : 0
         * createTime : 创建时间
         * id : 1231
         * onlineNum : 微信或支付宝账号
         * orderNum : 提现订单号
         * remark : 处理结果
         * state : 1    状态 0未处理 1提现完成 2已撤销
         * type : 1     0支付宝 1微信 2银行卡
         */

        private String account;
        private String amount;
        private String bankName;
        private int coinType;
        private String createTime;
        private int id;
        private String onlineNum;
        private String orderNum;
        private String remark;
        private int state;
        private int type;

        public void setAccount(String account) {
            this.account = account;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
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

        public void setOnlineNum(String onlineNum) {
            this.onlineNum = onlineNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAccount() {
            return account;
        }

        public String getAmount() {
            return amount;
        }

        public String getBankName() {
            return bankName;
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

        public String getOnlineNum() {
            return onlineNum;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public String getRemark() {
            return remark;
        }

        public int getState() {
            return state;
        }

        public int getType() {
            return type;
        }
    }
}
