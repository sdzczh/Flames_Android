package app.com.pgy.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Widgets.RoundImageView;
import app.com.pgy.im.server.response.GetUserInfoByPhoneResponse;

/**
 * 添加好友适配器
 *
 * @author xuqingzhong
 */
public class AddFriendListAdapter extends RecyclerArrayAdapter<GetUserInfoByPhoneResponse.ResultEntity> {

    private getPositionCallback callback;

    public AddFriendListAdapter(Context context) {
        super(context);
    }

    public void setCallback(getPositionCallback callback) {
        this.callback = callback;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyWalletViewHolder(parent);
    }

    class MyWalletViewHolder extends BaseViewHolder<GetUserInfoByPhoneResponse.ResultEntity> implements View.OnClickListener {
        private RoundImageView itemImAddFriendUserIcon;
        private TextView itemImAddFriendUserName;
        private TextView itemImAddFriendUserPhone;
        private LinearLayout itemImAddFriendFrame;

        MyWalletViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_im_add_firend);
            itemImAddFriendUserIcon = $(R.id.item_im_addFriend_userIcon);
            itemImAddFriendUserName = $(R.id.item_im_addFriend_userName);
            itemImAddFriendUserPhone = $(R.id.item_im_addFriend_userPhone);
            itemImAddFriendFrame = $(R.id.item_im_addFriend_frame);
            itemImAddFriendFrame.setOnClickListener(this);
        }

        @Override
        public void setData(GetUserInfoByPhoneResponse.ResultEntity addFriendDetail) {
            ImageLoaderUtils.displayCircle(getContext(),itemImAddFriendUserIcon,addFriendDetail.getHeadPath());
            itemImAddFriendUserName.setText(addFriendDetail.getNickName());
            itemImAddFriendUserPhone.setText(addFriendDetail.getPhone());
        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.getPosition(getPosition());
            }
        }

    }
}