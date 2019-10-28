package app.com.pgy.Models.Beans;

/**
 * Create by Android on 2019/10/28 0028
 */
public class BindInfo {

    /**
     * imgurl : 111
     * createtime : 1572256059000
     * name : 111  用户名
     * bankname : 11  	银行名称
     * id : 1
     * state : 1	0未启用 1启用
     * type : 1	0:支付宝,1:微信,2:银行卡
     * updatetime : 1572256059000
     * userid : 2
     * account : 111 收款账号
     * branchname : 111 开户行
     */

    private String imgurl;
    private String createtime;
    private String name;
    private String bankname;
    private String id;
    private int state;
    private int type;
    private String updatetime;
    private String userid;
    private String account;
    private String branchname;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }
}
