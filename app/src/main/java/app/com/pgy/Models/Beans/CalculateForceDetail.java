package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * 算力详情，当前算力、排名、登录获取算力、算力排行榜
 * @author xuqingzhong
 */

public class CalculateForceDetail{

    /**
     * instruUrl  算力说明文档地址
     * currentForce : 0
     * dayFinished : false
     * days : 0
     * monthFinished : false
     * rank : 1
     * tenFinished : false
     * userForces : [{"account":"135****0001","forceNumber":1,"id":1,"rank":0}]
     */

    private String instruUrl;
    private int currentForce;
    private boolean dayFinished;
    private int days;
    private boolean monthFinished;
    private int rank;
    private boolean tenFinished;
    private List<UserForcesBean> userForces;

    public String getInstruUrl() {
        return instruUrl;
    }


    public void setInstruUrl(String instruUrl) {
        this.instruUrl = instruUrl;
    }

    public void setCurrentForce(int currentForce) {
        this.currentForce = currentForce;
    }

    public void setDayFinished(boolean dayFinished) {
        this.dayFinished = dayFinished;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setMonthFinished(boolean monthFinished) {
        this.monthFinished = monthFinished;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setTenFinished(boolean tenFinished) {
        this.tenFinished = tenFinished;
    }

    public void setUserForces(List<UserForcesBean> userForces) {
        this.userForces = userForces;
    }

    public int getCurrentForce() {
        return currentForce;
    }

    public boolean getDayFinished() {
        return dayFinished;
    }

    public int getDays() {
        return days;
    }

    public boolean getMonthFinished() {
        return monthFinished;
    }

    public int getRank() {
        return rank;
    }

    public boolean getTenFinished() {
        return tenFinished;
    }

    public List<UserForcesBean> getUserForces() {
        return userForces;
    }

    public static class UserForcesBean implements Serializable{
        /**
         * account : 135****0001
         * forceNumber : 1
         * id : 1
         * rank : 0
         */

        private String account;
        private int forceNumber;
        private int id;
        private int rank;
        private String honorName;

        public String getHonorName() {
            return honorName;
        }

        public void setHonorName(String honorName) {
            this.honorName = honorName;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public void setForceNumber(int forceNumber) {
            this.forceNumber = forceNumber;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getAccount() {
            return account;
        }

        public int getForceNumber() {
            return forceNumber;
        }

        public int getId() {
            return id;
        }

        public int getRank() {
            return rank;
        }
    }
}
