package app.com.pgy.Models.Beans;

import java.util.List;

/**
 * Created by YX on 2018/7/20.
 */

public class HomeInfo {


    /**
     * total : 536.60
     * soptAccount : 128.00
     * banner : [{"address":"wallet","imgpath":"","title":"","type":1},{"address":"baidu.com","imgpath":"http","title":"新浪","type":0}]
     * yubiAccount : 0.00
     * leverAccount : 0.00
     * c2cAccount : 408.60
     * notice : {"id":2,"title":"标题2"}
     */

    private String total;
    private String soptAccount;
    private String yubiAccount;
    private String leverAccount;
    private String c2cAccount;
    private NoticeBean notice;
    private List<BannerInfo> banner;
    private String noticeUrl;
    private String noticeTitle;
    private MoodBean mood;
    private String accountBalanceCny;//总资产折合RMB价格
    private String accountBalance;

    private List<HomeMarketBean> marketMain;
    private List<HomeMarketBean> market24h;
    private List<HomeNewBean> newsList;

    private String accountGuide;//账户指南
    private String buyCoinGuide;//购买指南

    public void setMarketMain(List<HomeMarketBean> marketMain) {
        this.marketMain = marketMain;
    }

    public List<HomeMarketBean> getMarketMain() {
        return marketMain;
    }

    public void setMarket24h(List<HomeMarketBean> market24h) {
        this.market24h = market24h;
    }

    public List<HomeMarketBean> getMarket24h() {
        return market24h;
    }

    public void setNewsList(List<HomeNewBean> newsList) {
        this.newsList = newsList;
    }

    public List<HomeNewBean> getNewsList() {
        return newsList;
    }

    public void setAccountGuide(String accountGuide) {
        this.accountGuide = accountGuide;
    }

    public String getAccountGuide() {
        return accountGuide;
    }

    public void setBuyCoinGuide(String buyCoinGuide) {
        this.buyCoinGuide = buyCoinGuide;
    }

    public String getBuyCoinGuide() {
        return buyCoinGuide;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSoptAccount() {
        return soptAccount;
    }

    public void setSoptAccount(String soptAccount) {
        this.soptAccount = soptAccount;
    }

    public String getYubiAccount() {
        return yubiAccount;
    }

    public void setYubiAccount(String yubiAccount) {
        this.yubiAccount = yubiAccount;
    }

    public String getLeverAccount() {
        return leverAccount;
    }

    public void setLeverAccount(String leverAccount) {
        this.leverAccount = leverAccount;
    }

    public String getC2cAccount() {
        return c2cAccount;
    }

    public void setC2cAccount(String c2cAccount) {
        this.c2cAccount = c2cAccount;
    }

    public NoticeBean getNotice() {
        return notice;
    }

    public void setNotice(NoticeBean notice) {
        this.notice = notice;
    }

    public List<BannerInfo> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerInfo> banner) {
        this.banner = banner;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setMood(MoodBean mood) {
        this.mood = mood;
    }

    public MoodBean getMood() {
        return mood;
    }

    public void setAccountBalanceCny(String accountBalanceCny) {
        this.accountBalanceCny = accountBalanceCny;
    }

    public String getAccountBalanceCny() {
        return accountBalanceCny;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public static class NoticeBean {
        /**
         * id : 2
         * title : 标题2
         */

        private int id;
        private String title;
        private String address = "http://api.huolicoin.com/orderapi/web/article/";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address+id+".action";
        }
    }

    public static class MoodBean {
       String moodTop;
       String moodBottom;

        public void setMoodTop(String moodTop) {
            this.moodTop = moodTop;
        }

        public String getMoodTop() {
            return moodTop;
        }

        public void setMoodBottom(String moodBottom) {
            this.moodBottom = moodBottom;
        }

        public String getMoodBottom() {
            return moodBottom;
        }
    }

}
