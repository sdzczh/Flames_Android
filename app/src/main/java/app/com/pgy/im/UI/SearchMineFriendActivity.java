package app.com.pgy.im.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.dao.query.QueryBuilder;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Widgets.RoundImageView;
import app.com.pgy.im.SealUserInfoManager;
import app.com.pgy.im.db.DBManager;
import app.com.pgy.im.db.Friend;
import app.com.pgy.im.db.FriendDao;
import app.com.pgy.im.model.SearchResult;
import app.com.pgy.im.server.pinyin.CharacterParser;

/**
 * 搜索我的好友
 *
 * @author xuqingzhong
 */

public class SearchMineFriendActivity extends BaseActivity {
    @BindView(R.id.layout_searchTitle_input)
    EditText layoutSearchTitleInput;
    @BindView(R.id.activity_searchFriend_list)
    ListView mFriendListView;
    @BindView(R.id.activity_searchFriend_noResults)
    TextView activitySearchFriendNoResults;
    private CharacterParser mCharacterParser;
    private String mFilterString;
    private AsyncTask mAsyncTask;
    private ThreadPoolExecutor mExecutor;
    private ArrayList<Friend> mFilterFriendList;

    @Override
    public int getContentViewId() {
        return R.layout.activity_im_search_mine_friend;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        mExecutor = new ThreadPoolExecutor(3, 5, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        mCharacterParser = CharacterParser.getInstance();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        layoutSearchTitleInput.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(layoutSearchTitleInput, 0);
        mFriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object selectObject = parent.getItemAtPosition(position);
                if (selectObject instanceof Friend) {
                    Friend friend = (Friend) selectObject;
                    Intent intent = new Intent(mContext, UserDetailActivity.class);
                    intent.putExtra("friendPhone", friend.getUserId());
                    startActivity(intent);
                }
            }
        });

        layoutSearchTitleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFilterFriendList = new ArrayList<>();
                mFilterString = s.toString();
                mAsyncTask = new AsyncTask<String, Void, SearchResult>() {
                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected SearchResult doInBackground(String... params) {
                        return filterInfo(mFilterString);
                    }

                    @Override
                    protected void onPostExecute(SearchResult searchResult) {
                        if (searchResult == null || TextUtils.isEmpty(mFilterString)) {
                            activitySearchFriendNoResults.setVisibility(View.GONE);
                            mFriendListView.setVisibility(View.GONE);
                        } else if (searchResult.getFilterStr().equals(mFilterString)) {
                            List<Friend> filterFriendList = searchResult.getFilterFriendList();
                            for (Friend friend : filterFriendList) {
                                mFilterFriendList.add(friend);
                            }
                            if (mFilterFriendList.size() > 0) {
                                /*搜到用户了，显示列表隐藏noResult*/
                                activitySearchFriendNoResults.setVisibility(View.GONE);
                                mFriendListView.setVisibility(View.VISIBLE);
                                FriendListAdapter friendListAdapter = new FriendListAdapter(mFilterFriendList, mFilterString);
                                mFriendListView.setAdapter(friendListAdapter);
                            } else {
                                /*没找到用户，隐藏列表显示noResult*/
                                mFriendListView.setVisibility(View.GONE);
                                activitySearchFriendNoResults.setVisibility(View.VISIBLE);
                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                                spannableStringBuilder.append(getResources().getString(R.string.ac_search_no_result_pre));
                                SpannableStringBuilder colorFilterStr = new SpannableStringBuilder(mFilterString);
                                colorFilterStr.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), 0, mFilterString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                spannableStringBuilder.append(colorFilterStr);
                                spannableStringBuilder.append(getResources().getString(R.string.ac_search_no_result_suffix));
                                activitySearchFriendNoResults.setText(spannableStringBuilder);
                            }

                        }
                    }
                }.executeOnExecutor(mExecutor, s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        layoutSearchTitleInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(layoutSearchTitleInput.getWindowToken(), 0);
                    filterInfo(layoutSearchTitleInput.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });


    }

    private synchronized SearchResult filterInfo(String filterStr) {
        SearchResult searchResult = new SearchResult();
        if (TextUtils.isEmpty(filterStr)) {
            return searchResult;
        }

        /**
         *从数据库里边查询符合条件的数据
         */
        QueryBuilder queryBuilder = DBManager.getInstance().getDaoSession().getFriendDao().queryBuilder();
        //List<Friend> filterFriendList = queryBuilder.where(queryBuilder.or(FriendDao.Properties.Name.like("%" + filterStr + "%"),FriendDao.Properties.UserId.like("%" + filterStr + "%"))).orderAsc(FriendDao.Properties.UserId).build().list();
        List<Friend> filterFriendList = queryBuilder.where(FriendDao.Properties.UserId.like("%" + filterStr + "%")).orderAsc(FriendDao.Properties.UserId).build().list();
        searchResult.setFilterStr(filterStr);
        searchResult.setFilterFriendList(filterFriendList);
        return searchResult;
    }

    /**
     * 点击顶部取消按钮
     */
    @OnClick(R.id.layout_searchTitle_cancel)
    public void onViewClicked() {
        SearchMineFriendActivity.this.finish();
    }

    /**
     * 搜索到好友列表适配器
     */
    private class FriendListAdapter extends BaseAdapter {
        private List<Friend> filterFriendList;
        private String fiterStr;

        private FriendListAdapter(List<Friend> filterFriendList) {
            this.filterFriendList = filterFriendList;
        }

        public FriendListAdapter(ArrayList<Friend> mFilterFriendList, String mFilterString) {
            this.filterFriendList = mFilterFriendList;
            this.fiterStr = mFilterString;
        }

        @Override
        public int getCount() {
            if (filterFriendList != null) {
                return filterFriendList.size();
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            Friend friendInfo = (Friend) getItem(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(SearchMineFriendActivity.this, R.layout.item_filter_friend_list, null);
                viewHolder.portraitImageView = convertView.findViewById(R.id.item_aiv_friend_image);
                viewHolder.nameTextView = convertView.findViewById(R.id.item_tv_friend_name);
                viewHolder.phoneTextView = convertView.findViewById(R.id.item_tv_friend_phone);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(friendInfo);
            ImageLoaderUtils.displayCircle(mContext, viewHolder.portraitImageView, portraitUri);
            viewHolder.nameTextView.setText(friendInfo.getDisplayName());
            String userPhone = friendInfo.getUserId();
            SpannableString spannableString = new SpannableString(userPhone);
            spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.txt_blue)), 0, fiterStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.phoneTextView.setText(spannableString);
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            if (filterFriendList == null) {
                return null;
            }
            if (position >= filterFriendList.size()) {
                return null;
            }
            return filterFriendList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    class ViewHolder {
        RoundImageView portraitImageView;
        TextView nameTextView;
        TextView phoneTextView;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus() && event.getAction() == MotionEvent.ACTION_UP) {
            /*点击空白位置 隐藏软键盘*/
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(layoutSearchTitleInput.getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        layoutSearchTitleInput.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(layoutSearchTitleInput, 0);
        super.onResume();
    }

    @Override
    public void onPause() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(layoutSearchTitleInput.getWindowToken(), 0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
            mAsyncTask = null;
        }
        super.onDestroy();
    }
}
