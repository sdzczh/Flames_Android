package app.com.pgy.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**定义ViewPager的适配器
 * @author xuqingzhong
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    //数据，添加fragment到适配器中
    private List<Fragment> mFragments = new ArrayList<>();
    //数据，添加标题到适配器中
    private List<String> mFragmentTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**添加fragment和标题，填入数据*/
    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
        notifyDataSetChanged();
    }

    public void removeAll(){
        mFragments.clear();
        mFragmentTitles.clear();
        notifyDataSetChanged();
    }

    //获取每个fragment
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    //获取viewpager的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles == null || mFragmentTitles.size() <= position?"": mFragmentTitles.get(position);
    }

}
