package ca.nismit.simplerssreader.rss;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

public class RssReader {
    // For Debug
    private static final String TAG = "BackgroundTask";

    private static final int READ_TIMEOUT_MS = 15000; // Mili Sec = 15 Sec
    private static final int CONNECTION_TIMEOUT_MS = 10000; // Mili Sec = 10 Sec

    private String url;
    private List<RssItem> items;

    public RssReader(String url) {
        this.url = url;
    }

    public List<RssItem> getItems() {
        try {
            InputStream stream = null;
            URL urlAddress = new URL(this.url);
            XmlParser xmlParser = new XmlParser();

            try {
                Log.d(TAG, "TRY ACCESS TO:" + urlAddress);
                stream = getHTTPStream(urlAddress);
                items = xmlParser.parse(stream);
                return items;
            } finally {
                if(stream != null) {
                    stream.close();
                }
            }
        }catch (MalformedURLException e) {
            Log.e(TAG, "FEED URL IS MALFORMED: ", e);
        }catch (IOException e) {
            Log.e(TAG, "ERROR FROM NETWORK: " + e.getMessage());
        }catch (XmlPullParserException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private InputStream getHTTPStream(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(READ_TIMEOUT_MS);
        conn.setConnectTimeout(CONNECTION_TIMEOUT_MS);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        final int status = conn.getResponseCode();
        //Log.d(TAG, "CHECK CONNECTION CODE:" + status);
        if (status == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }else {
            throw new IOException("CONNECTION ERROR, RESPONSE CODE: " + status);
        }
    }
}
