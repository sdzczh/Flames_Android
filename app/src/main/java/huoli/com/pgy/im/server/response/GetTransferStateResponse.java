package huoli.com.pgy.im.server.response;

import java.io.Serializable;

/**
 * 创建日期：2018/5/21 0021 on 下午 4:10
 * 描述:获取转账详情
 *
 * @author 徐庆重
 */

public class GetTransferStateResponse implements Serializable{

    private String amount;
    private String amtOfCny;
    private int coinType;
    private String name;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmtOfCny() {
        return amtOfCny;
    }

    public void setAmtOfCny(String amtOfCny) {
        this.amtOfCny = amtOfCny;
    }

    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
