package app.com.pgy.Widgets;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import android.view.ViewGroup;

import app.com.pgy.Utils.ToolsUtils;

/**
 * 创建日期：2018/7/9 0009 on 下午 8:53
 * 描述:
 *
 * @author xu
 */

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;//需要添加到上面的Fragment
    private List<String> mFragmentTitles = new ArrayList<>();

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    /**
     * 自定义的构造函数
     * @param fm
     * @param fragments ArrayList<Fragment>
     */
    public MyViewPagerAdapter(FragmentManager fm,List<Fragment> fragments,List<Integer> coins) {
        super(fm);
        this.fragments = fragments;
        mFragmentTitles.clear();
        for (int coinType:coins){
            mFragmentTitles.add(ToolsUtils.getCoinName(coinType));
        }
    }


    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);//返回Fragment对象
    }
    @Override
    public int getCount() {
        return fragments.size();//返回Fragment的个数
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    //获取viewpager的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
