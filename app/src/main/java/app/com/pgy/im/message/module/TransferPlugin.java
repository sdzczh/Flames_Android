package app.com.pgy.im.message.module;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import app.com.pgy.R;
import app.com.pgy.im.UI.TransferActivity;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * Created by YX on 2018/5/21.
 */

public class TransferPlugin implements IPluginModule {
    private Conversation.ConversationType conversationType;
    private String targetId;
    public TransferPlugin() {

    }

    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.rc_transfer_plugin_bg);
    }

    @Override
    public String obtainTitle(Context context) {
        return "转账";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        this.conversationType = rongExtension.getConversationType();
        this.targetId = rongExtension.getTargetId();
        Context context = fragment.getContext();
        /*点击红包plugin,跳转发送红包页面*/
        Intent intent2Transfer = new Intent(context, TransferActivity.class);
        intent2Transfer.putExtra("targetId",targetId);
        context.startActivity(intent2Transfer);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
