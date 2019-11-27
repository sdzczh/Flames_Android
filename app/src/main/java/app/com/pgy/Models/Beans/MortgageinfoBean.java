package app.com.pgy.Models.Beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class MortgageinfoBean {
    /**
     * date : 2019-11-26
     * amount : 7016.16
     * agreement : https://lanhuapp.com
     * rate : {"1000":0.02,"2000":0.03,"5000":0.05}
     * cycle : [1000,2000,5000]
     */

    private String date;
    private double amount;
    private String agreement;


    //        private RateBean rate;
    private Map<Integer, Double> rate;
    private List<Integer> cycle;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public Map<Integer, Double> getRate() {
        return rate;
    }

    public void setRate(Map<Integer, Double> rate) {
        this.rate = rate;
    }

//        public RateBean getRate() {
//            return rate;
//        }
//
//        public void setRate(RateBean rate) {
//            this.rate = rate;
//        }

    public List<Integer> getCycle() {
        return cycle;
    }

    public void setCycle(List<Integer> cycle) {
        this.cycle = cycle;
    }

//        public static class RateBean {
//            /**
//             * 1000 : 0.02
//             * 2000 : 0.03
//             * 5000 : 0.05
//             */
//
//            @SerializedName("1000")
//            private double _$1000;
//            @SerializedName("2000")
//            private double _$2000;
//            @SerializedName("5000")
//            private double _$5000;
//
//            public double get_$1000() {
//                return _$1000;
//            }
//
//            public void set_$1000(double _$1000) {
//                this._$1000 = _$1000;
//            }
//
//            public double get_$2000() {
//                return _$2000;
//            }
//
//            public void set_$2000(double _$2000) {
//                this._$2000 = _$2000;
//            }
//
//            public double get_$5000() {
//                return _$5000;
//            }
//
//            public void set_$5000(double _$5000) {
//                this._$5000 = _$5000;
//            }
//        }

}
