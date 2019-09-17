package huoli.com.pgy.Models;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: 包装api接口对象
 * @author 徐庆重
 */

public class ResultBean<T> {

    /**
     * code : 10000
     * data : {"codeId":9}
     * msg : 成功
     */

    private int code;
    private T data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
