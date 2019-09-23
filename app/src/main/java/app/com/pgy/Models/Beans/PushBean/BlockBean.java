package app.com.pgy.Models.Beans.PushBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/26 0026.
 * 挖矿顶部区块，长连接接收一个，api接口接收一个list
 *
 * @author xuqingzhong
 */

public class BlockBean implements Serializable {

    /**
     * blockCode : 2322
     * createTime : 21:31
     * text : 已出矿
     */

    private int blockCode;
    private String createTime;
    private String text;

    public BlockBean() {
    }

    public BlockBean(int blockCode) {
        this.blockCode = blockCode;
    }

    public BlockBean(int blockCode, String createTime, String text) {
        this.blockCode = blockCode;
        this.createTime = createTime;
        this.text = text;
    }

    public int getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(int blockCode) {
        this.blockCode = blockCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
