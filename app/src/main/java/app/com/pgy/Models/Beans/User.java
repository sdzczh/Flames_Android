package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.Map;

/**
 * @author xuqingzhong
 * 登录返回内容，用户信息
 */

public class User implements Serializable {


    /**
     * name : 用户姓String
     * token : tokenString
     * phone : 手机号String
     * headImg : 头像地址String
     * orderPwdFlag : false
     * idCheckFlag : false
     * alipayNum : 绑定支付宝账号String
     * "address" : 钱包地址
     */

    private String name;
    private String token;
    private String phone;
    private String headImg;
    private boolean orderPwdFlag;
    private boolean idCheckFlag;
    private String alipayNum;
    private String address;
    private Map<Integer,BindInfoModel> bindInfo;
    private boolean digFlag;
    private String talkToken;
    private int sex;
    private String birthday;
    private int idStatus;
    private int referStatus;

    private String uuid;

    public String getTalkToken() {
        return talkToken;
    }

    public void setTalkToken(String talkToken) {
        this.talkToken = talkToken;
    }

    public boolean isDigFlag() {
        return digFlag;
    }

    public void setDigFlag(boolean digFlag) {
        this.digFlag = digFlag;
    }

    public Map<Integer, BindInfoModel> getBindInfo() {
        return bindInfo;
    }

    public void setBindInfo(Map<Integer, BindInfoModel> bindInfo) {
        this.bindInfo = bindInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public boolean isOrderPwdFlag() {
        return orderPwdFlag;
    }

    public void setOrderPwdFlag(boolean orderPwdFlag) {
        this.orderPwdFlag = orderPwdFlag;
    }

    public boolean isIdCheckFlag() {
        return idCheckFlag;
    }

    public void setIdCheckFlag(boolean idCheckFlag) {
        this.idCheckFlag = idCheckFlag;
    }

    public String getAlipayNum() {
        return alipayNum;
    }

    public void setAlipayNum(String alipayNum) {
        this.alipayNum = alipayNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return sex;
    }

    public String getSexStr(){
        if (sex == 1){
            return "女";
        }
        return "男";
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setReferStatus(int referStatus) {
        this.referStatus = referStatus;
    }

    public int getReferStatus() {
        return referStatus;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", phone='" + phone + '\'' +
                ", headImg='" + headImg + '\'' +
                ", orderPwdFlag=" + orderPwdFlag +
                ", idCheckFlag=" + idCheckFlag +
                ", alipayNum='" + alipayNum + '\'' +
                ", address='" + address + '\'' +
                ", bindInfo=" + bindInfo +
                ", digFlag=" + digFlag +
                ", talkToken='" + talkToken + '\'' +
                ", sex=" + sex +
                ", birthday='" + birthday + '\'' +
                '}';
    }

    public static class BindInfoModel implements Serializable{
        private Integer id;
        private Integer type;
        private String account;
        private String name;
        private String imgurl;
        private String bankName;
        private String branchName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUrl() {
            return imgurl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgurl = imgUrl;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }


    }

}
