package huoli.com.pgy.Models.Beans.PushBean;

import java.io.Serializable;

/**
 * Socket请求对象
 * @author xuqingzhong
 */

public class SocketRequestBean implements Serializable{

    /**
     * action : init
     * cmsg_code : 111111
     * data : {"token":"38dac1d5921e4ae0be1f81d84627ed12"}
     */

    private String action;
    private String cmsg_code;
    private Object data;

    public SocketRequestBean() {
    }

    public SocketRequestBean(String action) {
        this.action = action;
    }

    public SocketRequestBean(String action, String cmsg_code) {
        this.action = action;
        this.cmsg_code = cmsg_code;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setCmsg_code(String cmsg_code) {
        this.cmsg_code = cmsg_code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public String getCmsg_code() {
        return cmsg_code;
    }

    @Override
    public String toString() {
        return "SocketRequestBean{" +
                "action='" + action + '\'' +
                ", cmsg_code=" + cmsg_code +
                ", data=" + data +
                '}';
    }
}
