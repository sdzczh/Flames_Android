package huoli.com.pgy.Models.Beans;

import java.util.List;

/**
 * *  @Description:描述
 * *  @Author: EDZ
 * *  @CreateDate: 2019/7/22 13:54
 */
public class OrdinRewardInfo {

    /**
     * allUnion : 累计获得ECN
     * allOrder : 累计获得ODIN
     * thisOrder : 本期获得ODIN
     * thisUnion : 本期获得ECN
     * topList : [{"phone":"手机号","rank":"排名","amount":"数量"}]
     * rank : 我的排名
     * referPhone : 推荐人手机号
     * referCode : 我的邀请码
     */

    private String allUnion;
    private String allOrder;
    private String thisOrder;
    private String thisUnion;
    private String rank;
    private String referPhone;
    private String referCode;
    private List<TopListBean> topList;
    private String docUrl;
    private String qrCode;

    public String getAllUnion() {
        return allUnion;
    }

    public void setAllUnion(String allUnion) {
        this.allUnion = allUnion;
    }

    public String getAllOrder() {
        return allOrder;
    }

    public void setAllOrder(String allOrder) {
        this.allOrder = allOrder;
    }

    public String getThisOrder() {
        return thisOrder;
    }

    public void setThisOrder(String thisOrder) {
        this.thisOrder = thisOrder;
    }

    public String getThisUnion() {
        return thisUnion;
    }

    public void setThisUnion(String thisUnion) {
        this.thisUnion = thisUnion;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getReferPhone() {
        return referPhone;
    }

    public void setReferPhone(String referPhone) {
        this.referPhone = referPhone;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public List<TopListBean> getTopList() {
        return topList;
    }

    public void setTopList(List<TopListBean> topList) {
        this.topList = topList;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public static class TopListBean {
        /**
         * phone : 手机号
         * rank : 排名
         * amount : 数量
         */

        private String phone;
        private String rank;
        private String amount;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
