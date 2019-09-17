package huoli.com.pgy.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ImageLoaderUtils;
import huoli.com.pgy.Widgets.RoundImageView;
import huoli.com.pgy.im.server.response.UserRelationshipResponse;

@SuppressWarnings("deprecation")
public class NewFriendListAdapter extends BaseAdapters {

    public NewFriendListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.rs_ada_user_ship, parent, false);
            holder.mName = convertView.findViewById(R.id.ship_name);
            holder.mHead = convertView.findViewById(R.id.new_header);
            holder.mState = convertView.findViewById(R.id.ship_state);
            holder.mAgree = convertView.findViewById(R.id.ship_agree);
            holder.mRefuse = convertView.findViewById(R.id.ship_refuse);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final UserRelationshipResponse.ListBean bean = (UserRelationshipResponse.ListBean) dataSet.get(position);
        holder.mName.setText(bean.getNickName());
        //String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(new UserInfo(user.getId(), user.getNickname(), Uri.parse(user.getPortraitUri())));
        //ImageLoader.getInstance().displayImage(portraitUri, holder.mHead, MyApplication.getOptions());
        ImageLoaderUtils.displayCircle(getContext(),holder.mHead,bean.getHeadPath());
        holder.mAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemButtonClick != null) {
                    mOnItemButtonClick.onAgreeClick(position, v, bean.getState());
                }
            }
        });

        holder.mRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemButtonClick != null) {
                    mOnItemButtonClick.onRefuseClick(position, v, bean.getState());
                }
            }
        });

        switch (bean.getState()) {
            case 0: //收到了好友邀请
                holder.mAgree.setVisibility(View.VISIBLE);
                holder.mRefuse.setVisibility(View.VISIBLE);
                holder.mState.setVisibility(View.GONE);
                break;
            case 2: // 忽略好友邀请
                holder.mAgree.setVisibility(View.GONE);
                holder.mRefuse.setVisibility(View.GONE);
                holder.mState.setVisibility(View.VISIBLE);
                holder.mState.setText(R.string.ignore);
                break;
            case 1: // 已是好友
                holder.mAgree.setVisibility(View.GONE);
                holder.mRefuse.setVisibility(View.GONE);
                holder.mState.setVisibility(View.VISIBLE);
                holder.mState.setText(R.string.added);
                break;
                default:break;
        }
        return convertView;
    }

    /**
     * displayName :
     * message : 手机号:18622222222昵称:的用户请求添加你为好友
     * status : 11
     * updatedAt : 2016-01-07T06:22:55.000Z
     * user : {"id":"i3gRfA1ml","nickname":"nihaoa","portraitUri":""}
     */

    class ViewHolder {
        RoundImageView mHead;
        TextView mName;
        TextView mState;
        TextView mAgree;
        TextView mRefuse;
    }

    OnItemButtonClick mOnItemButtonClick;


    public void setOnItemButtonClick(OnItemButtonClick onItemButtonClick) {
        this.mOnItemButtonClick = onItemButtonClick;
    }

    public interface OnItemButtonClick {
        boolean onAgreeClick(int position, View view, int status);
        boolean onRefuseClick(int position, View view, int status);
    }
}
