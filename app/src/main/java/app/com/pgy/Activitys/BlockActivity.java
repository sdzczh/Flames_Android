package app.com.pgy.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseListActivity;
import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Adapters.DigRecordListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.BlockCollection;
import app.com.pgy.Models.Beans.DigPageInfo;
import app.com.pgy.Models.Beans.DigRecord;
import app.com.pgy.Models.Beans.DigResult;
import app.com.pgy.Models.Beans.EventBean.EventForceUpBanner;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.Models.Beans.Position;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.LoginUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.Utils;
import app.com.pgy.Widgets.BlockImageView;
import app.com.pgy.Widgets.ExitDialog;
import app.com.pgy.Widgets.LevelChangeDialog;
import app.com.pgy.Widgets.SupperRecyclerView;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/14.
 */

public class BlockActivity extends BaseListActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    /**
     * 区块消失间隔时间S
     */
    private static final int BLOCK_RESUME_TIME = 10;

    @BindView(R.id.iv_activity_block_bgImage)
    ImageView ivActivityBlockBgImage;
    @BindView(R.id.tv_activity_block_blockPoolName)
    TextView tvActivityBlockBlockPoolName;
    @BindView(R.id.tv_activity_block_currentLevel)
    TextView tvActivityBlockCurrentLevel;
    @BindView(R.id.tv_activity_block_currentNumber)
    TextView tvActivityBlockCurrentNumber;
    @BindView(R.id.iv_activity_block_currentCalculateForce_icon)
    ImageView ivActivityBlockCurrentCalculateForceIcon;
    @BindView(R.id.tv_activity_block_currentCalculateForce)
    TextView tvActivityBlockCurrentCalculateForce;
    @BindView(R.id.ll_activity_blockNew_currentCalculateForce_frame)
    LinearLayout llActivityBlockNewCurrentCalculateForceFrame;
    @BindView(R.id.fl_activity_block_container)
    FrameLayout flActivityBlockContainer;
    @BindView(R.id.switch_activity_block_bottomFrame_switch_state)
    Switch switchActivityBlockBottomFrameSwitchState;
    @BindView(R.id.switch_activity_block_bottomFrame_music)
    Switch switchActivityBlockBottomFrameMusic;
    @BindView(R.id.srv_activity_block_bottomFrame_list)
    SupperRecyclerView srvActivityBlockBottomFrameList;
    @BindView(R.id.ll_activity_blockNew_bottomFrame)
    LinearLayout llActivityBlockNewBottomFrame;
    @BindView(R.id.tv_activity_block_myAssets)
    TextView tvActivityBlockmyAssets;
    @BindView(R.id.tv_activity_block_bottomFrame_switch_state)
    TextView tvActivityBlockBottomFrameSwitchState;
    @BindView(R.id.tv_activity_block_bottomFrame_switch_music)
    TextView tvActivityBlockBottomFrameSwitchMusic;

    private DigRecordListAdapter adapter;
    /**
     * 是否正在挖矿
     */
    private boolean isDiging;
    private SparseArray<BlockImageView> blockViews;
    /**
     * 区块的宽、高，区块活动界面的宽、高
     */
    private int blockViewWidth, blockViewHeight, frameWidth, frameHeight;
    private SparseArray<Position> positions;
    /**
     * 当前等级
     */
    private int currentLevel;
    /**
     * 当前等级中所有的可挖币种
     */
    private List<Integer> digCoins;

    private LevelChangeDialog levelChangeDialog;

    private DigPageInfo mDigPageInfo;

    private boolean isFront = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_block;
    }

    @Override
    protected void initData() {
        LogUtils.w(TAG, "config:" + getConfiguration().toString());
        if (adapter == null) {
            adapter = new DigRecordListAdapter(mContext);
        }
        /*设置区块的宽、高，区块活动界面的宽、高*/
        blockViewWidth = MathUtils.dip2px(getApplicationContext(), 50);
        blockViewHeight = MathUtils.dip2px(getApplicationContext(), 50);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        frameWidth = metrics.widthPixels;
        frameHeight = metrics.heightPixels / 2;
        /*切换当前等级的界面*/
        blockViews = new SparseArray<>();
        positions = new SparseArray<>();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    /*如果自动挖矿，则启动全部矿石*/
        refreshFrame();
        /*初始化列表*/
        srvActivityBlockBottomFrameList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        init(srvActivityBlockBottomFrameList, adapter);
        switchActivityBlockBottomFrameMusic.setOnCheckedChangeListener(this);
        switchActivityBlockBottomFrameSwitchState.setOnCheckedChangeListener(this);
        if (isLogin()){
            switchActivityBlockBottomFrameMusic.setChecked(Preferences.isOpenBlockBgMusic());
        }else {
            switchActivityBlockBottomFrameMusic.setChecked(false);
        }
        /*初始化底部栏*/
        BottomSheetBehavior behavior = BottomSheetBehavior.from(llActivityBlockNewBottomFrame);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet 状态的改变
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        onRefresh();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        adapter.clear();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        isFront = true;
         /*获取当前等级，以及该等级的内容*/
        currentLevel = Preferences.getCurrentLevel();
        playMusic(currentLevel);
        getDigPageInfo(currentLevel);
    }


    @Override
    public void onPause() {
        isFront = false;
        mediaPause();
        super.onPause();
    }


    /**
     * 登录状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventLoginState loginState) {
        if (isLogin()) {
            refreshFrame();
            currentLevel = Preferences.getCurrentLevel();
            getDigPageInfo(currentLevel);
            initSoundPool(currentLevel);
        } else {
            /*退出登录取消所有矿*/
            isDiging = false;
            switchActivityBlockBottomFrameSwitchState.setChecked(false);
            switchActivityBlockBottomFrameMusic.setChecked(false);
            cancelAllDigCoin();
            switch2CurrentLevelFrame(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ForceBannerEvent(EventForceUpBanner event){
        if (event != null && event.getType() == EventForceUpBanner.TYPE_ALL){
            finish();
        }
    }

    /**
     * 刷新界面
     */
    private void refreshFrame() {
        isDiging = Preferences.getDigFlag();
        changeDigState(isDiging);
    }

    @Override
    public void onDestroy() {

        /*解除顶部币对币监听*/
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mediaStop();
        collectMusicRelease();
        super.onDestroy();
    }

    /**
     * 切换等级界面
     */
    private void switch2CurrentLevelFrame(DigPageInfo info) {
        mDigPageInfo = info;
        if (info != null) {

            if (info.getLevelChg() != 0) {
                showLevelDialog(info.getLevel(), info.getHonorName(), info.getLevelChg(), info.getHonorPic(), info.getCoinChg());
            }
            Preferences.setCurrentLevel(info.getLevel());
            playMusic(info.getLevel());
            initSoundPool(info.getLevel());
            llActivityBlockNewCurrentCalculateForceFrame.setVisibility(View.VISIBLE);
            tvActivityBlockCurrentNumber.setVisibility(View.VISIBLE);
            ImageLoaderUtils.display(this, ivActivityBlockBgImage, info.getMinePic(), R.mipmap.lv1);
            ImageLoaderUtils.display(this, ivActivityBlockCurrentCalculateForceIcon, info.getHonorPic(), R.mipmap.level_1);
            digCoins = info.getMineCoinList();
            refreshFrame();
            tvActivityBlockBlockPoolName.setText(info.getMineName() + "");
            tvActivityBlockCurrentLevel.setText("LV" + info.getLevel());
            tvActivityBlockCurrentNumber.setText(info.getCountOnline() + "名" + info.getHonorName() + "在线");
            tvActivityBlockCurrentCalculateForce.setText("算力值 " + info.getSoulVal());
        } else {
            mediaStop();
            llActivityBlockNewCurrentCalculateForceFrame.setVisibility(View.GONE);
            tvActivityBlockCurrentNumber.setVisibility(View.GONE);
            tvActivityBlockBlockPoolName.setText("圣魂村");
            tvActivityBlockCurrentLevel.setText("LV1");
            ivActivityBlockBgImage.setImageResource(R.mipmap.lv1);
        }

    }

    /**
     * 启动挖矿
     */
    private void start2DigAllCoin() {
        if (digCoins == null) {
            digCoins = new ArrayList<>();
        }
        for (int coin : digCoins) {
                /*创建所有挖矿币种*/
            createCoinBlock(coin);
        }


    }

    /**
     * 关闭所有挖矿
     */
    private void cancelAllDigCoin() {
        for (int i = 0; i < blockViews.size(); i++) {
            removeBlock2Map(blockViews.keyAt(i));
        }
    }

    /**
     * 创建矿石
     */
    private void createCoinBlock(int coinType) {
        if (isFinishing()||blockViews.indexOfKey(coinType) >= 0) {
            LogUtils.w(TAG, "创建币种：" + coinType + "已经存在");
            return;
        }
        LogUtils.w(TAG, "创建币种：" + coinType);
        /*定义imageView宽高*/
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(blockViewWidth, blockViewHeight);
        /*创建对象，id为coinType*/
        BlockImageView block = new BlockImageView(this);
        block.setId(coinType);
        block.setText(getCoinName(coinType));
        block.setLayoutParams(vlp);
        block.setImageResource(R.mipmap.block_bg);
         /*将imageView添加到Frame布局中*/
        flActivityBlockContainer.addView(block);

        /*设置imageView的位置，随机*/
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) block.getLayoutParams();
        Position newViewPosition = getRandomPosition();
        params.leftMargin = newViewPosition.getX();
        params.topMargin = newViewPosition.getY();
        positions.put(coinType, newViewPosition);
        /*添加imageView监听*/
        block.setOnClickListener(this);
        /*将矿石view添加到布局中*/
        blockViews.put(coinType, block);
    }

    /**
     * 获取不重复的随机位置
     */
    private Position getRandomPosition() {
        Position position = new Position(getRandomLocationX(), getRandomLocationY());
        for (int i = 0; i < positions.size(); i++) {
            int key = positions.keyAt(i);
            Position current = positions.get(key);
            if (current.equals(position)) {
                position = getRandomPosition();
            }
        }
        return position;
    }

    private int getRandomLocationX() {
        /*水平方向数量*/
        int xNumber = frameWidth / blockViewWidth;
        /*随机位置下标（0，xNumber-1）*/
        int index = Math.abs(new Random().nextInt() % (xNumber - 1));
        /*横向总边距*/
        int margins = frameWidth - xNumber * blockViewHeight;
        int margin = margins > 0 ? margins / 2 : 0;
        /*随机位置x */
        int positionX = (int) ((index + 0.5) * blockViewWidth + margin);
        /*布局左右边距*/
        int left = blockViewWidth / 2;
        int right = frameWidth - blockViewWidth / 2;
        if (positionX < left) {
            positionX = left;
        } else if (positionX > right) {
            positionX = right;
        }
        return positionX;
    }

    private int getRandomLocationY() {
        /*垂直方向数量*/
        int yNumber = frameHeight / blockViewHeight;
        /*随机位置下标（0，yNumber-1）*/
        int index = Math.abs(new Random().nextInt() % (yNumber - 1));
        /*纵向总边距*/
        int margins = frameHeight - yNumber * blockViewHeight;
        int margin = margins > 0 ? margins / 2 : 0;
        /*随机位置x */
        int positionY = (int) ((index + 0.5) * blockViewHeight + margin);
        /*布局上下边距*/
        int top = blockViewHeight / 2;
        int bottom = frameHeight - blockViewHeight / 2;
        if (positionY < top) {
            positionY = top;
        } else if (positionY > bottom) {
            positionY = bottom;
        }
        return positionY;
    }

    /**
     * 将矿石view从布局中移除
     */
    private void removeBlock2Map(final int coinType) {
        if (blockViews.indexOfKey(coinType) < 0) {
            LogUtils.w(TAG, "移除币种：" + coinType + "不存在");
            return;
        }
        final BlockImageView imageView = blockViews.get(coinType);
        if (imageView == null) {
            LogUtils.w(TAG, "imageView：" + coinType + "不存在");
            return;
        }
        LogUtils.w(TAG, "移除币种：" + coinType);
        imageView.setClickable(false);
        /*启动消失动画*/
        imageView.fadeOut(tvActivityBlockmyAssets, new BlockImageView.BlockImageViewFadeOutListener() {
            @Override
            public void fadeOutTime() {
            /*从区块view队列中移除*/
                blockViews.delete(coinType);
        /*从布局中移除*/
                flActivityBlockContainer.removeView(imageView);
        /*将位置从队列中移除*/
                positions.remove(coinType);
                if (isDiging) {
                    resumeBlock(coinType);
                }
            }
        });
    }

    /**
     * 隔一段时间后，再次添加币种
     */
    private void resumeBlock(final int coinType) {
        if (!isLogin() || !isDiging) {
            return;
        }
        if (!digCoins.contains(coinType)) {
            LogUtils.w(TAG, "币种：" + coinType + "无法复活");
            return;
        }
        int countSeconds = Math.abs(new Random().nextInt() % BLOCK_RESUME_TIME) * 1000;
        CountDownTimer timer = new CountDownTimer(countSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                createCoinBlock(coinType);
            }
        };
        timer.start();
    }

    /**
     * 弹出关闭挖矿提醒
     */
    private void showCloseSwitchDialog() {
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("关闭自动挖矿将会损失好多收益哦~");
        builder.setCancelable(false);

        builder.setPositiveButton("确认关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        autoDig();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switchActivityBlockBottomFrameSwitchState.setChecked(true);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 自动挖矿
     */
    private void autoDig() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.autoDig(Preferences.getAccessToken(), map, new getBeanCallback<DigResult>() {
            @Override
            public void onSuccess(DigResult digResult) {
                hideLoading();
                if (digResult == null) {
                    digResult = new DigResult();
                }
                isDiging = digResult.getDigFlag();
                Preferences.isDigFlag(isDiging);
                changeDigState(isDiging);
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
                changeDigState(isDiging);
            }
        });
    }

    private void changeDigState(boolean digFlag) {
        if (digFlag) {
            switchActivityBlockBottomFrameSwitchState.setChecked(true);
            start2DigAllCoin();
            LogUtils.w(TAG, "开启挖矿");
        } else {
            switchActivityBlockBottomFrameSwitchState.setChecked(false);
            //cancelAllDigCoin();
            LogUtils.w(TAG, "关闭挖矿");
        }
    }

    @Override
    protected void onListItemClick(int position) {

    }

    @Override
    public void onRefresh() {
        adapter.clear();
        if (!isLogin()) {
            return;
        }
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            srvActivityBlockBottomFrameList.setRefreshing(false);
            return;
        }
        requestData(pageIndex);
    }

    @Override
    public void onLoadMore() {
        if (!isLogin()) {
            return;
        }
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            srvActivityBlockBottomFrameList.setRefreshing(false);
            return;
        }
        pageIndex++;
        requestData(pageIndex);
    }


    @OnClick({R.id.iv_activity_block_back,R.id.ll_activity_block_title,R.id.tv_activity_block_digSecret,
            R.id.tv_activity_block_improveForce, R.id.ll_activity_blockNew_currentCalculateForce_frame
            ,R.id.tv_activity_block_myAssets,R.id.switch_activity_block_bottomFrame_switch_state,
            R.id.switch_activity_block_bottomFrame_music})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_activity_block_back:
                finish();
                break;
            /*点击头部跳转*/
            case R.id.ll_activity_block_title:
                String mineInfoUrl = getConfiguration().getMineInfoUrl();
                Intent intent2Desc = new Intent(mContext, WebDetailActivity.class);
                intent2Desc.putExtra("title", "矿区介绍");
                intent2Desc.putExtra("url", mineInfoUrl);
                startActivity(intent2Desc);
                break;
            /*挖矿秘籍*/
            case R.id.tv_activity_block_digSecret:
                String guidesUrl = getConfiguration().getGuidesUrl();
                Intent intent2Detail = new Intent(mContext, WebDetailActivity.class);
                intent2Detail.putExtra("title", "挖矿秘籍");
                intent2Detail.putExtra("url", guidesUrl);
                startActivity(intent2Detail);
                break;
            /*我的资产*/
            case R.id.tv_activity_block_myAssets:
               if (LoginUtils.isLogin(BlockActivity.this)){
                   Utils.IntentUtils(mContext, BlockAssetsActivity.class);
               }
                break;
                /*提升算力*/
            case R.id.tv_activity_block_improveForce:
                if (LoginUtils.isLogin(BlockActivity.this)){
                    Intent upintent = new Intent(mContext,ForceScoreUpActivity.class);
                    startActivity(upintent);
                }

                break;
            /*当前算力*/
            case R.id.ll_activity_blockNew_currentCalculateForce_frame:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                if (mDigPageInfo == null){
                    showToast("挖矿详情信息为空！");
                    return;
                }
                Intent intent = new Intent(mContext,MineForceActivity.class);
                intent.putExtra("force",mDigPageInfo.getSoulVal());
                startActivity(intent);
                break;
            /*自动挖矿开关*/
            case R.id.switch_activity_block_bottomFrame_switch_state:
                if (LoginUtils.isLogin(BlockActivity.this)){
                    if (!isDiging) {
                        autoDig();
                    } else {
                        showCloseSwitchDialog();
                    }
                }else {
                    switchActivityBlockBottomFrameSwitchState.setChecked(false);
                }

                break;
            case R.id.switch_activity_block_bottomFrame_music:
                if (LoginUtils.isLogin(BlockActivity.this)){
                    if (switchActivityBlockBottomFrameMusic.isChecked()){
                        Preferences.setBlockBgMusic(true);
                        currentLevel = Preferences.getCurrentLevel();
                        playMusic(currentLevel);
                    }else {
                        Preferences.setBlockBgMusic(false);
                        mediaPause();
                    }
                }else {
                    switchActivityBlockBottomFrameMusic.setChecked(false);
                }
                break;
            default:
                break;
        }
    }



    /**
     * 请求数据
     */
    private void requestData(int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getDigRecordList(Preferences.getAccessToken(), map, new getBeanCallback<DigRecord>() {
            @Override
            public void onSuccess(DigRecord digRecord) {
                List<DigRecord.ListBean> list = digRecord.getList();
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

    private void getDigPageInfo(int currentLevel) {
        if (!isLogin()) {
            switch2CurrentLevelFrame(null);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("syetemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("level", currentLevel > 0 ? currentLevel + "" : "");
        NetWorks.getDigPageInfo(Preferences.getAccessToken(), map, new getBeanCallback<DigPageInfo>() {
            @Override
            public void onSuccess(DigPageInfo digPageInfo) {
                if (digPageInfo != null) {
                    switch2CurrentLevelFrame(digPageInfo);
                } else {
                    switch2CurrentLevelFrame(null);
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int coinType = v.getId();
        if (coinType < 0) {
            return;
        }
        getBlockCollection(coinType);
    }

    /**
     * 收矿
     */
    private void getBlockCollection(final int coinType) {

        //showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getBlock(Preferences.getAccessToken(), map, new getBeanCallback<BlockCollection>() {
            @Override
            public void onSuccess(BlockCollection blockCollection) {
                //hideLoading();
                if (blockCollection == null) {
                    blockCollection = new BlockCollection();
                }
                String collAmt = blockCollection.getCollAmt();
                Double collAmtInt = MathUtils.string2Double(collAmt);
                if (collAmtInt != null && collAmtInt > 0) {
                    currentLevel = Preferences.getCurrentLevel();
                    playCollectMusic(currentLevel);
                    removeBlock2Map(coinType);
                    showToast("这时间刚巧，你来的正好。<br>" +
                            "收获" + collAmt + "个" + getCoinName(coinType));
                } else if (!isDiging) {
                    removeBlock2Map(coinType);
                } else {
                    showToast("矿产正在挖掘中...");
//                    playCollectMusic(currentLevel);
//                    playMusic(currentLevel);
//                    currentLevel++;
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                //hideLoading();
                onFail(errorCode, reason);
            }
        });
    }

    private void showLevelDialog(int currentLevel, String currentLevelName, int levelChg, String honorPic, List<Integer> coinTypeChg) {
        if (levelChangeDialog != null && levelChangeDialog.isShowing()) {
            levelChangeDialog.dismiss();
        }
        levelChangeDialog = new LevelChangeDialog.Builder(mContext)
                .setCurrentLevel(currentLevel)
                .setCurrentLevelName(currentLevelName)
                .setLevelChg(levelChg)
                .setCoinTypeChg(coinTypeChg)
                .setHonorPic(honorPic)
                .setCancelAble(false)
                .setClickListener(new LevelChangeDialog.UpOrDownOnClick() {
                    @Override
                    public void upClick() {
                        levelChangeDialog.dismiss();
                    }

                    @Override
                    public void downClick() {
                        if (!isLogin()) {
                            showToast(R.string.unlogin);
                            return;
                        }
                        // 跳转算力提升页面
                        Utils.IntentUtils(mContext, ForceScoreUpActivity.class);
                        levelChangeDialog.dismiss();
                    }
                }).create();
        levelChangeDialog.show();
    }


    /**
     * 自动挖矿switch状态监听
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.switch_activity_block_bottomFrame_switch_state) {
            if (isChecked) {
               tvActivityBlockBottomFrameSwitchState .setText("自动挖矿中");
            } else {
                tvActivityBlockBottomFrameSwitchState.setText("自动挖矿关");
            }
        }

        if (buttonView.getId() == R.id.switch_activity_block_bottomFrame_music) {
            if (isChecked) {
                tvActivityBlockBottomFrameSwitchMusic.setText("音效开");
            } else {
                tvActivityBlockBottomFrameSwitchMusic.setText("音效关");
            }
        }

    }

    MediaPlayer mediaPlayer;
    int currentMedia = 0;

    private void playMusic(int level) {
        LogUtils.e(TAG, "矿背景等级：" + level);
        if (!Preferences.isOpenBlockBgMusic()) {
            return;
        }
        if (level <= 0) {
            return;
        }

        if (!isFront) {
            return;
        }
        int mediaRes = getMediaRes(level);
        if (mediaRes == 0) {
            return;
        }
        if (currentMedia == mediaRes) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(mContext, mediaRes);
                mediaPlayer.setLooping(true);
            }
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } else {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
            }
            currentMedia = mediaRes;
            mediaPlayer = MediaPlayer.create(mContext, mediaRes);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    private void mediaPause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void mediaStop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    //创建一个SoundPool对象
    SoundPool soundPool;
    //定义一个HashMap用于存放音频流的ID
    HashMap<Integer, Integer> musicId = new HashMap<Integer, Integer>();

    private void initSoundPool(int level) {
        if (level <= 0) {
            return;
        }
        if (soundPool == null) {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
//            musicId.put(1, soundPool.load(mContext, R.raw.block_collect_1, 1));
//            musicId.put(2, soundPool.load(mContext, R.raw.block_collect_2, 1));
//            musicId.put(3, soundPool.load(mContext, R.raw.block_collect_3, 1));
        }
//        if (musicId.get(level) == null){
//            musicId.put(level, soundPool.load(mContext, getCollectMusic(level), 1));
//        }
    }

    private void playCollectMusic(final int level) {
        LogUtils.e(TAG, "收矿等级：" + level);
        if (!Preferences.isOpenBlockBgMusic()) {
            return;
        }
        if (level <= 0) {
            return;
        }
        if (soundPool == null) {
            initSoundPool(level);
        }
        try {
            if (musicId.get(level) == null) {
                musicId.clear();
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                        soundPool.play(musicId.get(level), 1, 1, 0, 0, 1);
                    }
                });
                musicId.put(level, soundPool.load(mContext, getCollectMusic(level), 1));
            } else {
                soundPool.play(musicId.get(level), 1, 1, 0, 0, 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void collectMusicRelease() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }


    private int getMediaRes(int level) {
        int resid = 0;
        switch (level) {
            case 1:
                resid = R.raw.block_level_1;
                break;
            case 2:
                resid = R.raw.block_level_2;
                break;
            case 3:
                resid = R.raw.block_level_3;
                break;
            case 4:
                resid = R.raw.block_level_4;
                break;
            case 5:
                resid = R.raw.block_level_5;
                break;
            case 6:
                resid = R.raw.block_level_6;
                break;
            case 7:
                resid = R.raw.block_level_7;
                break;
            case 8:
                resid = R.raw.block_level_8;
                break;
            case 9:
                resid = R.raw.block_level_9;
                break;
            case 10:
                resid = R.raw.block_level_10;
                break;
            default:
                resid = R.raw.block_level_1;
                break;
        }
        return resid;
    }

    private int getCollectMusic(int level) {
        int resid = 0;
        switch (level) {
            case 1:
                resid = R.raw.block_collect_1;
                break;
            case 2:
                resid = R.raw.block_collect_2;
                break;
            case 3:
                resid = R.raw.block_collect_3;
                break;
            case 4:
                resid = R.raw.block_collect_4;
                break;
            case 5:
                resid = R.raw.block_collect_5;
                break;
            case 6:
                resid = R.raw.block_collect_6;
                break;
            case 7:
                resid = R.raw.block_collect_7;
                break;
            case 8:
                resid = R.raw.block_collect_8;
                break;
            case 9:
                resid = R.raw.block_collect_9;
                break;
            case 10:
                resid = R.raw.block_collect_10;
                break;
            default:
                resid = R.raw.block_collect_1;
                break;
        }
        return resid;
    }
}
