package huoli.com.pgy.Models.Beans;

/**
 * 消息免打扰返回状态
 * @author xuqingzhong
 */

public class MuteResult {

    /**
     * isMuted : false
     */

    private boolean isMuted;

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }
}
