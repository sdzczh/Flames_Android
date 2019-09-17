package huoli.com.pgy.Models.Beans;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/23 0023.
 */

public class RealNameResult implements Serializable{

    /**
     * taskId : bf40403b3dee493395079d148a0f6a86
     * token : 446f2f063f464e5088a37a577fc5c336
     */

    private String taskId;
    private String token;

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "RealNameResult{" +
                "taskId='" + taskId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
