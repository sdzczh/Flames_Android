package app.com.pgy.Models.Beans;

import java.util.Map;

/**
 * c2c详情
 * @author xuqingzhong
 */

public class C2CEntrustDetails {

    /**
     * amount : 1
     * coinType : 2
     * createTime : 2018-02-25 11:58:16
     * updateTime : 2018-02-25 11:58:16
     * orderNum : TK371519531097209
     * inactiveTime : 1519784189
     * orderType : 0
     * price : 10
     * state : 0
     * total : 10
     * contactPhone
     * contactName
     * payInfo : {"0":{"account":"145245@qq.com","bankName":"","branchName":"","id":1,"imgUrl":"Http:img/djsd/jpg","name":"哇哈哈","type":0},"1":{"account":"145244545","bankName":"123","branchName":"123","id":2,"imgUrl":"Http:img/djsd.jpg","name":"1231","type":1}}
     */

    private String referNum;
    private String otherNickName;
    private String amount;
    private int coinType;
    private String createTime;
    private String updateTime;
    private String orderNum;
    private int inactiveTimeInterval;
    private String inactiveTime;
    private int orderType;
    private String price;
    private int state;
    private String total;
    private String otherPhone;
    private String otherHeadPath;
    private String otherName;
    private Map<Integer,PayInfo> payInfo;
    private String buyerName;

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerName() {
        return buyerName;
    }

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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public int getInactiveTimeInterval() {
        return inactiveTimeInterval;
    }

    public void setInactiveTimeInterval(int inactiveTimeInterval) {
        this.inactiveTimeInterval = inactiveTimeInterval;
    }

    public String getInactiveTime() {
        return inactiveTime;
    }

    public void setInactiveTime(String inactiveTime) {
        this.inactiveTime = inactiveTime;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Map<Integer, PayInfo> getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(Map<Integer, PayInfo> payInfo) {
        this.payInfo = payInfo;
    }

    public String getReferNum() {
        return referNum;
    }

    public void setReferNum(String referNum) {
        this.referNum = referNum;
    }

    public String getOtherNickName() {
        return otherNickName;
    }

    public void setOtherNickName(String otherNickName) {
        this.otherNickName = otherNickName;
    }

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getOtherHeadPath() {
        return otherHeadPath;
    }

    public void setOtherHeadPath(String otherHeadPath) {
        this.otherHeadPath = otherHeadPath;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public static class PayInfo{

        /**
         * account : 145245@qq.com
         * bankName :
         * branchName :
         * id : 1
         * imgUrl : Http:img/djsd/jpg
         * name : 哇哈哈
         * type : 0
         */

        private String account;
        private String bankName;
        private String branchName;
        private int id;
        private String imgurl;
        private String name;
        private int type;

        public void setAccount(String account) {
            this.account = account;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public void setId(int id) {
            this.id = id;
        }


        public void setName(String name) {
            this.name = name;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAccount() {
            return account;
        }

        public String getBankName() {
            return bankName;
        }

        public String getBranchName() {
            return branchName;
        }

        public int getId() {
            return id;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getName() {
            return name;
        }

        public int getType() {
            return type;
        }

        @Override
        public String toString() {
            return "PayInfo{" +
                    "account='" + account + '\'' +
                    ", bankName='" + bankName + '\'' +
                    ", branchName='" + branchName + '\'' +
                    ", id=" + id +
                    ", imgurl='" + imgurl + '\'' +
                    ", name='" + name + '\'' +
                    ", type=" + type +
                    '}';
        }
    }
}
