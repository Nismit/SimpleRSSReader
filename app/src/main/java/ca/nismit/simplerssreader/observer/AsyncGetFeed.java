package ca.nismit.simplerssreader.observer;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Observable;

import ca.nismit.simplerssreader.orma.FeedUrlStore;
import ca.nismit.simplerssreader.orma.FeedUrlStore_Relation;
import ca.nismit.simplerssreader.rss.RssItem;
import ca.nismit.simplerssreader.rss.RssReader;

public class AsyncGetFeed extends Observable {
    private static final String TAG = AsyncGetFeed.class.getSimpleName();

    public enum Event { START, PROGRESS, FAILURE, FINISH }
    protected List<RssItem> items;
    private int numURLs = 0;
    private int finishedURLs = 0;

    public void taskStart(FeedUrlStore_Relation relation) {
        setChanged();
        notifyObservers(Event.START);
        // TODO
        // CHECK FEED URL DB
        // return list or arraylist
        List<FeedUrlStore> feedList = FeedUrlStore.relationGetAll(relation);
        // set number
        numURLs = feedList.size();
        // For loop
        for (int i = 0; i < numURLs; i++) {
            String url = feedList.get(i).url;
            Log.d(TAG, "URL: "+ url);
            GetFeedData getFeedData = new GetFeedData();
            getFeedData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        }
    }

    public void taskProgress() {
        setChanged();
        notifyObservers(Event.PROGRESS);
        // Notify Feed URLs how many numbers.
        // if data returns same the numbers, run finish
        if(finishedURLs <= numURLs) {
            finishedURLs++;
        }

        if(finishedURLs == numURLs) {
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

    public void setItems(List<RssItem> items) {
        this.items = items;
    }

    public List<RssItem> getItems() {
        return items;
    }

    final class GetFeedData extends AsyncTask<String, Void, List<RssItem>> {
        @Override
        protected List<RssItem> doInBackground(String... params) {
            //Log.d(TAG, "URL: "+ params[0]);
            RssReader reader = new RssReader(params[0]);
            return reader.getItems();
        }

        @Override
        protected void onPostExecute(List<RssItem> mItems) {
            if(mItems != null) {
                setItems(mItems);
                taskProgress();
            }else {
                onCancelled();
            }
        }

        @Override
        protected void onCancelled() {
            taskFailed();
            super.onCancelled();
        }
    }
}
