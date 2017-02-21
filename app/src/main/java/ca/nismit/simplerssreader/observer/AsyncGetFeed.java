package ca.nismit.simplerssreader.observer;

import android.os.AsyncTask;

import java.util.List;
import java.util.Observable;

import ca.nismit.simplerssreader.rss.RssItem;
import ca.nismit.simplerssreader.rss.RssReader;

public class AsyncGetFeed extends Observable {
    private static final String TAG = AsyncGetFeed.class.getSimpleName();

    public enum Event { START, PROGRESS, FAILURE, FINISH }
    protected List<RssItem> items;
    private int numURLs = 0;
    private int finishedURLs = 0;

    public void taskStart() {
        setChanged();
        notifyObservers(Event.START);
        // TODO
        // CHECK FEED URL DB
        // return list or arraylist

        // For loop
        String url = "url--";
        GetFeedData getFeedData = new GetFeedData();
        getFeedData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }

    public void taskProgress() {
        setChanged();
        notifyObservers(Event.PROGRESS);
        // Notify Feed URLs how many numbers.
        // if data returns same the numbers, run finish
        if(numURLs >= finishedURLs) {
            finishedURLs++;
        } else {
            taskFinish();
        }
    }

    public void taskFailed() {
        setChanged();
        notifyObservers(Event.FAILURE);
        // DB ERROR
        // FETCH ERROR..?
    }

    public void taskFinish() {
        setChanged();
        notifyObservers(Event.FINISH);
        // Got data!

    }

    final class GetFeedData extends AsyncTask<String, Void, List<RssItem>> {
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
        protected void onPostExecute(List<RssItem> mItems) {
            items = mItems;
            taskProgress();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
