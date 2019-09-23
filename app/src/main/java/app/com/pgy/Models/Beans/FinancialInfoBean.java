package app.com.pgy.Models.Beans;

/**
 * Created by YX on 2018/6/11.
 */

public class FinancialInfoBean {
     String sum;// "1227632.795", 账户总额 String
    String lev;// "12155", 杠杆账户总额 String
    String yubi;// "0", 余币宝账户总额 String
    String spot;// "12623.614", 现货账户总额 String
    String c2c;// "1202854.181" c2c账户总额 String

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getSum() {
        return sum;
    }

    public void setLev(String lev) {
        this.lev = lev;
    }

    public String getLev() {
        return lev;
    }

    public void setYubi(String yubi) {
        this.yubi = yubi;
    }

    public String getYubi() {
        return yubi;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public String getSpot() {
        return spot;
    }

    public void setC2c(String c2c) {
        this.c2c = c2c;
    }

    public String getC2c() {
        return c2c;
    }
}
