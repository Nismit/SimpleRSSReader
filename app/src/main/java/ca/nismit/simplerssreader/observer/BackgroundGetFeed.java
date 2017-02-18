package ca.nismit.simplerssreader.observer;

import android.os.AsyncTask;

import java.util.List;
import java.util.Observable;

import ca.nismit.simplerssreader.rss.RssItem;
import ca.nismit.simplerssreader.rss.RssReader;

public class BackgroundGetFeed extends Observable {
    // For Debug
    private static final String TAG = BackgroundGetFeed.class.getSimpleName();

    public enum Event { START, FINISH, }
    private List<RssItem> items;

    public void taskStart(String url) {
        setChanged();
        notifyObservers(Event.START);
        GetFeedTask getFeedTask = new GetFeedTask();
        getFeedTask.execute(url);
    }

    public void setItems(List<RssItem> items) {
        this.items = items;
        setChanged();
        notifyObservers(Event.FINISH);
    }

    public List<RssItem> getItems() {
        return this.items;
    }

    class GetFeedTask extends AsyncTask<String, Void, List<RssItem>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<RssItem> doInBackground(String... params) {
            RssReader reader = new RssReader(params[0]);
            return reader.getItems();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<RssItem> arrayList) {
            //super.onPostExecute(arrayList);
            setItems(arrayList);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
