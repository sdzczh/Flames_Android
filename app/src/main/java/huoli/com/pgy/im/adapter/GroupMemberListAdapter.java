package huoli.com.pgy.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ImageLoaderUtils;
import huoli.com.pgy.Widgets.RoundImageView;
import huoli.com.pgy.im.server.response.GetGroupMemberResponse;

/**
 * 聊天室成员适配器
 *
 * @author xuqingzhong
 */
public class GroupMemberListAdapter extends RecyclerArrayAdapter<GetGroupMemberResponse.UserEntity> {

    public GroupMemberListAdapter(Context context) {
        super(context);
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyWalletViewHolder(parent);
    }

    class MyWalletViewHolder extends BaseViewHolder<GetGroupMemberResponse.UserEntity>{
        private RoundImageView layoutImGroupMemberIcon;
        private TextView layoutImGroupMemberName;
        private TextView layoutImGroupMemberOwner;

        MyWalletViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_im_groupmembers);
            layoutImGroupMemberIcon = $(R.id.layout_im_groupMember_icon);
            layoutImGroupMemberName = $(R.id.layout_im_groupMember_name);
            layoutImGroupMemberOwner = $(R.id.layout_im_groupMember_owner);
        }

        @Override
        public void setData(GetGroupMemberResponse.UserEntity memberDetail) {
            ImageLoaderUtils.displayCircle(getContext(), layoutImGroupMemberIcon, memberDetail.getHeadPath());
            layoutImGroupMemberName.setText(memberDetail.getName());
            layoutImGroupMemberOwner.setVisibility(memberDetail.getRole()==0?View.VISIBLE:View.GONE);
        }

    }
}