package app.com.pgy.Models.Beans;

import java.util.List;

public class MyteamBean {
    /**
     * data : {"directCount":3,"childCount":7,"teamDigProfit":"1.60","records":[{"nickname":"2222","amount":1.5,"reference_status":0,"id":5,"createTime":"2019-11-07 17:38:19","idstatus":2},{"nickname":"2222","amount":2,"reference_status":0,"id":4,"createTime":"2019-11-07 17:38:06","idstatus":2},{"nickname":"2222","amount":3,"reference_status":0,"id":3,"createTime":"2019-11-07 17:38:04","idstatus":2},{"nickname":"2222","amount":0.1,"reference_status":0,"id":2,"createTime":"2019-11-07 17:38:03","idstatus":2},{"nickname":"2222","amount":1,"reference_status":0,"id":1,"createTime":"2019-11-07 17:38:01","idstatus":2}],"personDigProfit":"6.00"}
     * msg : 成功
     * code : 10000
     */

    private DataBean data;
    private String msg;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * directCount : 3
         * childCount : 7
         * teamDigProfit : 1.60
         * records : [{"nickname":"2222","amount":1.5,"reference_status":0,"id":5,"createTime":"2019-11-07 17:38:19","idstatus":2},{"nickname":"2222","amount":2,"reference_status":0,"id":4,"createTime":"2019-11-07 17:38:06","idstatus":2},{"nickname":"2222","amount":3,"reference_status":0,"id":3,"createTime":"2019-11-07 17:38:04","idstatus":2},{"nickname":"2222","amount":0.1,"reference_status":0,"id":2,"createTime":"2019-11-07 17:38:03","idstatus":2},{"nickname":"2222","amount":1,"reference_status":0,"id":1,"createTime":"2019-11-07 17:38:01","idstatus":2}]
         * personDigProfit : 6.00
         */

        private int directCount;
        private int childCount;
        private String teamDigProfit;
        private String personDigProfit;
        private List<RecordsBean> records;

        public int getDirectCount() {
            return directCount;
        }

        public void setDirectCount(int directCount) {
            this.directCount = directCount;
        }

        public int getChildCount() {
            return childCount;
        }

        public void setChildCount(int childCount) {
            this.childCount = childCount;
        }

        public String getTeamDigProfit() {
            return teamDigProfit;
        }

        public void setTeamDigProfit(String teamDigProfit) {
            this.teamDigProfit = teamDigProfit;
        }

        public String getPersonDigProfit() {
            return personDigProfit;
        }

        public void setPersonDigProfit(String personDigProfit) {
            this.personDigProfit = personDigProfit;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean {
            /**
             * nickname : 2222
             * amount : 1.5
             * reference_status : 0
             * id : 5
             * createTime : 2019-11-07 17:38:19
             * idstatus : 2
             */

            private String nickname;
            private double amount;
            private int reference_status;
            private int id;
            private String createTime;
            private int idstatus;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public int getReference_status() {
                return reference_status;
            }

            public void setReference_status(int reference_status) {
                this.reference_status = reference_status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getIdstatus() {
                return idstatus;
            }

            public void setIdstatus(int idstatus) {
                this.idstatus = idstatus;
            }
        }
    }
}
