package ca.nismit.simplerssreader.rss;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
        byte[] byteArray = getByteArrayUrlData(this.url);
        //Log.d(TAG, "Byte Array: " + byteArray);
        String data = new String(byteArray);
        try {
            XmlParser xmlParser = new XmlParser();
            items = xmlParser.parse(data);
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getByteArrayUrlData(String urlAddress) {
        byte[] line = new byte[1024];
        byte[] result = null;
        int size = 0;
        HttpURLConnection conn = null;
        InputStream stream = null;
        ByteArrayOutputStream outputStream = null;

        try {
            URL url = new URL(urlAddress);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT_MS);
            conn.setConnectTimeout(CONNECTION_TIMEOUT_MS);
            conn.setRequestMethod("GET");
            conn.connect();
            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                stream = conn.getInputStream();
                outputStream = new ByteArrayOutputStream();
                while (true) {
                    size = stream.read(line);
                    if (size <= 0) {
                        break;
                    }

                    outputStream.write(line, 0, size);
                }
                result = outputStream.toByteArray();
            } else {
                throw new IOException("CONNECTION ERROR, RESPONSE CODE: " + status);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "FEED URL IS MALFORMED: ", e);
        } catch (IOException e) {
            Log.e(TAG, "ERROR FROM NETWORK: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "OTHER ERROR: ", e);
        } finally {
            try {
                if (conn != null) { conn.disconnect(); }
                if (stream != null) { stream.close(); }
                if (outputStream != null) { outputStream.close(); }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
