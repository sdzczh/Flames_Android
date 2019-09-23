package app.com.pgy.im.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Widgets.RoundImageView;
import app.com.pgy.im.server.response.GetRedPacketStateResponse;

/**
 * @author xuqingzhong
 *         红包详情页
 */
public class RedPackageDetailActivity extends BaseActivity {

    /**顶部*/
    @BindView(R.id.activity_redPackageDetail_icon)
    ImageView activityRedPackageDetailIcon;
    @BindView(R.id.activity_redPackageDetail_userName)
    TextView activityRedPackageDetailUserName;
    @BindView(R.id.activity_redPackageDetail_content)
    TextView activityRedPackageDetailContent;
    /**点击自己的红包已经被领取界面*/
    @BindView(R.id.activity_redPackageDetail_mineReceivedFrame_title)
    TextView activityRedPackageDetailMineReceivedFrameTitle;
    @BindView(R.id.activity_redPackageDetail_mineReceivedFrame_icon)
    RoundImageView activityRedPackageDetailMineReceivedFrameIcon;
    @BindView(R.id.activity_redPackageDetail_mineReceivedFrame_userName)
    TextView activityRedPackageDetailMineReceivedFrameUserName;
    @BindView(R.id.activity_redPackageDetail_mineReceivedFrame_time)
    TextView activityRedPackageDetailMineReceivedFrameTime;
    @BindView(R.id.activity_redPackageDetail_mineReceivedFrame_amount)
    TextView activityRedPackageDetailMineReceivedFrameAmount;
    @BindView(R.id.activity_redPackageDetail_mineReceivedFrame_priceOfCNY)
    TextView activityRedPackageDetailMineReceivedFramePriceOfCNY;
    @BindView(R.id.activity_redPackageDetail_mineReceivedFrame)
    LinearLayout activityRedPackageDetailMineReceivedFrame;
    /**点击自己的红包尚未被领取界面*/
    @BindView(R.id.activity_redPackageDetail_mineUnReceivedFrame_title)
    TextView activityRedPackageDetailMineUnReceivedFrameTitle;
    @BindView(R.id.activity_redPackageDetail_mineUnReceivedFrame)
    FrameLayout activityRedPackageDetailMineUnReceivedFrame;
    /**点击别人的红包自己领取界面*/
    @BindView(R.id.activity_redPackageDetail_otherReceivedFrame_amount)
    TextView activityRedPackageDetailOtherReceivedFrameAmount;
    @BindView(R.id.activity_redPackageDetail_otherReceivedFrame_coinName)
    TextView activityRedPackageDetailOtherReceivedFrameCoinName;
    @BindView(R.id.activity_redPackageDetail_otherReceivedFrame_priceOfCNY)
    TextView activityRedPackageDetailOtherReceivedFramePriceOfCNY;
    @BindView(R.id.activity_redPackageDetail_otherReceivedFrame)
    LinearLayout activityRedPackageDetailOtherReceivedFrame;
    /**是否是别人发的红包*/
    private boolean isReceive;
    private GetRedPacketStateResponse redDetail;

    @Override
    public int getContentViewId() {
        return R.layout.activity_im_red_package_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        isReceive = getIntent().getBooleanExtra("isReceive",false);
        redDetail = (GetRedPacketStateResponse) getIntent().getSerializableExtra("detail");
        if (redDetail == null){
            redDetail = new GetRedPacketStateResponse();
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ImageLoaderUtils.displayCircle(mContext,activityRedPackageDetailIcon,redDetail.getHeadPath());
        activityRedPackageDetailUserName.setText(redDetail.getName()+"的红包");
        activityRedPackageDetailContent.setText(redDetail.getNote());
        if (isReceive){
            /*别人的已领取*/
            activityRedPackageDetailOtherReceivedFrame.setVisibility(View.VISIBLE);
            activityRedPackageDetailMineReceivedFrame.setVisibility(View.GONE);
            activityRedPackageDetailMineUnReceivedFrame.setVisibility(View.GONE);
            /*设置界面数据*/
            activityRedPackageDetailOtherReceivedFrameAmount.setText(redDetail.getAmount());
            activityRedPackageDetailOtherReceivedFrameCoinName.setText(getCoinName(redDetail.getCoinType()));
            activityRedPackageDetailOtherReceivedFramePriceOfCNY.setText("≈¥"+redDetail.getAmtOfCny());
        }else{
            switch (redDetail.getState()){
                case 0:
                    /*自己的未领取*/
                    activityRedPackageDetailMineUnReceivedFrame.setVisibility(View.VISIBLE);
                    activityRedPackageDetailMineReceivedFrame.setVisibility(View.GONE);
                    activityRedPackageDetailOtherReceivedFrame.setVisibility(View.GONE);
                    /*设置界面数据*/
                    activityRedPackageDetailMineUnReceivedFrameTitle.setText(redDetail.getNum()+"个红包共"+redDetail.getAmount()+getCoinName(redDetail.getCoinType()));
                    break;
                case 1:
                    /*自己的已经领取*/
                    activityRedPackageDetailMineReceivedFrame.setVisibility(View.VISIBLE);
                    activityRedPackageDetailMineUnReceivedFrame.setVisibility(View.GONE);
                    activityRedPackageDetailOtherReceivedFrame.setVisibility(View.GONE);
                    /*设置界面数据*/
                    activityRedPackageDetailMineReceivedFrameTitle.setText(redDetail.getNum()+"个红包共"+redDetail.getAmount()+getCoinName(redDetail.getCoinType()));
                    List<GetRedPacketStateResponse.ReciveListBean> reciveList = redDetail.getReciveList();
                    if (reciveList == null){
                        reciveList = new ArrayList<>();
                    }
                    GetRedPacketStateResponse.ReciveListBean reciveListBean = reciveList.size()>0?reciveList.get(0):null;
                    if (reciveListBean == null){
                        reciveListBean = new GetRedPacketStateResponse.ReciveListBean();
                    }
                    ImageLoaderUtils.displayCircle(mContext,activityRedPackageDetailMineReceivedFrameIcon,reciveListBean.getHeadPath());
                    activityRedPackageDetailMineReceivedFrameUserName.setText(reciveListBean.getName());
                    activityRedPackageDetailMineReceivedFrameTime.setText(reciveListBean.getReciveTime());
                    activityRedPackageDetailMineReceivedFrameAmount.setText(reciveListBean.getAmount()+getCoinName(reciveListBean.getCoinType()));
                    activityRedPackageDetailMineReceivedFramePriceOfCNY.setText("≈¥"+reciveListBean.getAmtOfCny());
                    break;
                case 2:
                    break;
                    default:break;
            }
        }
    }




    @OnClick({R.id.activity_redPackageDetail_titleBack, R.id.activity_redPackageDetail_titleRight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.activity_redPackageDetail_titleBack:
                RedPackageDetailActivity.this.finish();
                break;
                /*红包详情*/
            case R.id.activity_redPackageDetail_titleRight:
                Intent intent2Record = new Intent(mContext, RedPackageRecordsActivity.class);
                intent2Record.putExtra("isRedPacket",true);
                startActivity(intent2Record);
                break;
                default:break;
        }
    }
}