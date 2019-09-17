package huoli.com.pgy.im.server.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by AMing on 16/1/4.
 * Company RongCloud
 */
public class GetUserInfoByPhoneResponse {
    private List<ResultEntity> list;

    public List<ResultEntity> getList() {
        return list;
    }

    public void setList(List<ResultEntity> list) {
        this.list = list;
    }

    public static class ResultEntity implements Serializable{
        /**
         * nickName : 李
         * phone : 1768
         * headPath :
         */

        private String nickName;
        private String phone;
        private String headPath;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getHeadPath() {
            return headPath;
        }

        public void setHeadPath(String headPath) {
            this.headPath = headPath;
        }
    }
}
