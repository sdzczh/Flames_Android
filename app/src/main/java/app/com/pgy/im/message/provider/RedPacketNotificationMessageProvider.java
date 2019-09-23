package app.com.pgy.im.message.provider;

import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.com.pgy.Constants.Preferences;
import app.com.pgy.R;
import app.com.pgy.im.message.RedPacketNotificationMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * 红包被拆，提示消息的展示
 * Created by YX on 2018/5/19.
 */

@ProviderTag(messageContent = RedPacketNotificationMessage.class, showPortrait = false, centerInHorizontal = true, showProgress = false, showSummaryWithName = false)
public class RedPacketNotificationMessageProvider extends IContainerItemProvider.MessageProvider<RedPacketNotificationMessage> {

    private static class ViewHolder{
        TextView tv_content;
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_red_packet_notification,null);
        ViewHolder holder = new ViewHolder();
        holder.tv_content = view.findViewById(R.id.tv_content);
        view.setTag(holder);
        return view;
    }

    @Override
    public Spannable getContentSummary(RedPacketNotificationMessage redPacketNotificationMessage) {
        return null;
    }

    @Override
    public Spannable getContentSummary(Context context, RedPacketNotificationMessage data) {
        return super.getContentSummary(context, data);
    }

    @Override
    public void onItemClick(View view, int i, RedPacketNotificationMessage redPacketNotificationMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int position, RedPacketNotificationMessage content, UIMessage message) {
        super.onItemLongClick(view, position, content, message);
    }

    @Override
    public void bindView(View view, int i, RedPacketNotificationMessage redPacketNotificationMessage, UIMessage uiMessage) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (redPacketNotificationMessage != null){
            if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND){
                viewHolder.tv_content.setText("你领取了"+redPacketNotificationMessage.getRedFromName()+"的红包");
            }else {
                if (Preferences.getLocalUser().getPhone().equals(redPacketNotificationMessage.getRedFromUid())){
                    viewHolder.tv_content.setText(redPacketNotificationMessage.getSendName()+"领取了你的红包");
                }else {
                    viewHolder.tv_content.setText(redPacketNotificationMessage.getSendName()+"领取了"+redPacketNotificationMessage.getRedFromName()+"的红包");
                }
            }
        }
    }
}
