package marcomessini.mybookmarks;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by marcomessini on 22/01/15.
 */
public class DownloadWS {

    public static class DownloadWebpageTask extends AsyncTask<String, Void, Integer> {
        private TaskCallback mCallback;
        private WebSite website;

        public DownloadWebpageTask(TaskCallback callback, WebSite ws) {
            website=ws;
            mCallback = callback;
        }

        @Override
        protected Integer doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                String pars= downloadUrl(urls[0]);
                int res=pars.hashCode();
                return res;

            } catch (IOException e) {
                return -1;
                //return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Integer res) {
            mCallback.done(res,website);
        }
    }

    static String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4" );
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.e( "The response is: ", ""+response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        //Reader reader = null;
        //InputStream stream = null;
        /*char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);*/
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        String ris=total.toString();
        return ris;
    }
    /*public static void startAsyncTask(String stringUrl){
        new DownloadWebpageTask().execute(stringUrl);
    }*/
}

