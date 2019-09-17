package huoli.com.pgy.Models.Beans;

import java.util.List;

/**
 * 创建日期：2018/7/24 0024 on 上午 11:31
 * 描述:
 *
 * @author xu
 */

public class C2CPersonalMessage {

    /**
     * buyList : [{"coinType":0,"amount":"56","payType":4,"price":"1","totalMax":"200","totalMin":"1","id":39}]
     * totalNum : 73
     * phone : 15865711061
     * registerTime : 2018-07-25
     * nickName : COIN061
     * saleList : [{"coinType":0,"amount":"146","payType":7,"price":"0.99","totalMax":"210","totalMin":"1","id":13}]
     * realNameAuthFlag : true
     * spotNum : 51
     * headPath : http://img.huolicoin.com/fai/061/15865711061/201807310855029516.png
     * phoneAuthFlag : true
     * c2cNum : 22
     */

    private int totalNum;
    private String phone;
    private String registerTime;
    private String nickName;
    private boolean realNameAuthFlag;
    private int spotNum;
    private String headPath;
    private boolean phoneAuthFlag;
    private int c2cNum;
    private List<ListBean> buyList;
    private List<ListBean> saleList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isRealNameAuthFlag() {
        return realNameAuthFlag;
    }

    public void setRealNameAuthFlag(boolean realNameAuthFlag) {
        this.realNameAuthFlag = realNameAuthFlag;
    }

    public int getSpotNum() {
        return spotNum;
    }

    public void setSpotNum(int spotNum) {
        this.spotNum = spotNum;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public boolean isPhoneAuthFlag() {
        return phoneAuthFlag;
    }

    public void setPhoneAuthFlag(boolean phoneAuthFlag) {
        this.phoneAuthFlag = phoneAuthFlag;
    }

    public int getC2cNum() {
        return c2cNum;
    }

    public void setC2cNum(int c2cNum) {
        this.c2cNum = c2cNum;
    }

    public List<ListBean> getBuyList() {
        return buyList;
    }

    public void setBuyList(List<ListBean> buyList) {
        this.buyList = buyList;
    }

    public List<ListBean> getSaleList() {
        return saleList;
    }

    public void setSaleList(List<ListBean> saleList) {
        this.saleList = saleList;
    }

    public static class ListBean {
        /**
         * coinType : 0
         * amount : 146
         * payType : 7
         * price : 0.99
         * totalMax : 210
         * totalMin : 1
         * id : 13
         */

        private int coinType;
        private String amount;
        private int payType;
        private String price;
        private String totalMax;
        private String totalMin;
        private int id;

        public int getCoinType() {
            return coinType;
        }

        public void setCoinType(int coinType) {
            this.coinType = coinType;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTotalMax() {
            return totalMax;
        }

        public void setTotalMax(String totalMax) {
            this.totalMax = totalMax;
        }

        public String getTotalMin() {
            return totalMin;
        }

        public void setTotalMin(String totalMin) {
            this.totalMin = totalMin;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
