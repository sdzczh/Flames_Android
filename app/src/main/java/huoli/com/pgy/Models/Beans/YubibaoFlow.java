package huoli.com.pgy.Models.Beans;

import java.util.List;

/**
 * @description 余币宝流水详情
 */

public class YubibaoFlow {


    /**
     * availBalance : 50
     * totalProfit : 0
     * annualRate : 15.67
     * lastProfit : 0
     * list : [{"amount":50,"createtime":1532435432000,"opertype":"转入","time":"07-24 20:30","resultAmount":"50"}]
     * forecastProfit : 1567
     * availBalanceOfCny : 50
     */

    private String availBalance;// 总金额
    private String totalProfit;// 累计收益
    private String annualRate;//年化收益率
    private String lastProfit;//昨日收益
    private String forecastProfit;//万份收益
    private String availBalanceOfCny;//总金额折合人民币
    private List<ListBean> list;//流水记录

    public String getAvailBalance() {
        return availBalance;
    }

    public void setAvailBalance(String availBalance) {
        this.availBalance = availBalance;
    }

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
    }

    public String getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(String annualRate) {
        this.annualRate = annualRate;
    }

    public String getLastProfit() {
        return lastProfit;
    }

    public void setLastProfit(String lastProfit) {
        this.lastProfit = lastProfit;
    }

    public String getForecastProfit() {
        return forecastProfit;
    }

    public void setForecastProfit(String forecastProfit) {
        this.forecastProfit = forecastProfit;
    }

    public String getAvailBalanceOfCny() {
        return availBalanceOfCny;
    }

    public void setAvailBalanceOfCny(String availBalanceOfCny) {
        this.availBalanceOfCny = availBalanceOfCny;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * createtime : 1532435432000
         * opertype : 转入
         * time : 07-24 20:30
         * resultAmount : 50
         */

        private long createtime;
        private String opertype;// 操作类型
        private String time;//时间
        private String resultAmount;//操作金额


        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getOpertype() {
            return opertype;
        }

        public void setOpertype(String opertype) {
            this.opertype = opertype;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getResultAmount() {
            return resultAmount;
        }

        public void setResultAmount(String resultAmount) {
            this.resultAmount = resultAmount;
        }
    }
}

