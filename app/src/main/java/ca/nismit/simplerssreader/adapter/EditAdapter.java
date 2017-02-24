package ca.nismit.simplerssreader.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.nismit.simplerssreader.orma.FeedUrlStore;

public class EditAdapter extends ArrayAdapter<FeedUrlStore> {
    private static final String TAG = EditAdapter.class.getSimpleName();

    private List<FeedUrlStore> list;

    public EditAdapter(Context context, int resource, int textViewResourceId, List<FeedUrlStore> objects) {
        super(context, resource, textViewResourceId, objects);
        list = objects;
        Log.d(TAG, "EditAdapter: Runs");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        //Log.d(TAG, "getView: "+ list.get(position).url);
        TextView titleText = (TextView) v.findViewById(android.R.id.text1);
        TextView urlText = (TextView) v.findViewById(android.R.id.text2);

        titleText.setText(list.get(position).title);
        titleText.setTypeface(Typeface.DEFAULT_BOLD);
        urlText.setText(list.get(position).url + "\nCategory: "+ list.get(position).category);

        titleText.setPadding(7,6,7,25);
        urlText.setPadding(7,1,7,25);

        return v;
    }
}
