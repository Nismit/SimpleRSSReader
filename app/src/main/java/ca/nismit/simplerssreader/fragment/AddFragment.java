package ca.nismit.simplerssreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.nismit.simplerssreader.R;
import ca.nismit.simplerssreader.orma.FeedUrlStore;
import ca.nismit.simplerssreader.orma.OrmaDatabase;

public class AddFragment extends Fragment {
    static final String TAG = AddFragment.class.getSimpleName();

    EditText titleText, urlText;
    Button addFeedButton;

    public AddFragment() {
    }

    public static AddFragment newInstance() {
        return new AddFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Called onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Called onCreateView");
        return inflater.inflate(R.layout.fragment_add_rss, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Called onStart");

        initOrma();

        urlText = (EditText) getActivity().findViewById(R.id.urlText);
        titleText = (EditText) getActivity().findViewById(R.id.titleText);
        addFeedButton = (Button) getActivity().findViewById(R.id.addFeedButton);
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
    }

    @UiThread
    private void addFeedUrl() {
        Log.d(TAG, "Call addFeedUrl");
        String url = urlText.getText().toString();
        if (url.startsWith("http://") || url.startsWith("https://")) {
            // Insert data to database
            final FeedUrlStore feedData = new FeedUrlStore(url);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    feedData.insertData();
                    //Log.d(TAG,"YEAH");
                    Log.d(TAG, "run: しましたね");
                }
            }).start();

            //Toast.makeText(getActivity(), "INSERT DATA TO DATABASE", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "URL ERROR. Please make sure correct url", Toast.LENGTH_SHORT).show();
        }

    }

    void initOrma() {
        FeedUrlStore.initRelaion(getContext());
    }
}
