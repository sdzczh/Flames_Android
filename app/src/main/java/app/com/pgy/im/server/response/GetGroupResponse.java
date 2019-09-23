package app.com.pgy.im.server.response;

import java.util.List;

/**
 * Created by AMing on 16/1/26.
 * Company RongCloud
 */
public class GetGroupResponse {
    private List<GroupEntity> list;

    public List<GroupEntity> getList() {
        return list;
    }

    public void setList(List<GroupEntity> list) {
        this.list = list;
    }

    public static class GroupEntity {
            private String imgUrl;
            private String groupId;
            private int num;
            private String name;
            private String decription;

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDecription() {
                return decription;
            }

            public void setDecription(String decription) {
                this.decription = decription;
            }
        }
}
