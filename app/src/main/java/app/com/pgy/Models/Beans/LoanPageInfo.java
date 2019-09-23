package app.com.pgy.Models.Beans;

/**
 * Created by YX on 2018/6/4.
 */

public class LoanPageInfo {
    String min;// 最低借款数量 String
    String arrears;// 未还数量 String
    String rate;// 利息 String
    int  deadDay;//还款期限 int
    String max;//最大可借数量 String


    public void setMin(String min) {
        this.min = min;
    }

    public String getMin() {
        return min;
    }

    public void setArrears(String arrears) {
        this.arrears = arrears;
    }

    public String getArrears() {
        return arrears;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }

    public void setDeadDay(int deadDay) {
        this.deadDay = deadDay;
    }

    public int getDeadDay() {
        return deadDay;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMax() {
        return max;
    }
}
