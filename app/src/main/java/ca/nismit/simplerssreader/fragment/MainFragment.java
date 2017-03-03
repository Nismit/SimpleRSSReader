package ca.nismit.simplerssreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import ca.nismit.simplerssreader.R;
import ca.nismit.simplerssreader.adapter.MainAdapter;
import ca.nismit.simplerssreader.observer.AsyncGetFeed;
import ca.nismit.simplerssreader.orma.Feed;
import ca.nismit.simplerssreader.util.Utils;

public class MainFragment extends Fragment {
    static final String TAG = MainFragment.class.getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AsyncGetFeed getFeed;
    private MainAdapter mainAdapter;
    private boolean isCache = false;
    private boolean isRefresh = false;
    public ListView mListView;

    public MainFragment() { }

    public static MainFragment newInstance() { return new MainFragment(); }

    // Calls at once
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        initOrma();
        initListView();
        initObserver();
        tableCheck();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) v.findViewById(R.id.f_listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.loaderColorOne, R.color.loaderColorTwo, R.color.loaderColorThree, R.color.loaderColorFour);
        mSwipeRefreshLayout.setRefreshing(true);
        return v;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        if(isCache) {
            Log.d(TAG, "Got Cached");
            mSwipeRefreshLayout.setRefreshing(false);
            mListView.setAdapter(mainAdapter);
        } else {
            fetchData();
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: "+ mainAdapter.getListItem(position).getLink());
            }
        });

        // Changed title on Action bar
        getActivity().setTitle("HOME");
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // if user swiped twice, it will be crash.
            // should be reload after notifydata changed
            isRefresh = true;
            fetchData();
        }
    };

    void initOrma() {
        Feed.initRelaion(getContext());
    }

    void initListView() {
        mainAdapter = new MainAdapter(getContext());
    }

    void initObserver() {
        getFeed = new AsyncGetFeed();
        getFeed.addObserver(observer);
    }

    private void tableCheck() {
        Log.d(TAG, "tableCheck: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int count = Feed.relationGetAll(Feed.getRelation()).size();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), "THERE'S NO DATA", Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            fm.beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.slide_in_down, R.anim.slide_out_down,
                                            android.R.anim.fade_in, android.R.anim.fade_out)
                                    .replace(R.id.activity_main, AddFragment.newInstance())
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });
            }
        }).start();
    }

    void fetchData() {
        if (Utils.networkCheck(this.getContext())) {
            // if it is null, get feed urls from database
            getFeed.taskStart(Feed.getRelation());
        } else {
            Toast.makeText(getActivity(), "Internet is not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void showResult() {
        mainAdapter.sortList();
        if(isRefresh) {
            Log.d(TAG, "notify data changed");
            mainAdapter.notifyDataSetChanged();
        } else {
            Log.d(TAG, "First time to setAdapter");
            mListView.setAdapter(mainAdapter);
            isCache = true;
        }

        if(mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private Observer observer = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            if(!(arg instanceof AsyncGetFeed.Event)){
                return;
            }

            switch ((AsyncGetFeed.Event)arg) {
                case START:
                    mainAdapter.clearData();
                    break;
                case PROGRESS:
                    // Show progress (2/13 Downloading.. sth like that)
                    // Log.d(TAG, "PROGRESS");
                    mainAdapter.setMainAdapater(getFeed.getItems());
                    break;
                case FAILURE:
                    Log.d(TAG, "FAILURE");
                    break;
                case FINISH:
                    // Sort array and display
                    Log.d(TAG, "SUCCESS!");
                    showResult();
                    break;
            }
        }
    };
}