package app.com.pgy.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Widgets.RoundImageView;
import app.com.pgy.im.server.response.GetUserInfosResponse;

/**
 * Created by AMing on 16/1/14.
 * Company RongCloud
 */
public class FriendListAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;

    private List<GetUserInfosResponse.ResultEntity> list;

    public FriendListAdapter(Context context, List<GetUserInfosResponse.ResultEntity> list) {
        this.context = context;
        this.list = list;
    }


    /**
     * 传入新的数据 刷新UI的方法
     */
    public void updateListView(List<GetUserInfosResponse.ResultEntity> list) {
        if (list == null || list.size() <= 0){
            return;
        }
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null){ return list.size();}
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list == null) {
            return null;
        }
        if (position >= list.size()) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final GetUserInfosResponse.ResultEntity mContent = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);
            viewHolder.tvTitle = convertView.findViewById(R.id.friendname);
            viewHolder.tvLetter = convertView.findViewById(R.id.catalog);
            viewHolder.mImageView = convertView.findViewById(R.id.frienduri);
            viewHolder.tvUserId = convertView.findViewById(R.id.friend_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GetUserInfosResponse.ResultEntity resultEntity = list.get(position);
        if (resultEntity == null){
            resultEntity = new GetUserInfosResponse.ResultEntity();
        }
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
             String letterFirst = String.valueOf(resultEntity.getLetters().toUpperCase().charAt(0));
            viewHolder.tvLetter.setText(letterFirst);
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(resultEntity.getName());
        ImageLoaderUtils.displayCircle(context,viewHolder.mImageView,resultEntity.getHeadPath());
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getLetters();
            char firstChar = sortStr.charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getLetters().charAt(0);
    }


    final static class ViewHolder {
        /**
         * 首字母
         */
        TextView tvLetter;
        /**
         * 昵称
         */
        TextView tvTitle;
        /**
         * 头像
         */
        RoundImageView mImageView;
        /**
         * userid
         */
        TextView tvUserId;
    }
}
