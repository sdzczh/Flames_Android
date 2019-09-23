package app.com.pgy.im.server.response;

import java.util.List;

/**
 * Created by AMing on 16/1/7.
 * Company RongCloud
 */
public class UserRelationshipResponse {
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean{

        /**
         * id : 2
         * state : 0
         * nickName : 李
         * headPath :
         */

        private int id;
        private int state;
        private String nickName;
        private String headPath;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
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
    }
}
