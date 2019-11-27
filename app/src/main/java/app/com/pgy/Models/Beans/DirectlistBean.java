package app.com.pgy.Models.Beans;

import java.util.List;

public class DirectlistBean {

    /**
     * data : [{"nickname":"2222","createtime":"2019-09-20 11:24:42","id":2,"referenceStatus":0,"phone":"13165373280","idstatus":2},{"nickname":"2222","createtime":"2019-09-20 11:24:42","id":2,"referenceStatus":0,"phone":"13165373280","idstatus":2},{"nickname":"2222","createtime":"2019-09-20 11:24:42","id":2,"referenceStatus":0,"phone":"13165373280","idstatus":2},{"nickname":"2222","createtime":"2019-09-20 11:24:42","id":2,"referenceStatus":0,"phone":"13165373280","idstatus":2},{"nickname":"2222","createtime":"2019-09-20 11:24:42","id":2,"referenceStatus":0,"phone":"13165373280","idstatus":2},{"nickname":"2222","createtime":"2019-09-20 11:24:42","id":2,"referenceStatus":0,"phone":"13165373280","idstatus":2},{"nickname":"2222","createtime":"2019-09-20 11:24:42","id":2,"referenceStatus":0,"phone":"13165373280","idstatus":2},{"nickname":"2222","createtime":"2019-09-20 11:24:42","id":2,"referenceStatus":0,"phone":"13165373280","idstatus":2},{"nickname":"2222","createtime":"2019-09-20 11:24:42","id":2,"referenceStatus":0,"phone":"13165373280","idstatus":2},{"nickname":"2222","createtime":"2019-09-20 11:24:42","id":2,"referenceStatus":0,"phone":"13165373280","idstatus":2}]
     * msg : 成功
     * code : 10000
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * nickname : 2222
         * createtime : 2019-09-20 11:24:42
         * id : 2
         * referenceStatus : 0
         * phone : 13165373280
         * idstatus : 2
         */

        private String nickname;
        private String createtime;
        private int id;
        private int referenceStatus;
        private String phone;
        private int idstatus;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getReferenceStatus() {
            return referenceStatus;
        }

        public void setReferenceStatus(int referenceStatus) {
            this.referenceStatus = referenceStatus;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getIdstatus() {
            return idstatus;
        }

        public void setIdstatus(int idstatus) {
            this.idstatus = idstatus;
        }
    }
}
