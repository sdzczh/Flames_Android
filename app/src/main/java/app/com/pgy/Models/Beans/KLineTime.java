package app.com.pgy.Models.Beans;

import java.io.Serializable;

/**
 * 创建日期：2018/6/28 0028 on 下午 3:43
 * 描述:
 *
 * @author xu
 */

public class KLineTime implements Serializable{
    private int time;
    private String txt;
    private int delaySeconds;
    private boolean isSelect;

    public KLineTime() {
    }

    public KLineTime(int time, String txt, int delayseonds) {
        this.time = time;
        this.txt = txt;
        this.delaySeconds = delayseonds;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }
}
