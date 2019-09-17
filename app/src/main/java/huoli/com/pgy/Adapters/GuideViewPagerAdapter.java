package huoli.com.pgy.Adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 引导页viewPager适配器
 * @author xuqingzhong
 */
public class GuideViewPagerAdapter extends PagerAdapter {
    /**获得导航页的图片集*/
    private List<View> views;

    public GuideViewPagerAdapter(List<View> views)
    {
        this.views = views;
    }

    //获得界面个数
    @Override
    public int getCount() {

        //返回图片页数
        return views.size();
    }

    //初始化position位置的界面
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(views.get(position));
        return views.get(position);
    }

    //判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    //对不在界面内的导航页进行删除
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
