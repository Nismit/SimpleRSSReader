package ca.nismit.simplerssreader.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
    static final String TAG = Utils.class.getSimpleName();
    private static final int READ_TIMEOUT_MS = 15000; // Mili Sec = 15 Sec
    private static final int CONNECTION_TIMEOUT_MS = 10000; // Mili Sec = 10 Sec

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
            Log.e(TAG, "FEED URL IS MALFORMED: " + e.getMessage());
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

    public static boolean networkCheck(Context context){
        ConnectivityManager cm =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info != null) {
            return info.isConnected();
        } else {
            return false;
        }
    }
}
