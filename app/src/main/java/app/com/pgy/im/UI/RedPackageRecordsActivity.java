package app.com.pgy.im.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import app.com.pgy.Activitys.Base.BaseListActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.MyCenterSpinnerList;
import app.com.pgy.Widgets.RoundImageView;
import app.com.pgy.Widgets.TitleView;
import app.com.pgy.im.adapter.RedPackageRecordsAdapter;
import app.com.pgy.im.server.response.RedPackageRecord;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * @author xuqingzhong
 * 红包、转账记录列表页
 */
public class RedPackageRecordsActivity extends BaseListActivity {
    /**标题*/
    @BindView(R.id.activity_baselist_title)
    TitleView titleView;
    /**初始化适配器和RecyclerView*/
    private RedPackageRecordsAdapter adapter;
    @BindView(R.id.activity_baselist_list)
    EasyRecyclerView recyclerView;
    private MyCenterSpinnerList chooseIsSendSpinner;
    private spinnerSingleChooseListener spinnerChooseListener;
    private TextView allAmount;
    /**是红包记录还是转账记录*/
    private boolean isRedPacket=true;
    /**是发送红包还是接收*/
    private boolean isSend = true;
    private String sumAmtOfCny;

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
        isRedPacket = getIntent().getBooleanExtra("isRedPacket",true);
        if (adapter == null) {
            adapter = new RedPackageRecordsAdapter(mContext);
        }

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle(isRedPacket?"红包记录":"转账记录");
        titleView.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedPackageRecordsActivity.this.finish();
            }
        });
        titleView.setImgRightVisibility(View.VISIBLE);
        titleView.setImgRightResource(R.mipmap.more_point);
        titleView.setTvRightVisibility(View.VISIBLE);
        titleView.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*切换发送红包还是接收红包*/
                showChooseIsSendDialog();
            }
        });
        setSpinnerListener();
        /*继承BaseListFragment，初始化*/
        init(recyclerView, adapter);
        /*添加头部*/
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View headView = LayoutInflater.from(mContext).inflate(R.layout.layout_im_redpackage_record_head,parent,false);
                return headView;
            }

            @Override
            public void onBindView(View headerView) {
                /*给头部设置数据*/
                RoundImageView icon = headerView.findViewById(R.id.layout_im_redPackageRecordHead_icon);
                TextView sendType = headerView.findViewById(R.id.layout_im_redPackageRecordHead_type);
                ImageLoaderUtils.displayCircle(mContext,icon, Preferences.getLocalUser().getHeadImg());
                allAmount = headerView.findViewById(R.id.layout_im_redPackageRecordHead_amount);
                //ImageLoaderUtils.displayCircle(mContext,icon,);
                if (isRedPacket) {
                    sendType.setText(isSend ? "共发出" : "共收到");
                }else{
                    sendType.setText(isSend ? "共转出" : "共转入");
                }
                allAmount.setText("¥"+sumAmtOfCny);
            }
        });
    }

    /**弹出选择发送红包还是接收对话框*/
    private void showChooseIsSendDialog() {
        List<String> coinNameList = new ArrayList<>();
        coinNameList.add(isRedPacket?"收到的红包":"转入");
        coinNameList.add(isRedPacket?"发出的红包":"转出");
        chooseIsSendSpinner = new MyCenterSpinnerList(mContext,isRedPacket?"红包记录":"转账记录",coinNameList,recyclerView);
        chooseIsSendSpinner.setMySpinnerListener(spinnerChooseListener);
        chooseIsSendSpinner.showUp();
    }

    private void setSpinnerListener() {
        spinnerChooseListener = new spinnerSingleChooseListener() {
            @Override
            public void onItemClickListener(int position) {
                switch (position){
                    case 0:
                        isSend = false;
                        break;
                    case 1:
                        isSend = true;
                        break;
                        default:break;
                }
                onRefresh();
            }
        };
    }

    /**点击某Item时候的操作*/
    @Override
    protected void onListItemClick(int position) {

    }

    /**下拉刷新*/
    @Override
    public void onRefresh() {
        adapter.clear();
        if (!isLogin()){
            return;
        }
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            recyclerView.setRefreshing(false);
            return;
        }
        requestData(pageIndex);
    }

    /**上拉加载*/
    @Override
    public void onLoadMore() {
        if (!isLogin()){
            return;
        }
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            recyclerView.setRefreshing(false);
            return;
        }
        pageIndex++;
        requestData(pageIndex);
    }

    /**请求数据*/
    private void requestData(int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", isSend?1:0);
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        if (isRedPacket) {
            NetWorks.getRedPackageRecordList(Preferences.getAccessToken(), map, new getBeanCallback<RedPackageRecord>() {
                @Override
                public void onSuccess(RedPackageRecord record) {
                    sumAmtOfCny = record.getSumAmtOfCny();
                    allAmount.setText("¥" + sumAmtOfCny);
                    List<RedPackageRecord.ListBean> list = record.getList();
                    if (list == null || list.size() <= 0) {
                    /*再无更多数据*/
                        adapter.stopMore();
                        return;
                    }
                    adapter.addAll(list);
                }

                @Override
                public void onError(int errorCode, String reason) {
                    onFail(errorCode, reason);
                /*网络错误*/
                    adapter.pauseMore();
                }
            });
        }else{
            NetWorks.getTransferRecordList(Preferences.getAccessToken(), map, new getBeanCallback<RedPackageRecord>() {
                @Override
                public void onSuccess(RedPackageRecord record) {
                    sumAmtOfCny = record.getSumAmtOfCny();
                    allAmount.setText("¥" + sumAmtOfCny);
                    List<RedPackageRecord.ListBean> list = record.getList();
                    if (list == null || list.size() <= 0) {
                    /*再无更多数据*/
                        adapter.stopMore();
                        return;
                    }
                    adapter.addAll(list);
                }

                @Override
                public void onError(int errorCode, String reason) {
                    onFail(errorCode, reason);
                /*网络错误*/
                    adapter.pauseMore();
                }
            });
        }
    }
}