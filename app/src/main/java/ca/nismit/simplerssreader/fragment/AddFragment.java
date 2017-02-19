package ca.nismit.simplerssreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ca.nismit.simplerssreader.R;

public class AddFragment extends Fragment {
    static final String TAG = AddFragment.class.getSimpleName();

    EditText urlText;
    Button addFeedButton;
    Button fetchButton;

    public AddFragment() {
    }

    public static AddFragment newInstance() {
        return new AddFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Started onCreateView");
        View v = inflater.inflate(R.layout.fragment_add_rss, container, false);
        urlText = (EditText) v.findViewById(R.id.urlText);
        addFeedButton = (Button) v.findViewById(R.id.addFeedButton);
        fetchButton = (Button) v.findViewById(R.id.fetchButton);
        return v;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Started onStart");
        super.onStart();
    }
}
