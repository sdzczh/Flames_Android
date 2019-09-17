package huoli.com.pgy.Widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.effects.BaseEffects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Models.Beans.Configuration;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ImageLoaderUtils;

/**
 * Created by YX on 2018/4/23.
 */

public class LevelChangeDialog extends Dialog {
    public LevelChangeDialog(@NonNull Context context) {
        super(context);
    }

    public LevelChangeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        Context context;
        UpOrDownOnClick clickLisener;
        boolean cancelAble = false;
        private int mDuration = -1;

        Configuration configuration;
        Configuration.CalculateForceLevel currentLevel;
        int level;//当前等级
        String levelName ="暂无称号";
        int levelChg;//当前用户比之前升降等级 Integer
        String honorPic;//当前称号图片地址
        List<Integer> coinTypeChg;//升级或降级后新增或减少可挖币种Type List
        List<Configuration.CoinInfo> coinChg;
        public Builder(Context context){
            this.context = context;
        }

        public Builder setCurrentLevel(int level ){
            this.level = level;
            currentLevel = getLevelDetail(level);
            if (currentLevel != null){
                levelName = currentLevel.getRolename();
            }
            return this;
        }

        public Builder setCurrentLevelName(String currentLevelName){
            this.levelName = currentLevelName;
            return this;
        }

        public Builder setLevelChg(int levelChg){
            this.levelChg = levelChg;
            return this;
        }

        public Builder setHonorPic(String honorPic){
            this.honorPic = honorPic;
            return this;
        }

        public Builder setCoinTypeChg(List<Integer> list){
            coinTypeChg = list;
            if (coinTypeChg != null){
                coinChg = new ArrayList<>();
                for (int i=0;i<coinTypeChg.size();i++){
                    Configuration.CoinInfo info = getCoinInfo(list.get(i));
                    if (info != null){
                        coinChg.add(info);
                    }
                }
            }
            return this;
        }
        public Builder setClickListener(UpOrDownOnClick buttonClickListener){
            this.clickLisener = buttonClickListener;
            return this;
        }

        public Builder setCancelAble(boolean cancelAble) {
            this.cancelAble = cancelAble;
            return this;
        }

        private void start(View view) {
            BaseEffects animator = NewEffectstype.NewFall.getAnimator();
            mDuration = 900;
            if (mDuration != -1) {
                animator.setDuration(Math.abs(mDuration));
            }
            animator.start(view);
        }

        /**获取配置文件各种信息*/
        protected Configuration getConfiguration() {
            if (configuration == null){
                configuration = Preferences.getConfiguration();
            }
            return configuration;
        }

        /**获取币种信息*/
        protected Configuration.CoinInfo getCoinInfo(int coinType){
            Map<Integer, Configuration.CoinInfo> coinInfoMap = getConfiguration().getCoinInfo();
            if (coinInfoMap == null){
                return new Configuration.CoinInfo();
            }
            Configuration.CoinInfo coinInfo = coinInfoMap.get(coinType);
            if (coinInfo == null){
                return new Configuration.CoinInfo();
            }
            return coinInfo;
        }
        /**获取对应的算力等级详情*/
        protected Configuration.CalculateForceLevel getLevelDetail(int level){
            Map<Integer, Configuration.CalculateForceLevel> calculateForceLevelMap = getConfiguration().getHonorList();
            if (calculateForceLevelMap == null){
                calculateForceLevelMap=new HashMap<>();
            }
            Configuration.CalculateForceLevel calculateForceLevel = calculateForceLevelMap.get(level);
            if (calculateForceLevel == null){
                calculateForceLevel = new Configuration.CalculateForceLevel();
            }
            return calculateForceLevel;
        }

        public LevelChangeDialog create(){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final LevelChangeDialog dialog = new LevelChangeDialog(context, R.style.LeverDialog);
            final View layout = inflater.inflate(R.layout.dialog_level_change_view, null);

            ImageView iv_level = layout.findViewById(R.id.iv_dialog_level_change_view_level);
            if (TextUtils.isEmpty(honorPic) && currentLevel != null){
                ImageLoaderUtils.display(context,iv_level,currentLevel.getRolepicurl());
            }else {
                ImageLoaderUtils.display(context,iv_level,honorPic);
            }

            TextView tv_desc = layout.findViewById(R.id.tv_dialog_level_change_view_desc);
            TextView tv_button = layout.findViewById(R.id.tv_dialog_level_change_view_button);
            if (levelChg > 0){
                String desc = "太棒了，升级到"+levelName+"级别\n"+"您获得了新矿种";
                if (coinChg != null && coinChg.size() > 0){
                    if (coinChg.size() > 1){
                        desc = desc+coinChg.get(0).getCoinname();
                        for (int i = 1;i < coinChg.size();i++){
                            desc = desc+","+coinChg.get(i).getCoinname();
                        }
                    }else if (coinChg.size() >0){
                        desc = desc+coinChg.get(0).getCoinname();
                    }
                }
                tv_desc.setText(desc);
                tv_button.setText("点击立即体验");
                tv_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (clickLisener != null){
                            clickLisener.upClick();
                        }
                    }
                });
            }else if (levelChg < 0){
                tv_desc.setText("好难过，降级到"+levelName+"级别\n"+"大侠要继续努力哟");
                tv_button.setText("提升算力");
                tv_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (clickLisener != null){
                            clickLisener.downClick();
                        }
                    }
                });
            }

            dialog.setCancelable(cancelAble);
            dialog.setOnShowListener(new OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    start(layout);
                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return dialog;
        }
    }

    public interface UpOrDownOnClick{
        //升级点击按钮
        void upClick();
        //降级点击按钮
        void downClick();
    }
}
