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
        Log.d(TAG, "Called onCreateView");
        return inflater.inflate(R.layout.fragment_add_rss, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Called onStart");
        urlText = (EditText) getActivity().findViewById(R.id.urlText);
        addFeedButton = (Button) getActivity().findViewById(R.id.addFeedButton);
        fetchButton = (Button) getActivity().findViewById(R.id.fetchButton);
    }
}
