package ca.nismit.simplerssreader.rss;

import android.util.Log;

import java.util.List;

import static ca.nismit.simplerssreader.util.Utils.getByteArrayUrlData;

public class RssReader {
    // For Debug
    private static final String TAG = "BackgroundTask";

    private String url;
    private List<RssItem> items;

    public RssReader(String url) {
        this.url = url;
    }

    public List<RssItem> getItems() {
        try {
            byte[] byteArray = getByteArrayUrlData(this.url);
            //Log.d(TAG, "Byte Array: " + byteArray);
            String data = new String(byteArray);
            XmlParser xmlParser = new XmlParser();
            items = xmlParser.parse(data);
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
