package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import huoli.com.pgy.R;

/**
 * Created by xuqingzhong on 2017/12/20 0020.
 * @author xuqingzhong
 * 自定义上弹列表框适配器
 */

public class MyBottomSpinnerListAdapter extends BaseAdapter {
    private List<String> mStringList;
    private Context mContext;

    public MyBottomSpinnerListAdapter(Context context, List<String> list) {
        mContext = context;
        this.mStringList = list;
    }

    @Override
    public int getCount() {
        return mStringList.size();
    }

    @Override
    public Object getItem(int i) {
        return mStringList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder lViewHolder;
        if (view == null) {
            lViewHolder = new ViewHolder();
            view = View.inflate(mContext, R.layout.layout_mybottomspinner_item, null);
            lViewHolder.itemTextView = view.findViewById(R.id.mybottomspinner_item_item);
            view.setTag(lViewHolder);
        } else {
            lViewHolder = (ViewHolder) view.getTag();
        }
        //文字内容设置
        lViewHolder.itemTextView.setText(mStringList.get(i));
        return view;
    }

    private class ViewHolder {
        TextView itemTextView;
    }
}
