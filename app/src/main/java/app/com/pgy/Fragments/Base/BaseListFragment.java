package app.com.pgy.Fragments.Base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.R;

/**
 * 带HeaderView的分页加载LinearLayout RecyclerView
 * @author xuqingzhong
 */
public abstract class BaseListFragment extends BaseFragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{

    /**
     * 请求数据的页码，从0开始即第一页，每页的数据由后台定
     */
    protected int pageIndex = StaticDatas.PAGE_START;

    /**
     * 初始化布局, 子类必须实现
     */
    protected void init(EasyRecyclerView recyclerView,final RecyclerArrayAdapter adapter) {
        if (recyclerView == null){
            return;
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        /*添加Layout*/
        recyclerView.setLayoutManager(layoutManager);
        /*添加加载进度条*/
//        recyclerView.setProgressView(R.layout.view_progress);
        /*添加适配器*/
        if (adapter == null){
            return;
        }
        recyclerView.setEmptyView(R.layout.view_empty);
        recyclerView.setAdapterWithProgress(adapter);
        recyclerView.setRefreshingColorResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        /*设置加载更多监听*/
        adapter.setMore(R.layout.view_more, this);
        /*点击再无更多加载*/
        adapter.setNoMore(R.layout.view_nomore, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                if (!checkNetworkState()){
                    showToast(R.string.notHaveNet);
                    return;
                }
                adapter.resumeMore();
            }

            @Override
            public void onNoMoreClick() {
                if (!checkNetworkState()){
                    showToast(R.string.notHaveNet);
                    return;
                }
                adapter.resumeMore();
            }
        });
        /*点击断网或错误时点击加载*/
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                if (!checkNetworkState()){
                    showToast(R.string.notHaveNet);
                    return;
                }
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                if (!checkNetworkState()){
                    showToast(R.string.notHaveNet);
                    return;
                }
                adapter.resumeMore();
            }
        });
        /*设置点击Item监听*/
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onListItemClick(position);
            }
        });
        /*添加下拉刷新监听*/
        recyclerView.setRefreshListener(this);
    }

    /***
     * 点击item事件
     * @param position 返回点击的位置
     */
    public abstract void onListItemClick(int position);

}