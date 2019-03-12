package su.zzz.android.currencysalemonitormgn;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MonitorActivity extends AppCompatActivity {
    private static final String TAG = MonitorActivity.class.getSimpleName();
    Button buttonGetXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        buttonGetXml = findViewById(R.id.button_get_xml);
        buttonGetXml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
                new FetchCourseTask().execute();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
    private class FetchCourseTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground: ");
            try {
                new CourseFetcher().fetch();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
