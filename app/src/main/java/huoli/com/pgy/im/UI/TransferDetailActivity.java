package huoli.com.pgy.im.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import huoli.com.pgy.Activitys.Base.BaseActivity;
import huoli.com.pgy.R;
import huoli.com.pgy.Widgets.TitleView;
import huoli.com.pgy.im.server.response.GetTransferStateResponse;

/***
 * @author xuqingzhong
 * 转账详情页
 */
public class TransferDetailActivity extends BaseActivity {

    @BindView(R.id.activity_imTransferDetail_title)
    TitleView activityImTransferDetailTitle;
    @BindView(R.id.activity_imTransferDetail_amount)
    TextView activityImTransferDetailAmount;
    @BindView(R.id.activity_imTransferDetail_coinName)
    TextView activityImTransferDetailCoinName;
    @BindView(R.id.activity_imTransferDetail_priceOfCNY)
    TextView activityImTransferDetailPriceOfCNY;
    @BindView(R.id.activity_imTransferDetail_time)
    TextView activityImTransferDetailTime;
    @BindView(R.id.activity_imTransferDetail_typeNote)
    TextView activityImTransferDetailTypeNote;
    private boolean isReceiver;
    private GetTransferStateResponse detail;

    @Override
    public int getContentViewId() {
        return R.layout.activity_im_transfer_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activityImTransferDetailTitle.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferDetailActivity.this.finish();
            }
        });
        activityImTransferDetailTitle.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2Record = new Intent(mContext, RedPackageRecordsActivity.class);
                intent2Record.putExtra("isRedPacket",false);
                startActivity(intent2Record);
            }
        });
    }

    @Override
    protected void initData() {
        /*true代表是别人发给自己的*/
        isReceiver = getIntent().getBooleanExtra("isReceive",false);
        detail = (GetTransferStateResponse) getIntent().getSerializableExtra("detail");
        if (detail == null){
            detail = new GetTransferStateResponse();
        }
        activityImTransferDetailTypeNote.setText(isReceiver?("已收到"+detail.getName()+"的转账"):"转账成功");
        activityImTransferDetailAmount.setText(detail.getAmount());
        activityImTransferDetailCoinName.setText(getCoinName(detail.getCoinType()));
        activityImTransferDetailPriceOfCNY.setText("≈¥"+detail.getAmtOfCny());
        activityImTransferDetailTime.setText("时间："+detail.getCreateTime());
    }

}
