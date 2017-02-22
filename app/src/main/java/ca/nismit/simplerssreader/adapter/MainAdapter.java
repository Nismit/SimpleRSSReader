package ca.nismit.simplerssreader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.nismit.simplerssreader.R;
import ca.nismit.simplerssreader.rss.RssItem;

public class MainAdapter extends BaseAdapter {
    static final String TAG = MainAdapter.class.getSimpleName();
    Context context;
    LayoutInflater layoutInflater = null;
    List<RssItem> items = new ArrayList<>();


    public MainAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d(TAG, "Created main adapter");
    }

    public void setMainAdapater(List<RssItem> items) {
        this.items.addAll(items);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getCount() {
        return items.size();
    }

    public void sortList() {
        Collections.sort(this.items, new Comparator<RssItem>() {
            @Override
            public int compare(RssItem o1, RssItem o2) {
                //Log.d(TAG, "compare1: "+ (int) o1.getPublished());
                //Log.d(TAG, "compare2: "+ (int) o2.getPublished());
                return ((int) o2.getPublished()) - ((int) o1.getPublished());
                //return (int) (o2.getPublished() - o1.getPublished());
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        Log.d(TAG, "Data Changed");
        super.notifyDataSetChanged();
    }

    public String trimTitle(int position) {
        String title = items.get(position).getTitle();
        // Title Trim
        int titleLength = title.length();
        if(titleLength >= 25){
            String titleTrim = title.substring(0, 25) + "...";
            return titleTrim;
        }else{
            return title;
        }
    }

    public String trimSummary(int position) {
        String summary = items.get(position).getSummary();
        // Summary Trim
        int summaryLength = summary.length();
        if (summaryLength >= 100) {
            String summaryTrim = summary.substring(0, 98) + "...";
            return summaryTrim;
        } else {
            return summary;
        }
    }

    public String stipFormat(String str) {
        String strip = str;
        strip = strip.replaceAll("<.+?>", "");
        strip = strip.replaceAll("[\r\n]", " ");
        strip = strip.replaceAll("  ", " ");
        strip = strip.replaceAll("&amp;", "&");
        strip = strip.replaceAll("&nbsp;", " ");
        strip = strip.trim();

        return strip;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.c_list_view, parent, false);

        items.get(position).setSummary(stipFormat(items.get(position).getSummary()));

        Picasso.with(context).load(items.get(position).getThumbnail()).into(((ImageView)convertView.findViewById(R.id.imageView)));
        ((TextView)convertView.findViewById(R.id.cTitle)).setText(trimTitle(position));
        ((TextView)convertView.findViewById(R.id.summary)).setText(trimSummary(position));
        ((TextView)convertView.findViewById(R.id.sitePublishDate)).setText(items.get(position).getDate());

        return convertView;
    }
}
