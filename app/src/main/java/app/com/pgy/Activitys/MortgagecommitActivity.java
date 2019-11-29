package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Adapters.MortgagedateAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Models.Beans.MortgageinfoBean;
import app.com.pgy.Models.ResultBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ToastUtils;
import app.com.pgy.Widgets.CoinTypeListPopupwindow;
import butterknife.BindView;
import butterknife.OnClick;

public class MortgagecommitActivity extends BaseActivity {

    @BindView(R.id.ll_back)
    ImageView llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tv_everyday_info)
    TextView tvEverydayInfo;
    @BindView(R.id.tv_yesterday)
    TextView tvYesterday;
    @BindView(R.id.tv_todayrate)
    TextView tvTodayrate;
    @BindView(R.id.tv_diya_bizhongyue)
    TextView tvDiyaBizhongyue;
    @BindView(R.id.tv_diya_yue)
    TextView tvDiyaYue;
    @BindView(R.id.edt_diya_num)
    EditText edtDiyaNum;
    @BindView(R.id.tv_diya_numall)
    TextView tvDiyaNumall;
    @BindView(R.id.rv_date)
    RecyclerView rvDate;
    @BindView(R.id.tv_diya_earningsdate)
    TextView tvDiyaEarningsdate;
    @BindView(R.id.iv_agreement)
    ImageView ivAgreement;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.tv_diya_enter)
    TextView tvDiyaEnter;
    @BindView(R.id.tv_diya_date1)
    TextView tvDiyaDate1;
    @BindView(R.id.tv_diya_date2)
    TextView tvDiyaDate2;
    @BindView(R.id.tv_diya_date3)
    TextView tvDiyaDate3;
    private int coinType;
    private List<Integer> cycle;
    private Integer diyaDate = 0;//抵押天数
    private Map<Integer, Double> rateMap;
    private Double rate = 0.0;//当前所选日利率
    private double amount;//可用余额

    private double postamount;
    private boolean agreement = false;//是否同意抵押合约
    private String agreement1 = "";//协议weburl
    private boolean isChange = false;//输入数量是否经过整千处理

    @Override
    public int getContentViewId() {
        return R.layout.activity_mortgagecommit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle bundle = getIntent().getExtras();
//        coinType = bundle.getInt("coinType");
        coinType = Preferences.getDiyaCoin();
        tvTitle.setText("抵押挖矿(" + getCoinName(coinType) + ")");
        tvEverydayInfo.setText("预计每日收益("+getCoinName(coinType)+")");
        edtDiyaNum.setHint("1000 "+getCoinName(coinType)+"起");
        tvDiyaBizhongyue.setText(getCoinName(coinType) + " 可用余额");

        getData();

//        edtDiyaNum.

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.tv_diya_date1, R.id.tv_diya_date2, R.id.tv_diya_date3, R.id.ll_back, R.id.tv_title, R.id.tv_title_right, R.id.tv_diya_numall, R.id.iv_agreement, R.id.tv_agreement, R.id.tv_diya_enter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_title:
                showSpinner();
                break;
            case R.id.tv_title_right:
                Intent intent2 = new Intent(this, MortgageorderActivity.class);

//                Bundle bundle = new Bundle();
//                bundle.putInt("coinType", coinType);
//                intent2.putExtras(bundle);

                startActivity(intent2);
                break;
            case R.id.tv_diya_numall:
                edtDiyaNum.setText(tvDiyaYue.getText().toString());
                calculate();
                break;
            case R.id.iv_agreement:
                calculate();
                if (agreement) {
                    agreement = false;
                    ivAgreement.setImageDrawable(getResources().getDrawable(R.mipmap.diya_butongyi));
                } else {
                    agreement = true;
                    ivAgreement.setImageDrawable(getResources().getDrawable(R.mipmap.diya_tongyi));
                }
                break;
            case R.id.tv_agreement:
                Intent intent = new Intent(this, WebDetailActivity.class);
                intent.putExtra("title", "抵押挖矿协议");
                intent.putExtra("url", agreement1);
                startActivity(intent);
                break;
            case R.id.tv_diya_enter:
                calculate();
                if (edtDiyaNum.getText().toString().length() == 0) {
                    showToast("请输入抵押数量");

                } else {
                    if (agreement) {
                        if (!isChange) {
                            mortgageconmit();
                        }
                    } else {
                        showToast("请先阅读并同意抵押挖矿协议");
                    }
                }


                break;
            case R.id.tv_diya_date1:
                chooseDate(1);
                break;
            case R.id.tv_diya_date2:
                chooseDate(2);
                break;
            case R.id.tv_diya_date3:
                chooseDate(3);
                break;
        }
    }

    /**
     * 选择抵押期限
     *
     * @param date
     */
    private void chooseDate(int date) {
        DecimalFormat dfs = new DecimalFormat("#.##");
        switch (date) {
            case 1:
                diyaDate = cycle.get(0);
                rate = rateMap.get(diyaDate);
                tvTodayrate.setText(dfs.format(rate * 100) + "%");
                tvDiyaDate1.setBackground(getResources().getDrawable(R.mipmap.bg_date_b));
                tvDiyaDate2.setBackground(getResources().getDrawable(R.mipmap.bg_date_w));
                tvDiyaDate3.setBackground(getResources().getDrawable(R.mipmap.bg_date_w));
                calculate();
                break;
            case 2:
                diyaDate = cycle.get(1);
                rate = rateMap.get(diyaDate);
                tvTodayrate.setText(dfs.format(rate * 100) + "%");
                tvDiyaDate1.setBackground(getResources().getDrawable(R.mipmap.bg_date_w));
                tvDiyaDate2.setBackground(getResources().getDrawable(R.mipmap.bg_date_b));
                tvDiyaDate3.setBackground(getResources().getDrawable(R.mipmap.bg_date_w));
                calculate();
                break;
            case 3:
                diyaDate = cycle.get(2);
                rate = rateMap.get(diyaDate);
                tvTodayrate.setText(dfs.format(rate * 100) + "%");
                tvDiyaDate1.setBackground(getResources().getDrawable(R.mipmap.bg_date_w));
                tvDiyaDate2.setBackground(getResources().getDrawable(R.mipmap.bg_date_w));
                tvDiyaDate3.setBackground(getResources().getDrawable(R.mipmap.bg_date_b));
                calculate();
                break;
        }
    }


    /**
     * 计算预计每日收益
     */
    private void calculate() {
        isChange = false;
        String strDiyanum = edtDiyaNum.getText().toString();
        int intDiyanum = 0;

        if (strDiyanum.length() > 0) {
//            intDiyanum = Integer.parseInt(strDiyanum);
            intDiyanum = Double.valueOf(strDiyanum).intValue();
            if (intDiyanum < 1000) {
                Toast.makeText(mContext, "抵押数量必须是1000的整数倍", Toast.LENGTH_SHORT).show();
                strDiyanum = "1000";
                isChange = true;
            } else {
                if (!strDiyanum.substring(strDiyanum.length() - 3, strDiyanum.length()).equals("000")) {
                    strDiyanum = intDiyanum + "";
                    strDiyanum = strDiyanum.substring(0, strDiyanum.length() - 3) + "000";
                    Toast.makeText(mContext, "抵押数量必须是1000的整数倍", Toast.LENGTH_SHORT).show();
                    isChange = true;
                }
            }
            intDiyanum = Integer.parseInt(strDiyanum);
            edtDiyaNum.setText(strDiyanum);
        }
        if (edtDiyaNum.getText().toString().length() > 0) {
            DecimalFormat dfs = new DecimalFormat("0.00");
            postamount = rate * intDiyanum;
            tvYesterday.setText(dfs.format(postamount));
        } else {
            tvYesterday.setText("0.00");
        }
    }


    private void getData() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        NetWorks.mortgageinfo(Preferences.getAccessToken(), map, new getBeanCallback<MortgageinfoBean>() {
            @Override
            public void onSuccess(MortgageinfoBean mortgageinfoBean) {
//                String json = "{\"data\":{\"date\":\"2019-11-28\",\"amount\":1246.04832560,\"agreement\":\"https://pgy.zendesk.com/hc/zh-cn/articles/360036754292-抵押挖矿协议\",\"rate\":{\"30\":0.02,\"60\":0.022,\"90\":0.025},\"cycle\":[30,60,90]},\"msg\":\"成功\",\"code\":10000}";
                amount = mortgageinfoBean.getAmount();
                cycle = mortgageinfoBean.getCycle();
                if (cycle.size() == 3) {
                    tvDiyaDate1.setText(cycle.get(0) + "天");
                    tvDiyaDate2.setText(cycle.get(1) + "天");
                    tvDiyaDate3.setText(cycle.get(2) + "天");
                }
                diyaDate = cycle.get(0);
                rateMap = mortgageinfoBean.getRate();
                rate = rateMap.get(diyaDate);
                DecimalFormat dfs = new DecimalFormat("#.##");
                DecimalFormat dfs2 = new DecimalFormat("0.00");
                tvTodayrate.setText(dfs.format(rate * 100) + "%");
                tvDiyaYue.setText(dfs2.format(amount));
                tvDiyaEarningsdate.setText("预计" + mortgageinfoBean.getDate() + "计算收益，赎回需要完成抵押合约");
                agreement1 = mortgageinfoBean.getAgreement();

                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
                finish();
            }
        });
    }

    private void mortgageconmit() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("amount", edtDiyaNum.getText().toString());
        map.put("rate", rate);
        map.put("time", diyaDate);
        NetWorks.mortgageconmit(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o ) {
                showToast("提交成功");
                hideLoading();
                finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
                finish();
            }
        });
    }

    private CoinTypeListPopupwindow coinspinner;

    private void showSpinner() {
        if (getMortgageList() == null || getMortgageList().size() <= 0) {
            return;
        }
        if (coinspinner == null) {
            coinspinner = new CoinTypeListPopupwindow(mContext, getMortgageList(), new spinnerSingleChooseListener() {
                @Override
                public void onItemClickListener(int position) {
                    coinspinner.dismiss();
                    if (getMortgageList().get(position) == coinType) {
                        return;
                    }
                    coinType = getMortgageList().get(position);
                    Preferences.setDiyaCoin(coinType);
                    tvTitle.setText("抵押挖矿(" + getCoinName(coinType) + ")");
                    tvEverydayInfo.setText("预计每日收益("+getCoinName(coinType)+")");
                    edtDiyaNum.setHint("1000 "+getCoinName(coinType)+"起");
                    tvDiyaBizhongyue.setText(getCoinName(coinType) + " 可用余额");

                    edtDiyaNum.setText("");
                    getData();
                }
            });
        }
        coinspinner.setSelectCoin(coinType);
        if (!coinspinner.isShowing()) {
            coinspinner.showDown(viewLine);
        }
    }

}
