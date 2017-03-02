package ca.nismit.simplerssreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ca.nismit.simplerssreader.R;
import ca.nismit.simplerssreader.adapter.EditAdapter;
import ca.nismit.simplerssreader.orma.Feed;

public class FeedsListFragment extends Fragment {
    private static final String TAG = FeedsListFragment.class.getSimpleName();

    public ListView mListView;
    private EditAdapter editAdapter;
    private List<Feed> list;
    private static final String TABLE_ID = "table_id";
    private static final String TABLE_TITLE = "table_title";
    private static final String TABLE_URL = "table_url";
    private static final String TABLE_CATEGORY = "table_category";

    public FeedsListFragment() { }

    public static FeedsListFragment newInstance() { return new FeedsListFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feeds_list, container, false);
        mListView = (ListView) v.findViewById(R.id.e_listview);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        list = Feed.relationGetAll(Feed.getRelation());
        initListView();
        mListView.setAdapter(editAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO
                // replace new fragment to edit table
                Bundle bundle = new Bundle();
                bundle.putLong(TABLE_ID, list.get(position).id);
                bundle.putString(TABLE_TITLE, list.get(position).title);
                bundle.putString(TABLE_URL, list.get(position).url);
                bundle.putString(TABLE_CATEGORY, list.get(position).category);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                EditListFragment elf = new EditListFragment();
                elf.setArguments(bundle);
                fm.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right, android.R.anim.slide_out_right,
                                R.anim.slide_in_right, android.R.anim.slide_out_right)
                        .replace(R.id.activity_main, elf)
                        .addToBackStack(null)
                        .commit();
            }
        });

        getActivity().setTitle("FEEDS LIST");
    }

    private void initListView() {
        editAdapter = new EditAdapter(
                getContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                list);
    }
}
