package app.com.pgy.Models.Beans;

import java.util.List;

/**
 * 算力流水
 * @author xuqingzhong
 */

public class CalculateDetail {
    /**
     * list : [{"allCalculForce":5,"calculForce":1,"createTime":"2018-04-11 16: 04: 04","id":2,"type":"签到"}]
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
         * allCalculForce : 5
         * calculForce : 1
         * createTime : 2018-04-11 16: 04: 04
         * id : 2
         * type : 签到
         */

        private int allCalculForce;
        private int calculForce;
        private String createTime;
        private int id;
        private String type;

        public void setAllCalculForce(int allCalculForce) {
            this.allCalculForce = allCalculForce;
        }

        public void setCalculForce(int calculForce) {
            this.calculForce = calculForce;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getAllCalculForce() {
            return allCalculForce;
        }

        public int getCalculForce() {
            return calculForce;
        }

        public String getCreateTime() {
            return createTime;
        }

        public int getId() {
            return id;
        }

        public String getType() {
            return type;
        }
    }
}
