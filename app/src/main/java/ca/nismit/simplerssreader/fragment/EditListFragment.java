package ca.nismit.simplerssreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.nismit.simplerssreader.R;
import ca.nismit.simplerssreader.orma.Feed;

public class EditListFragment extends Fragment {
    private static final String TAG = EditListFragment.class.getSimpleName();

    private long id;
    private EditText mTitle, mURL, mCategory;
    private Button mEditButton, mDeleteButton;
    private Bundle bundle;

    public EditListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, "onCreate: ");
        bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Control of menu
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        mTitle = (EditText) v.findViewById(R.id.editTitleText);
        mURL = (EditText) v.findViewById(R.id.editUrlText);
        mCategory = (EditText) v.findViewById(R.id.editCategoryText);

        mEditButton = (Button) v.findViewById(R.id.editButton);
        mDeleteButton = (Button) v.findViewById(R.id.deleteButton);

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTable();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTable();
            }
        });

        mTitle.setText(bundle.getString("table_title"));
        mURL.setText(bundle.getString("table_url"));
        mCategory.setText(bundle.getString("table_category"));

        id = bundle.getLong("table_id");

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("EDIT FEED");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    public void editTable() {
        String title = mTitle.getText().toString();
        String url = mURL.getText().toString();
        String category = mCategory.getText().toString();

        final Feed feed = new Feed();
        feed.setId(id);
        feed.setTitle(title);
        feed.setUrl(url);
        feed.setCategory(category);

        new Thread(new Runnable() {
            @Override
            public void run() {
                feed.save();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "DATA UPDATED!", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        }).start();

    }

    public void deleteTable() {
        final Feed feed = new Feed();
        feed.setId(id);

        new Thread(new Runnable() {
            @Override
            public void run() {
                feed.remove();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "DATA DELETED!", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        }).start();
    }
}
