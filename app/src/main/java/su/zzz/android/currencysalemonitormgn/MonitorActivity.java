package su.zzz.android.currencysalemonitormgn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import su.zzz.android.currencysalemonitormgn.database.MonitorDbHelper;
import su.zzz.android.currencysalemonitormgn.database.MonitorDbSchema;

public class MonitorActivity extends AppCompatActivity {
    private static final String TAG = MonitorActivity.class.getSimpleName();
    Button buttonUpdate;
    Button buttonWriteDb;
    Button buttonReadDb;
    TextView tv_usd;
    TextView tv_eur;
    CheckBox cb_eur;
    CheckBox cb_usd;
    EditText et_usd;
    EditText et_eur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        tv_usd = findViewById(R.id.tv_usd);
        tv_eur = findViewById(R.id.tv_eur);

        cb_usd = findViewById(R.id.cb_usd);
        cb_eur = findViewById(R.id.cb_eur);

        cb_usd.setChecked(MonitorPreferences.getUsdMonitorState(getApplicationContext()));
        cb_eur.setChecked(MonitorPreferences.getEurMonitorState(getApplicationContext()));

        CompoundButton.OnCheckedChangeListener cb_listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.getId() == R.id.cb_usd){
                    MonitorPreferences.setUsdMonitorState(getApplicationContext(), isChecked);
                }
                if(buttonView.getId() == R.id.cb_eur){
                    MonitorPreferences.setEurMonitorState(getApplicationContext(), isChecked);
                }
                boolean cb_status = cb_usd.isChecked() || cb_eur.isChecked();
                boolean monitor_status = MonitorService.isServiceAlarmOn(getApplicationContext());
                if(cb_status != monitor_status) {
                    MonitorService.setServiceAlarm(getApplicationContext(), cb_status);
                }
            }
        };

        cb_usd.setOnCheckedChangeListener(cb_listener);
        cb_eur.setOnCheckedChangeListener(cb_listener);

        et_usd = findViewById(R.id.et_usd);
        et_eur = findViewById(R.id.et_eur);
        et_usd.setText(String.format("%.2f",MonitorPreferences.getUsdExpectedCourse(getApplicationContext())));
        et_eur.setText(String.format("%.2f",MonitorPreferences.getEurExpectedCourse(getApplicationContext())));
        et_usd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    MonitorPreferences.setUsdExpectedCourse(getApplicationContext(), Float.valueOf(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_eur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0) {
                    MonitorPreferences.setEurExpectedCourse(getApplicationContext(), Float.valueOf(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        buttonWriteDb = findViewById(R.id.button_write_db);
        buttonWriteDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "buttonWriteDb.onClick: ");
                new FetchCourseTask().execute();
            }
        });

        buttonReadDb = findViewById(R.id.button_read_db);
        buttonReadDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "buttonReadDb.onClick: ");
                new GetMinCourseTask().execute();
            }
        });

        buttonUpdate = findViewById(R.id.b_refresh);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "buttonUpdate.onClick: ");
                startService(MonitorService.newIntent(getApplicationContext()));
            }
        });
        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(MonitorService.ACTION_UPDATE_COURSE);
        registerReceiver(onCourseUpdate, filter, null, null);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(onCourseUpdate);
    }

    private class FetchCourseTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground: ");
            try {
                new CourseFetcher().fetch(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class GetMinCourseTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground: ");
            try {
                MonitorDbHelper.getInstance(getApplicationContext()).getMinCourseList(MonitorDbSchema.CourseTable.Cols.USD);
                MonitorDbHelper.getInstance(getApplicationContext()).getMinCourseList(MonitorDbSchema.CourseTable.Cols.EUR);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void updateUI(){
        tv_usd.setText(String.format("%.2f", MonitorDbHelper.getInstance(getApplicationContext()).getMinCourse(MonitorDbSchema.CourseTable.Cols.USD)));
        tv_eur.setText(String.format("%.2f", MonitorDbHelper.getInstance(getApplicationContext()).getMinCourse(MonitorDbSchema.CourseTable.Cols.EUR)));
        String sb_text = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(MonitorPreferences.getCourseFetchDate(getApplicationContext()))+" : "+(MonitorPreferences.getCourseFetchSuccess(getApplicationContext())?"Success":"Fail");
        Snackbar.make(findViewById(R.id.ll_monitor), sb_text, Snackbar.LENGTH_INDEFINITE)
                .show();
    }

    private BroadcastReceiver onCourseUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: onCourseUpdate");
            updateUI();
        }
    };
}
