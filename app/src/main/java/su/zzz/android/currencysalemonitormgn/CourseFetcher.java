package su.zzz.android.currencysalemonitormgn;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CourseFetcher {
    private static final String TAG = CourseFetcher.class.getSimpleName();
    private static final String COURSE_SOURCE_URL = "https://informer.kovalut.ru/webmaster/xml-table.php?kod=7416";
    private String getXml() throws IOException {
        URL url = new URL(COURSE_SOURCE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Log.i(TAG, "getXml: getResponseCode:"+connection.getResponseCode()+":"+HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            Log.i(TAG, "getXml: Exception: "+e);
        } finally {
            connection.disconnect();
        }
        return "";
    }
    public void fetch() throws IOException {
        getXml();
    }
}
