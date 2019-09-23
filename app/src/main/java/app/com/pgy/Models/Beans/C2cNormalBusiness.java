package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * 普通用户商家
 * @author xuqingzhong
 */

public class C2cNormalBusiness {


    /**
     * list : [{"headPath":"http://image.huolicoin.com/sdsd232.jpg","id":2,"payType":7,"price":"10","quantity":1,"remain":"22","totalMax":"50","totalMin":"5","userName":"13500010001"}]
     */

    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean implements Serializable{
        /**
         * headPath : http://image.huolicoin.com/sdsd232.jpg
         * id : 2
         * payType : 7
         * price : 10
         * quantity : 1
         * remain : 22
         * totalMax : 50
         * totalMin : 5
         * userName : 13500010001
         * phone
         */

        private String headPath;
        private int id;
        private int payType;
        private String price;
        private int quantity;
        private String remain;
        private String totalMax;
        private String totalMin;
        private String userName;
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setHeadPath(String headPath) {
            this.headPath = headPath;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setRemain(String remain) {
            this.remain = remain;
        }

        public void setTotalMax(String totalMax) {
            this.totalMax = totalMax;
        }

        public void setTotalMin(String totalMin) {
            this.totalMin = totalMin;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getHeadPath() {
            return headPath;
        }

        public int getId() {
            return id;
        }

        public int getPayType() {
            return payType;
        }

        public String getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getRemain() {
            return remain;
        }

        public String getTotalMax() {
            return totalMax;
        }

        public String getTotalMin() {
            return totalMin;
        }

        public String getUserName() {
            return userName;
        }
    }
}
