package ca.nismit.simplerssreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ca.nismit.simplerssreader.R;
import ca.nismit.simplerssreader.adapter.MainAdapter;
import ca.nismit.simplerssreader.observer.BackgroundGetFeed;
import ca.nismit.simplerssreader.rss.RssItem;
import ca.nismit.simplerssreader.util.Utils;

public class MainFragment extends Fragment {
    static final String TAG = MainFragment.class.getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    BackgroundGetFeed backgroundGetFeed = new BackgroundGetFeed();
    MainAdapter mainAdapter;
    ListView mListView;
    boolean flag = false;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    // Calls at once
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Called onCreate");
        init();
        if (Utils.networkCheck(this.getContext())) {
            kicks();
        } else {
            Toast.makeText(getActivity(), "Internet is not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Called onCreateView");
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) v.findViewById(R.id.f_listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Called onStart");

        if (flag) {
            setResult(backgroundGetFeed.getItems());
        }
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            kicks2();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    void init() {
        Log.d(TAG, "Called init()");
        mainAdapter = new MainAdapter(getContext());
    }

    private void kicks() {
        backgroundGetFeed.addObserver(observer);
        //backgroundGetFeed.taskStart("http://android-developers.blogspot.com/atom.xml");
        //backgroundGetFeed.taskStart("https://www.smashingmagazine.com/feed/");
        //backgroundGetFeed.taskStart("http://feeds2.feedburner.com/tympanus");
        backgroundGetFeed.taskStart("http://feeds2.feedburner.com/webdesignerdepot");
        //backgroundGetFeed.taskStart("http://gihyo.jp/design/feed/atom");
    }

    private void kicks2() {
        backgroundGetFeed.taskStart("https://www.smashingmagazine.com/feed/");
        //backgroundGetFeed.taskStart("http://gihyo.jp/design/feed/atom");
    }

    private void test(List<RssItem> items) {
        mainAdapter.setMainAdapater(items);
        mainAdapter.notifyDataSetChanged();
    }

    private void setResult(List<RssItem> items) {
        mainAdapter.setMainAdapater(items);
        mListView.setAdapter(mainAdapter);
    }

    private Observer observer = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            if(!(arg instanceof BackgroundGetFeed.Event)){
                return;
            }

            switch ((BackgroundGetFeed.Event)arg) {
                case START:
                    break;
                case FINISH:
                    if (!flag) {
                        setResult(backgroundGetFeed.getItems());
                        flag = true;
                    } else {
                        test(backgroundGetFeed.getItems());
                    }
                    break;
            }
        }
    };
}