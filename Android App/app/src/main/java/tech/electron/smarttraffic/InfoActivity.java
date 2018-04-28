package tech.electron.smarttraffic;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InfoActivity extends AppCompatActivity {

    private final String TAG = "Http Connection";
    private final String url = "http://43cd11f8.ngrok.io/info";

    private String actual_speed = "";
    private String actual_info = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        new AsyncHttpTask().execute(url);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;

            HttpURLConnection urlConnection = null;

            Integer result = 0;
            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode == 200) {

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                    String response = convertInputStreamToString(inputStream);

                    parseResult(response);

                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Integer result) {
            /* Download complete. Lets update UI */

            if (result == 1) {
                ((TextView) findViewById(R.id.speed_text_view)).setText(actual_speed);
                ((TextView) findViewById(R.id.info_text_view)).setText(actual_info);

                ((TextView) findViewById(R.id.cnx_status_button)).setBackground(getDrawable(R.drawable.green_rounded_textview));
            } else {
                ((TextView) findViewById(R.id.cnx_status_button)).setBackground(getDrawable(R.drawable.red_rounded_textview));
                Log.e(TAG, "Failed to fetch data!");
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new AsyncHttpTask().execute(url);
                }
            }, 1000);
        }
    }


    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private void parseResult(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            actual_speed = jsonObject.getString("vitesse");
            actual_info = jsonObject.getString("info");
            Log.println(Log.INFO, TAG, "Vitesse : " + actual_speed + ", Info : " + actual_info);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}