package huoli.com.pgy.im.UI;

import android.os.Bundle;
import android.view.View;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import huoli.com.pgy.Activitys.Base.BaseListActivity;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.TitleView;
import huoli.com.pgy.im.adapter.StickyHeaderAdapter;
import huoli.com.pgy.im.adapter.TransferRecordsAdapter;
import huoli.com.pgy.im.db.TransferRecord;

/**
 * 创建日期：2018/5/21 0021 on 下午 8:26
 * 描述:IM的转账记录
 *
 * @author 徐庆重
 */

public class TransferRecordsActivity extends BaseListActivity {
    /**标题*/
    @BindView(R.id.activity_baselist_title)
    TitleView titleView;
    /**初始化适配器和RecyclerView*/
    private TransferRecordsAdapter adapter;
    @BindView(R.id.activity_baselist_list)
    EasyRecyclerView recyclerView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_baselist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        if (adapter == null){
            adapter = new TransferRecordsAdapter(mContext);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferRecordsActivity.this.finish();
            }
        });
        init(recyclerView,adapter);
        // StickyHeader
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(new StickyHeaderAdapter(mContext,adapter.getAllData()));
        decoration.setIncludeHeader(false);
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void onListItemClick(int position) {

    }

    @Override
    public void onRefresh() {
        List<TransferRecord.ListBean> records = new ArrayList<>();
        for (int i=1;i<=20;i++){
            TransferRecord.ListBean record = new TransferRecord.ListBean();
            record.setId(i);
            record.setAmount(i+"0."+i);
            record.setCoinType(i%8);
            record.setCreateTime(TimeUtils.timeStampInt()+i*5+"");
            record.setName("测试"+i);
            record.setPriceOfCNy("100"+i);
            records.add(record);
        }
        adapter.addAll(records);
    }

    @Override
    public void onLoadMore() {

    }
}
