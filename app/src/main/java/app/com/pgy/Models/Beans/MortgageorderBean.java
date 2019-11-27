package app.com.pgy.Models.Beans;

import java.util.List;

public class MortgageorderBean {
    /**
     * data : [{"amount":400,"startTime":"2019-11-23","endTime":"2020-02-29","rate":0.02,"percentage":0.0206},{"amount":100,"startTime":"2019-11-23","endTime":"2019-11-29","rate":2,"percentage":0.4}]
     * msg : 成功
     * code : 10000
     */

        /**
         * amount : 400.0
         * startTime : 2019-11-23
         * endTime : 2020-02-29
         * rate : 0.02
         * percentage : 0.0206
         */

        private double amount;
        private String startTime;
        private String endTime;
        private double rate;
        private double percentage;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }

}
