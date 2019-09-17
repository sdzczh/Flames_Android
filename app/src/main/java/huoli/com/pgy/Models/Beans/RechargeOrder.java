package huoli.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/6 0006.
 * @author xuqingzhong
 * 充值订单
 */

public class RechargeOrder{

    /**
     * list : [{"amount":123.2,"coinType":0,"createTime":123112313,"id":123131,"onlineNum":"1432143@qq.co","orderNumCli":12431414,"account":1414141414142,"bankName":"中国银行","receiptAccName":"张三","receiptAccount":1231312,"receiptBankName":"fsfsf","receiptOnlineNum":"2234sdfsa","remark":0,"state":1,"type":0}]
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
         * amount : 123.2
         * coinType : 0
         * createTime : 12/12
         * id : 123131
         * onlineNum : 1432143@qq.co
         * orderNumCli : 12431414
         * account : 1414141414142
         * bankName : 中国银行
         * receiptAccName : 张三
         * receiptAccount : 1231312
         * receiptBankName : fsfsf
         * receiptOnlineNum : 2234sdfsa
         * remark : 0
         * state : 1
         * type : 0
         */

        private String amount;
        private int coinType;
        private String createTime;
        private int id;
        private String onlineNum;
        private String orderNumCli;
        private String account;
        private String bankName;
        private String receiptAccName;
        private String receiptAccount;
        private String receiptBankName;
        private String receiptOnlineNum;
        private String remark;
        private int state;
        private int type;


        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getCoinType() {
            return coinType;
        }

        public void setCoinType(int coinType) {
            this.coinType = coinType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOnlineNum() {
            return onlineNum;
        }

        public void setOnlineNum(String onlineNum) {
            this.onlineNum = onlineNum;
        }

        public String getOrderNumCli() {
            return orderNumCli;
        }

        public void setOrderNumCli(String orderNumCli) {
            this.orderNumCli = orderNumCli;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getReceiptAccName() {
            return receiptAccName;
        }

        public void setReceiptAccName(String receiptAccName) {
            this.receiptAccName = receiptAccName;
        }

        public String getReceiptAccount() {
            return receiptAccount;
        }

        public void setReceiptAccount(String receiptAccount) {
            this.receiptAccount = receiptAccount;
        }

        public String getReceiptBankName() {
            return receiptBankName;
        }

        public void setReceiptBankName(String receiptBankName) {
            this.receiptBankName = receiptBankName;
        }

        public String getReceiptOnlineNum() {
            return receiptOnlineNum;
        }

        public void setReceiptOnlineNum(String receiptOnlineNum) {
            this.receiptOnlineNum = receiptOnlineNum;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
