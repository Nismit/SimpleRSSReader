package ca.nismit.simplerssreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ca.nismit.simplerssreader.R;

public class EditListFragment extends Fragment {
    private static final String TAG = EditListFragment.class.getSimpleName();

    EditText mTitle, mURL, mCategory;
    Bundle bundle;

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

        mTitle.setText(bundle.getString("siteTitle"));
        mURL.setText(bundle.getString("siteURL"));
        mCategory.setText(bundle.getString("category"));

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
}
