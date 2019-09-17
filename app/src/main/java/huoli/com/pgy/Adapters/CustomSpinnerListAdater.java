package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import huoli.com.pgy.R;

/**
 * Created by YX on 2018/7/9.
 */

public class CustomSpinnerListAdater extends BaseAdapter {
    private List<String> mStringList;
    private Context mContext;

    public CustomSpinnerListAdater(Context context, List<String> list) {
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
            view = View.inflate(mContext, R.layout.view_custom_spinner_item, null);
            lViewHolder.itemTextView = view.findViewById(R.id.tv_my_spinner_item_content);
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
