package ca.nismit.simplerssreader.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.nismit.simplerssreader.R;
import ca.nismit.simplerssreader.orma.Feed;

public class AddFragment extends Fragment {
    static final String TAG = AddFragment.class.getSimpleName();

    private EditText titleText, urlText, categoryText;
    private Button addFeedButton;

    public AddFragment() {
    }

    public static AddFragment newInstance() {
        return new AddFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_rss, container, false);

        urlText = (EditText) v.findViewById(R.id.urlText);
        titleText = (EditText) v.findViewById(R.id.titleText);
        categoryText = (EditText) v.findViewById(R.id.autoCompleteCatText);
        addFeedButton = (Button) v.findViewById(R.id.addFeedButton);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Called onStart");

        addFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(urlText.getText().toString() == null || urlText.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "URL field is empty, Can not add URL", Toast.LENGTH_SHORT).show();
                } else {
                    addFeedUrl();
                }

            }
        });

        getActivity().setTitle("ADD FEED");
    }

    @UiThread
    private void addFeedUrl() {
        String title = titleText.getText().toString();
        String url = urlText.getText().toString();
        String category = categoryText.getText().toString();

        InputMethodManager inputMethodManage = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManage.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (url.startsWith("http://") || url.startsWith("https://")) {
            // Insert data to database
            final Feed feed = Feed.createFeed();
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
                            Toast.makeText(getActivity().getApplicationContext(), "DATA INSERTED!", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        }

                    });
                }
            }).start();
        } else {
            Toast.makeText(getActivity(), "URL ERROR. Please make sure correct full url", Toast.LENGTH_SHORT).show();
        }

    }
}
