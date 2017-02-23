package ca.nismit.simplerssreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ca.nismit.simplerssreader.R;
import ca.nismit.simplerssreader.adapter.EditAdapter;
import ca.nismit.simplerssreader.orma.FeedUrlStore;
import ca.nismit.simplerssreader.orma.FeedUrlStore_Relation;

public class EditFragment extends Fragment {
    private static final String TAG = EditFragment.class.getSimpleName();

    public ListView mListView;
    private EditAdapter editAdapter;
    private List<FeedUrlStore> list;

    public EditFragment() { }

    public static EditFragment newInstance() { return new EditFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOrma();
        list = FeedUrlStore.relationGetAll(FeedUrlStore.getRelation());
        initListView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        mListView = (ListView) v.findViewById(R.id.e_listview);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mListView.setAdapter(editAdapter);

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),"Touched!",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    void initListView() {
        editAdapter = new EditAdapter(
                getContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                list);

    }

    void initOrma() {
        FeedUrlStore.initRelaion(getContext());
    }
}
