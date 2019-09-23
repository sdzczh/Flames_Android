package app.com.pgy.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;

import java.util.List;

import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.im.db.TransferRecord;

/**
 * 创建日期：2018/5/21 0021 on 下午 9:05
 * 描述:
 *
 * @author 徐庆重
 */

public class StickyHeaderAdapter implements StickyHeaderDecoration.IStickyHeaderAdapter<StickyHeaderAdapter.HeaderHolder> {
    private static final String TAG = "StickyHeaderAdapter";
    private LayoutInflater mInflater;
    private List<TransferRecord.ListBean> list;

    public StickyHeaderAdapter(Context mContext, List<TransferRecord.ListBean> allData) {
        mInflater = LayoutInflater.from(mContext);
        this.list = allData;
    }

    @Override
    public long getHeaderId(int position) {
        int headerId = 1;
        if (list == null || list.size() <= 1){
            return headerId;
        }

        //return position/4;
        int firstPos = getPosition(position, 0);
        if (firstPos > 0){
            headerId++;
            getPosition(position,firstPos);
        }
        return headerId;
    }

    private int getPosition(int currentPos,int forPos){
        int currentTime = MathUtils.string2Integer(list.get(currentPos).getCreateTime());
        int forTime = MathUtils.string2Integer(list.get(forPos).getCreateTime());
        if (currentTime - forTime >= 20){
            return currentPos;
        }else{
            return 0;
        }
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.layout_im_transfer_head, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
        long headerId = getHeaderId(position);
        LogUtils.w(TAG,"headerId:"+headerId);
        viewholder.time.setText(headerId+"月");
        viewholder.out.setText("支出：¥"+position*headerId);
        viewholder.in.setText("收入：¥"+position*headerId);
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView out;
        public TextView in;

        public HeaderHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.layout_imTransferHead_time);
            out = itemView.findViewById(R.id.layout_imTransferHead_out);
            in = itemView.findViewById(R.id.layout_imTransferHead_in);
        }
    }
}

