package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 配置文件
 * @author xuqingzhong
 */

public class Configuration implements Serializable{

    private String agreenmentUrl;     /*协议*/

    private List<Integer> mortgageList;/*当前抵押挖矿币种*/
    private List<Integer> c2cCoin;/*c2c交易币种*/
    private Map<Integer,CoinInfo> coinInfo; /*币种信息*/
    private String contactusUrl;      /*联系我们*/
    private String guidesUrl;      /*挖矿秘籍*/
    private String mineInfoUrl;      /*矿区介绍*/
    private String digRechargeAmount; //挖火粒充值金额
    private String exchangerateUrl;   /*汇率*/
    private String helpDocUrl;     /*帮助文档*/
    private boolean
            httpsFlag;   /*是否使用https协议*/
    private String inviteUrl;       /*邀请返佣地址*/
    private String loggedShareUrl;     /*分享地址*/
    private int maxWithdrawCount;   /*最高提现次数*/
    private double minRechargeAmount;   /*最低充值金额*/
    private double minWithdrawAmount;   /*最低提现金额*/
    private String notLoggedShareUrl;     /*未登录分享地址*/
    private String activityUrl;//活动（大转盘）URL
    private List<Integer> orderCount;   /*委托列表档位*/
    private List<Integer> rechAndWithCoin;  /*充值提现币种*/
    private List<Integer> yubiCoin;  /*余币宝币种*/
    private String shareDes;    /*分享描述*/
    private String shareTitle;      /*分享标题*/
    private Map<Integer,List<Integer>> spotCoinPair;    /*现货顶部币币交易*/
    private Map<Integer,List<Integer>> spotQueryCoinPair;    /*现货委托查询币币交易*/
    private int versionCode;    /*版本号*/
    private String maxCancelOfMaker; /*C2C交易商家最高取消次数*/
    private String maxCancelOfTaker;  /*C2C交易普通用户最高取消次数*/
    private List<Integer> digCoinType;/*挖矿币种*/
    private Map<Integer,CalculateForceLevel> honorList;    /*算力等级*/
    private List<Integer> redPacketCoin;/*红包币种*/
    private List<Integer> talkTransferCoin;/*转账币种*/
    private String coinIntroUrl;
    private String calculateInstructionUrl;
    private String yubibaoHelpUrl;
    private String withdrawDocUrl;  //提现帮助
    private String rechargeDocUrl;  //充值帮助文档
    private String rateDocUrl;  //费率文档 rateDocUrl
    private String indexUrl;  //官网
    private String dealDigDocUrl;//交易挖矿帮助文档
    private YibiElve yibiElve;
    private List<Integer> dealDigCoinTypes;/*交易挖矿币种币种*/
    private String  c2cHelpDocUrl;

    public YibiElve getYibiElve() {
        return yibiElve;
    }

    public void setYibiElve(YibiElve yibiElve) {
        this.yibiElve = yibiElve;
    }

    public void setDealDigDocUrl(String dealDigDocUrl) {
        this.dealDigDocUrl = dealDigDocUrl;
    }

    public String getDealDigDocUrl() {
        return dealDigDocUrl;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public String getCalculateInstructionUrl() {
        return calculateInstructionUrl;
    }

    public void setCalculateInstructionUrl(String calculateInstructionUrl) {
        this.calculateInstructionUrl = calculateInstructionUrl;
    }

    public void setRateDocUrl(String rateDocUrl) {
        this.rateDocUrl = rateDocUrl;
    }

    public String getRateDocUrl() {
        return rateDocUrl;
    }

    public void setC2cHelpDocUrl(String c2cHelpDocUrl) {
        this.c2cHelpDocUrl = c2cHelpDocUrl;
    }

    public String getC2cHelpDocUrl() {
        return c2cHelpDocUrl;
    }

    public String getYubibaoHelpUrl() {
        return yubibaoHelpUrl;
    }

    public void setYubibaoHelpUrl(String yubibaoHelpUrl) {
        this.yubibaoHelpUrl = yubibaoHelpUrl;
    }

    public String getWithdrawDocUrl() {
        return withdrawDocUrl;
    }

    public void setWithdrawDocUrl(String withdrawDocUrl) {
        this.withdrawDocUrl = withdrawDocUrl;
    }

    public String getRechargeDocUrl() {
        return rechargeDocUrl;
    }

    public void setRechargeDocUrl(String rechargeDocUrl) {
        this.rechargeDocUrl = rechargeDocUrl;
    }

    private Map<Integer,List<Integer>> leverageOrderType;    /*杠杆顶部币币交易*/

    public List<Integer> getRedPacketCoin() {
        return redPacketCoin;
    }


    public String getCoinIntroUrl() {
        return coinIntroUrl;
    }

    public void setCoinIntroUrl(String coinIntroUrl) {
        this.coinIntroUrl = coinIntroUrl;
    }

    public void setRedPacketCoin(List<Integer> redPacketCoin) {
        this.redPacketCoin = redPacketCoin;
    }

    public List<Integer> getYubiCoin() {
        return yubiCoin;
    }

    public void setYubiCoin(List<Integer> yubiCoin) {
        this.yubiCoin = yubiCoin;
    }

    public List<Integer> getTalkTransferCoin() {
        return talkTransferCoin;
    }

    public void setTalkTransferCoin(List<Integer> talkTransferCoin) {
        this.talkTransferCoin = talkTransferCoin;
    }

    public Map<Integer, CalculateForceLevel> getHonorList() {
        return honorList;
    }

    public void setHonorList(Map<Integer, CalculateForceLevel> honorList) {
        this.honorList = honorList;
    }

    public String getMineInfoUrl() {
        return mineInfoUrl;
    }

    public void setMineInfoUrl(String mineInfoUrl) {
        this.mineInfoUrl = mineInfoUrl;
    }

    public String getGuidesUrl() {
        return guidesUrl;
    }

    public void setGuidesUrl(String guidesUrl) {
        this.guidesUrl = guidesUrl;
    }

    public List<Integer> getDigCoinType() {
        return digCoinType;
    }

    public void setDigCoinType(List<Integer> digCoinType) {
        this.digCoinType = digCoinType;
    }

    public void setAgreenmentUrl(String agreenmentUrl) {
        this.agreenmentUrl = agreenmentUrl;
    }

    public void setC2cCoin(List<Integer> c2cCoin) {
        this.c2cCoin = c2cCoin;
    }

    public List<Integer> getMortgageList() {
        return mortgageList;
    }

    public void setMortgageList(List<Integer> mortgageList) {
        this.mortgageList = mortgageList;
    }

    public void setCoinInfo(Map<Integer, CoinInfo> coinInfo) {
        this.coinInfo = coinInfo;
    }

    public void setContactusUrl(String contactusUrl) {
        this.contactusUrl = contactusUrl;
    }

    public void setDigRechargeAmount(String digRechargeAmount) {
        this.digRechargeAmount = digRechargeAmount;
    }

    public void setExchangerateUrl(String exchangerateUrl) {
        this.exchangerateUrl = exchangerateUrl;
    }

    public void setHelpDocUrl(String helpDocUrl) {
        this.helpDocUrl = helpDocUrl;
    }

    public void setHttpsFlag(boolean httpsFlag) {
        this.httpsFlag = httpsFlag;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }

    public void setLoggedShareUrl(String loggedShareUrl) {
        this.loggedShareUrl = loggedShareUrl;
    }

    public void setMaxWithdrawCount(int maxWithdrawCount) {
        this.maxWithdrawCount = maxWithdrawCount;
    }

    public void setMinRechargeAmount(double minRechargeAmount) {
        this.minRechargeAmount = minRechargeAmount;
    }

    public void setMinWithdrawAmount(double minWithdrawAmount) {
        this.minWithdrawAmount = minWithdrawAmount;
    }

    public void setNotLoggedShareUrl(String notLoggedShareUrl) {
        this.notLoggedShareUrl = notLoggedShareUrl;
    }

    public void setOrderCount(List<Integer> orderCount) {
        this.orderCount = orderCount;
    }


    public void setRechAndWithCoin(List<Integer> rechAndWithCoin) {
        this.rechAndWithCoin = rechAndWithCoin;
    }

    public void setShareDes(String shareDes) {
        this.shareDes = shareDes;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public void setSpotCoinPair(Map<Integer, List<Integer>> spotCoinPair) {
        this.spotCoinPair = spotCoinPair;
    }

    public void setSpotQueryCoinPair(Map<Integer, List<Integer>> spotQueryCoinPair) {
        this.spotQueryCoinPair = spotQueryCoinPair;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setMaxCancelOfMaker(String maxCancelOfMaker) {
        this.maxCancelOfMaker = maxCancelOfMaker;
    }

    public void setMaxCancelOfTaker(String maxCancelOfTaker) {
        this.maxCancelOfTaker = maxCancelOfTaker;
    }

    public void setLeverageOrderType(Map<Integer, List<Integer>> leverageOrderType) {
        this.leverageOrderType = leverageOrderType;
    }

    public String getMaxCancelOfMaker() {
        return maxCancelOfMaker;
    }

    public String getMaxCancelOfTaker() {
        return maxCancelOfTaker;
    }

    public String getAgreenmentUrl() {
        return agreenmentUrl;
    }

    public List<Integer> getC2cCoin() {
        return c2cCoin;
    }

    public Map<Integer, CoinInfo> getCoinInfo() {
        return coinInfo;
    }

    public String getContactusUrl() {
        return contactusUrl;
    }

    public String getDigRechargeAmount() {
        return digRechargeAmount;
    }

    public String getExchangerateUrl() {
        return exchangerateUrl;
    }

    public String getHelpDocUrl() {
        return helpDocUrl;
    }

    public boolean isHttpsFlag() {
        return httpsFlag;
    }

    public String getInviteUrl() {
        return inviteUrl;
    }

    public String getLoggedShareUrl() {
        return loggedShareUrl;
    }

    public int getMaxWithdrawCount() {
        return maxWithdrawCount;
    }

    public double getMinRechargeAmount() {
        return minRechargeAmount;
    }

    public double getMinWithdrawAmount() {
        return minWithdrawAmount;
    }

    public String getNotLoggedShareUrl() {
        return notLoggedShareUrl;
    }

    public List<Integer> getOrderCount() {
        return orderCount;
    }


    public String getShareDes() {
        return shareDes;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public Map<Integer, List<Integer>> getSpotCoinPair() {
        return spotCoinPair;
    }

    public Map<Integer, List<Integer>> getSpotQueryCoinPair() {
        return spotQueryCoinPair;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public List<Integer> getRechAndWithCoin() {
        return rechAndWithCoin;
    }

    public Map<Integer, List<Integer>> getLeverageOrderType() {
        return leverageOrderType;
    }

    public void setDealDigCoinTypes(List<Integer> dealDigCoinTypes) {
        this.dealDigCoinTypes = dealDigCoinTypes;
    }

    public List<Integer> getDealDigCoinTypes() {
        return dealDigCoinTypes;
    }

    public static class CoinInfo implements Serializable{

        private String description;
        private String imgurl;
        private String coinname;
        private int cointype;
        private int id;
        private String cnname;
        private int calculScale;    //资金划转小数位数
        private int withdrawScale;    //提现小数位数
        private String withdrawNum;     //提现最低金额
        private int c2cNumScale;        //法币数量小数位数
        private int c2cPriceScale;        //法币价格小数位数
        private int yubiScale;          //余币宝计算小数位数
        private String minC2cTransNum;      //最低法币交易金额;


        public int getC2cPriceScale() {
            return c2cPriceScale;
        }

        public void setC2cPriceScale(int c2cPriceScale) {
            this.c2cPriceScale = c2cPriceScale;
        }

        public int getCalculScale() {
            return calculScale;
        }

        public void setCalculScale(int calculScale) {
            this.calculScale = calculScale;
        }

        public int getWithdrawScale() {
            return withdrawScale;
        }

        public void setWithdrawScale(int withdrawScale) {
            this.withdrawScale = withdrawScale;
        }

        public String getWithdrawNum() {
            return withdrawNum;
        }

        public void setWithdrawNum(String withdrawNum) {
            this.withdrawNum = withdrawNum;
        }

        public int getC2cNumScale() {
            return c2cNumScale;
        }

        public void setC2cNumScale(int c2cNumScale) {
            this.c2cNumScale = c2cNumScale;
        }

        public int getYubiScale() {
            return yubiScale;
        }

        public void setYubiScale(int yubiScale) {
            this.yubiScale = yubiScale;
        }

        public String getMinC2cTransNum() {
            return minC2cTransNum;
        }

        public void setMinC2cTransNum(String minC2cTransNum) {
            this.minC2cTransNum = minC2cTransNum;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getCoinname() {
            return coinname;
        }

        public void setCoinname(String coinname) {
            this.coinname = coinname;
        }

        public int getCointype() {
            return cointype;
        }

        public void setCointype(int cointype) {
            this.cointype = cointype;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCnname() {
            return cnname;
        }

        public void setCnname(String cnname) {
            this.cnname = cnname;
        }



        @Override
        public String toString() {
            return "CoinInfo{" +
                    "description='" + description + '\'' +
                    ", imgurl='" + imgurl + '\'' +
                    ", coinname='" + coinname + '\'' +
                    ", cointype=" + cointype +
                    ", id=" + id +
                    ", cnname='" + cnname + '\'' +
                    ", calculScale=" + calculScale +
                    ", withdrawScale=" + withdrawScale +
                    ", withdrawNum='" + withdrawNum + '\'' +
                    ", c2cNumScale=" + c2cNumScale +
                    ", c2cPriceScale=" + c2cPriceScale +
                    ", yubiScale=" + yubiScale +
                    ", minC2cTransNum='" + minC2cTransNum + '\'' +
                    '}';
        }
    }

    public static class YibiElve{

        /**
         * name : COIN精灵
         * phone : 15865711060
         * headPath : http://img.huolicoin.com/fai/060/15865711060/201807300907566672.png
         */

        private String name;
        private String phone;
        private String headPath;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getHeadPath() {
            return headPath;
        }

        public void setHeadPath(String headPath) {
            this.headPath = headPath;
        }
    }

    public static class CalculateForceLevel {
        private String cointype;
        private long createtime;
        private String rolepicurl;
        private int soulmaxforce;
        private int rolegrade;
        private String rolename;
        private int soulminforce;
        private String minename;
        private String minepicurl;
        private int id;
        private long updatetime;
        private List<Integer> mineCoin;

        public String getCointype() {
            return cointype;
        }

        public void setCointype(String cointype) {
            this.cointype = cointype;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getRolepicurl() {
            return rolepicurl;
        }

        public void setRolepicurl(String rolepicurl) {
            this.rolepicurl = rolepicurl;
        }

        public int getSoulmaxforce() {
            return soulmaxforce;
        }

        public void setSoulmaxforce(int soulmaxforce) {
            this.soulmaxforce = soulmaxforce;
        }

        public int getRolegrade() {
            return rolegrade;
        }

        public void setRolegrade(int rolegrade) {
            this.rolegrade = rolegrade;
        }

        public String getRolename() {
            return rolename;
        }

        public void setRolename(String rolename) {
            this.rolename = rolename;
        }

        public int getSoulminforce() {
            return soulminforce;
        }

        public void setSoulminforce(int soulminforce) {
            this.soulminforce = soulminforce;
        }

        public String getMinename() {
            return minename;
        }

        public void setMinename(String minename) {
            this.minename = minename;
        }

        public String getMinepicurl() {
            return minepicurl;
        }

        public void setMinepicurl(String minepicurl) {
            this.minepicurl = minepicurl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(long updatetime) {
            this.updatetime = updatetime;
        }

        public List<Integer> getMineCoin() {
            return mineCoin;
        }

        public void setMineCoin(List<Integer> mineCoin) {
            this.mineCoin = mineCoin;
        }
    }
}
