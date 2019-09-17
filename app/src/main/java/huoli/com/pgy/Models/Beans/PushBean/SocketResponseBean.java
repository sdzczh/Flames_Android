package huoli.com.pgy.Models.Beans.PushBean;

import java.io.Serializable;

/**
 * @author xuqingzhong
 * 长连接返回对象
 */

public class SocketResponseBean implements Serializable{

    /**
     * type : -3002
     * info : 身份认证失败!
     * code : 111111
     */

    private String info;
    private int code;
    private String msg;
    private String cmsgCode;
    /**
     * scene : 3511
     */

    private int scene;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCmsgCode(String cmsgCode) {
        this.cmsgCode = cmsgCode;

    }

    public String getCmsgCode() {
        return cmsgCode;
    }

    public int getScene() {
        return scene;
    }

    public void setScene(int scene) {
        this.scene = scene;
    }
}
