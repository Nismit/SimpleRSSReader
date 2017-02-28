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
        //Log.d(TAG, "Created main adapter");
    }

    static class ViewHolder {
        ImageView iv;
        TextView title;
        TextView desc;
        TextView url;
        TextView date;
    }

    public void setMainAdapater(List<RssItem> items) {
        this.items.addAll(items);
    }

    public void clearData() {
        items.clear();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    public RssItem getListItem(int position) { return items.get(position); }

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
                //Log.d(TAG, "items.size=" + items.size() + ". compare1: "+ (int) o1.getPublished() + " - "+ (int) o2.getPublished() + " = " + (((int) o2.getPublished()) - ((int) o1.getPublished())));
                //return (int)((o2.getPublished()) - (o1.getPublished()));
                //return Long.compare(o1.getPublished(), o2.getPublished());
                if(o1.getPublished() < o2.getPublished()) {
                    return 1;
                }else if(o1.getPublished() > o2.getPublished()) {
                    return -1;
                }else {
                    return 0;
                }
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        Log.d(TAG, "Data Changed");
        super.notifyDataSetChanged();
    }

    public String trimTitle(int position, int length) {
        String title = items.get(position).getTitle();
        // Title Trim
        int titleLength = title.length();
        if(titleLength >= length){
            String titleTrim = title.substring(0, length) + "...";
            return titleTrim;
        }else{
            return title;
        }
    }

    public String trimSummary(int position, int length) {
        String summary = items.get(position).getSummary();
        // Summary Trim
        int summaryLength = summary.length();
        if (summaryLength >= length) {
            String summaryTrim = summary.substring(0, (length - 2)) + "...";
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
        View v = convertView;
        ViewHolder vh = null;

        if (v == null) {
            v = layoutInflater.inflate(R.layout.c_list_view, parent, false);
            vh = new ViewHolder();
            vh.iv = (ImageView) v.findViewById(R.id.imageView);
            vh.title = (TextView) v.findViewById(R.id.cTitle);
            vh.desc = (TextView) v.findViewById(R.id.summary);
            vh.url = (TextView) v.findViewById(R.id.siteURL);
            vh.date = (TextView) v.findViewById(R.id.sitePublishDate);
            v.setTag(vh);
        } else {
            vh = (ViewHolder) v.getTag();
        }

        items.get(position).setSummary(stipFormat(items.get(position).getSummary()));

        if(items.get(position).getThumbnail() != null) {
            vh.iv.setVisibility(View.VISIBLE);
            Picasso.with(context).load(items.get(position).getThumbnail()).into(vh.iv);
            vh.title.setText(trimTitle(position, 25));
            vh.desc.setText(trimSummary(position, 100));

        } else {
            vh.iv.setVisibility(View.GONE);
            vh.title.setText(trimTitle(position, 41));
            vh.desc.setText(trimSummary(position, 150));
        }

        vh.date.setText(items.get(position).getDate());

        return v;
    }
}
