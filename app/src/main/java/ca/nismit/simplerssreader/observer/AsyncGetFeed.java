package ca.nismit.simplerssreader.observer;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Observable;

import ca.nismit.simplerssreader.orma.Feed;
import ca.nismit.simplerssreader.orma.Feed_Relation;
import ca.nismit.simplerssreader.rss.RssItem;
import ca.nismit.simplerssreader.rss.RssReader;

public class AsyncGetFeed extends Observable {
    private static final String TAG = AsyncGetFeed.class.getSimpleName();

    public enum Event { START, PROGRESS, FAILURE, FINISH }
    protected List<RssItem> items;
    private int numURLs = 0;
    private int finishedURLs = 0;

    public void taskStart(Feed_Relation relation) {
        setChanged();
        notifyObservers(Event.START);
        // init Numbers
        numURLs = 0;
        finishedURLs = 0;

        // TODO
        // CHECK FEED URL DB
        List<Feed> feedList = Feed.relationGetAll(relation);

        // Set total feed URLs
        numURLs = feedList.size();

        if (numURLs == 0) {
            // There's no data
            taskFailed();
        } else {
            for (int i = 0; i < numURLs; i++) {
                String url = feedList.get(i).url;
                //Log.d(TAG, "URL: "+ url);
                GetFeedData getFeedData = new GetFeedData();
                getFeedData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            }
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
    }

    public void taskSkipped() {
        Log.d(TAG, "taskSkipped");

        // Something wrong a feed
        // it will be skipped to fetch the feed.
        finishedURLs++;

        if(finishedURLs == numURLs) {
            taskFinish();
        }
    }

    public void taskFinish() {
        setChanged();
        notifyObservers(Event.FINISH);
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
            taskSkipped();
            super.onCancelled();
        }
    }
}
