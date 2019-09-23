package app.com.pgy.Models.Beans;

import java.util.List;

/**
 * @description 银行卡
 * @author xuqingzhong
 */

public class BankCard {

    /**
     * list : [{"createTime":1518142836000,"id":1,"name":"中国银行","operId":1,"state":1,"updateTime":1519719617000},{"createTime":1518146133000,"id":2,"name":"中国农业银行","operId":1,"state":1,"updateTime":1519719619000}]
     */

    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean {
        /**
         * createTime : 1518142836000
         * id : 1
         * name : 中国银行
         * operId : 1
         * state : 1
         * updateTime : 1519719617000
         */

        private long createTime;
        private int id;
        private String name;

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCreateTime() {
            return createTime;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }
}
