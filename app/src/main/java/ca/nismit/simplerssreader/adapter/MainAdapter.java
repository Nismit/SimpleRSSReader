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

import java.util.List;

import ca.nismit.simplerssreader.R;
import ca.nismit.simplerssreader.rss.RssItem;

public class MainAdapter extends BaseAdapter {
    static final String TAG = MainAdapter.class.getSimpleName();
    Context context;
    LayoutInflater layoutInflater = null;
    List<RssItem> items;


    public MainAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d(TAG, "Created main adapter");
    }

    public void setMainAdapater(List<RssItem> items) {
        this.items = items;
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
