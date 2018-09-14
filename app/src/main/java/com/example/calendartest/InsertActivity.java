package com.example.calendartest;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;

import java.util.Collections;
import java.util.List;


public class InsertActivity extends AppCompatActivity{


    private static String TAG = "InsertActivity";


    private static final int MY_PERMISSION_REQUEST_WRITE_CALENDAR = 1;

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    public static GoogleAccountCredential credential;

    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    com.google.api.services.calendar.Calendar service;



    private EventInsert eventinsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ListView listView;


        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.CALENDAR)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.CALENDAR}, MY_PERMISSION_REQUEST_WRITE_CALENDAR);
            }
        }

        credential = GoogleAccountCredential.usingOAuth2(this,Collections.singleton(CalendarScopes.CALENDAR));
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);


        String setcalendarDate = "";
        final String setcalendarStartTime = "";
        String setcalendarEndTime = "";

        final TextView text = (TextView) findViewById(R.id.DateText);

        Button DateBtn = (Button) findViewById(R.id.DateButton);        //日付の選択画面
        DateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });


        Button startBtn = (Button) findViewById(R.id.StartBtn);     //開始時刻の選択画面
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });

        Button endBtn = (Button) findViewById(R.id.Endbutton);      //終了時刻の選択画面
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndTimePickerDialogFragment timePicker = new EndTimePickerDialogFragment();
                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });

        Button EventBtn = (Button) findViewById(R.id.Event);      //カレンダーにEventの追加
        EventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text2 = (TextView) findViewById(R.id.textView2);
                //credential.setSelectedAccountName("171y065@epson-isc.com");

                eventinsert = (EventInsert) new EventInsert().execute();

            }
        });
     }

    public static String EDate;
    public void onDateReturnValue(String Date) {            //入力された日付をtextに代入
        TextView Datetext = (TextView) findViewById(R.id.DateText);
        Datetext.setText(Date);
        EDate = Date;
    }

    public static String EStartTime;
    public void onTimeReturnValue(String startTime) {        //入力された開始時刻をtextに代入
        TextView Timetext = (TextView) findViewById(R.id.StartTimeText);
        Timetext.setText(startTime);
        EStartTime = startTime;
    }
    public static String EEndTime;
    public void onEndTimeReturnValue(String endTime) {     //入力された終了時刻をtextに代入
        TextView endTimetext = (TextView) findViewById(R.id.EndTimetext);
        endTimetext.setText(endTime);
        EEndTime = endTime;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_sub,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.actionok:
                eventinsert = (EventInsert) new EventInsert().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



