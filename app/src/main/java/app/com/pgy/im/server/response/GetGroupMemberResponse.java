package app.com.pgy.im.server.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by AMing on 16/1/27.
 * Company RongCloud
 */
public class GetGroupMemberResponse {

    private List<UserEntity> list;

    public List<UserEntity> getList() {
        return list;
    }

    public void setList(List<UserEntity> list) {
        this.list = list;
    }

    public static class UserEntity implements Serializable {
            private int role;
            private String phone;
            private String headPath;
            private String name;

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
