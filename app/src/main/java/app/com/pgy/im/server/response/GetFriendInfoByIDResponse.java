package app.com.pgy.im.server.response;

import java.io.Serializable;

/**
 * Created by wangmingqiang on 16/9/11.
 * Company RongCloud
 */

public class GetFriendInfoByIDResponse implements Serializable{
    private int id;
    private int sex;
    private String phone;
    private String nickName;
    private String roleName;
    private String headPath;
    private String remarkName;
    /**
     * isFriend : true
     */

    private boolean isFriend;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public boolean isIsFriend() {
        return isFriend;
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }
}
