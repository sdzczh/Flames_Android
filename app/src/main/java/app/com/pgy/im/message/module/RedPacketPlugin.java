package app.com.pgy.im.message.module;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.im.UI.SendRedPackageActivity;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;


/**
 * Created by YX on 2018/5/18.
 */

public class RedPacketPlugin implements IPluginModule {
    private static final String TAG = "RedPacketPlugin";
    private Conversation.ConversationType conversationType;
    private String targetId;

    public RedPacketPlugin(){

    }
    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.rc_red_packet_bg);
    }

    @Override
    public String obtainTitle(Context context) {
        return "红包";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        this.conversationType = rongExtension.getConversationType();
        this.targetId = rongExtension.getTargetId();
        LogUtils.w(TAG,"targetId:"+targetId);
        Context context = fragment.getContext();
        /*点击红包plugin,跳转发送红包页面*/
        Intent intent2SendRedPackage = new Intent(context, SendRedPackageActivity.class);
        intent2SendRedPackage.putExtra("targetId",targetId);
        context.startActivity(intent2SendRedPackage);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {
        LogUtils.w(TAG,"onActivityResult");
    }
}
