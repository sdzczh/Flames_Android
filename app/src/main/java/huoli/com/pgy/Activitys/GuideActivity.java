package huoli.com.pgy.Activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import huoli.com.pgy.Activitys.Base.BaseActivity;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.Utils;

/**
 * Created by xuqingzhong on 2017/3/15.
 * 引导页
 * @author xuqingzhong
 */

public class GuideActivity extends BaseActivity {
    @BindView(R.id.activity_guide_viewpager)
    ViewPager guideVp;
//    private TextView guideLogin;
    private YindaoPagerAdapter adapter;
//    private List<View> views;

    @Override
    public int getContentViewId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        views = new ArrayList<>();
//        /*加载视图*/
//        views.add(inflater.inflate(R.layout.view_guidefirst, null));
//        views.add(inflater.inflate(R.layout.view_guidesecond, null));
//        views.add(inflater.inflate(R.layout.view_guidethird, null));
//        View thirdView = inflater.inflate(R.layout.view_guideforth, null);
//        views.add(thirdView);
//        guideLogin = (TextView) thirdView.findViewById(R.id.guide_login);
//        adapter = new GuideViewPagerAdapter(views);
        adapter = new YindaoPagerAdapter(this);
        guideVp.setAdapter(adapter);
//        guideLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.IntentUtils(mContext, MainActivity.class);
//                GuideActivity.this.finish();
//            }
//        });
    }


    class YindaoPagerAdapter extends PagerAdapter{

        int[] imgs = {R.mipmap.yindao1,R.mipmap.yindao2,R.mipmap.yindao3};
        Context mContext;
        public YindaoPagerAdapter(Context context){
            this.mContext = context;
        }
        //获得界面个数
        @Override
        public int getCount() {

            //返回图片页数
            return imgs.length;
        }

        //初始化position位置的界面
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(mContext,R.layout.view_guide_page,null);
            ImageView iv = view.findViewById(R.id.iv_guide_bg);
            TextView tv = view.findViewById(R.id.guide_login);
            if (position < imgs.length){
                iv.setImageResource(imgs[position]);
            }
            if (position == imgs.length-1){
                tv.setVisibility(View.VISIBLE);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.IntentUtils(mContext, MainActivity.class);
                        GuideActivity.this.finish();
                    }
                });
            }else {
                tv.setVisibility(View.GONE);
            }
            container.addView(view);
            return view;
//            container.addView(views.get(position));
//            return views.get(position);
        }

        //判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }

        //对不在界面内的导航页进行删除
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof View){
                container.removeView((View) object);
            }
        }
    }
}
