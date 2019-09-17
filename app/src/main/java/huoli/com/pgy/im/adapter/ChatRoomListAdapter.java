package huoli.com.pgy.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import huoli.com.pgy.Interfaces.getPositionCallback;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ImageLoaderUtils;
import huoli.com.pgy.Widgets.RoundImageView;
import huoli.com.pgy.im.server.response.GetGroupResponse;

/**
 * 聊天室适配器
 *
 * @author xuqingzhong
 */
public class ChatRoomListAdapter extends RecyclerArrayAdapter<GetGroupResponse.GroupEntity> {

    private getPositionCallback callback;
    public ChatRoomListAdapter(Context context) {
        super(context);
    }

    public void setCallback(getPositionCallback callback) {
        this.callback = callback;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyWalletViewHolder(parent);
    }

    class MyWalletViewHolder extends BaseViewHolder<GetGroupResponse.GroupEntity>  implements View.OnClickListener{
        private RoundImageView layoutImChatRoomIcon;
        private TextView layoutImChatRoomRoomName;
        private TextView layoutImChatRoomRoomDesc;
        private TextView layoutImChatRoomOnlineNumber;
        private LinearLayout layoutImChatRoomFrame;

        MyWalletViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_im_chatroom);
            layoutImChatRoomIcon = $(R.id.layout_im_chatRoom_icon);
            layoutImChatRoomRoomName = $(R.id.layout_im_chatRoom_roomName);
            layoutImChatRoomRoomDesc = $(R.id.layout_im_chatRoom_roomDesc);
            layoutImChatRoomOnlineNumber = $(R.id.layout_im_chatRoom_onlineNumber);
            layoutImChatRoomFrame = $(R.id.layout_im_chatRoom_frame);
            layoutImChatRoomFrame.setOnClickListener(this);
        }

        @Override
        public void setData(GetGroupResponse.GroupEntity chatRoomDetail) {
            ImageLoaderUtils.displayCircle(getContext(),layoutImChatRoomIcon,chatRoomDetail.getImgUrl());
            layoutImChatRoomRoomName.setText(chatRoomDetail.getName());
            layoutImChatRoomRoomDesc.setText(chatRoomDetail.getDecription());
            layoutImChatRoomOnlineNumber.setText(chatRoomDetail.getNum()+"人");
        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.getPosition(getPosition());
            }
        }

    }
}