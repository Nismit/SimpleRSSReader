package ca.nismit.simplerssreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.nismit.simplerssreader.MainActivity;
import ca.nismit.simplerssreader.R;

public class DisplayTestFragment extends Fragment {
    static final String TAG = MainActivity.class.getSimpleName();

    public DisplayTestFragment() {
    }

    public static DisplayTestFragment newInstance() {
        return new DisplayTestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Started onCreateView");
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_display_test, container, false);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Started onStart");
        super.onStart();
    }
}
