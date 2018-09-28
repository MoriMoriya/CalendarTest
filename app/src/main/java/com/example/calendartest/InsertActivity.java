package com.example.calendartest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class InsertActivity extends AppCompatActivity{


    // データーベース名
    private static final String DATABASE_NAME = "MONEY.db";
    private static final String TABLE_NAME = "Money";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME_TITLE = "date";
    private static final String COLUMN_NAME_SUBTITLE = "money";

    private static final int DATABASE_VERSION = 4;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_SUBTITLE + " INTEGER)";

    private static String TAG = "InsertActivity";


    private static final int MY_PERMISSION_REQUEST_WRITE_CALENDAR = 1;

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    public static GoogleAccountCredential credential;

    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    com.google.api.services.calendar.Calendar service;



    private EventInsert eventinsert;
    private EventConfirm eventconfirm;
    private Database helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ListView listView;

        helper = new Database(getApplicationContext());
        /*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.CALENDAR)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.CALENDAR}, MY_PERMISSION_REQUEST_WRITE_CALENDAR);
            }
        }
        */


        credential = GoogleAccountCredential.usingOAuth2(this,Collections.singleton(CalendarScopes.CALENDAR));
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);

        String setcalendarDate = "";
        final String setcalendarStartTime = "";
        String setcalendarEndTime = "";

        final TextView text = (TextView) findViewById(R.id.DateText);

        //日付の選択ダイアログの表示
        Button DateBtn = (Button) findViewById(R.id.DateButton);
        DateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        Button startBtn = (Button) findViewById(R.id.StartBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });

        //終了時刻の選択ダイアログの表示
        Button endBtn = (Button) findViewById(R.id.Endbutton);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndTimePickerDialogFragment timePicker = new EndTimePickerDialogFragment();
                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });
/*
        Button EventBtn = (Button) findViewById(R.id.Event);      //カレンダーにEventの追加
        EventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text2 = (TextView) findViewById(R.id.textView2);
                //credential.setSelectedAccountName("171y065@epson-isc.com");

                eventinsert = (EventInsert) new EventInsert().execute();

            }
        });
        */

        //予定確認ボタンが押された際のページ遷移
        Button ConfirmBtn = (Button) findViewById(R.id.Confirm);
        ConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //eventconfirm = (EventConfirm) new EventConfirm().execute();
                Intent Confirm = new Intent(InsertActivity.this, EventConfirmActivity.class);
                startActivity(Confirm);
            }
        });

        //予定確認が押された際のページ遷移
        Button dbBtn = (Button) findViewById(R.id.dbButton);
        dbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent db = new Intent(InsertActivity.this,showData.class);
                startActivity(db);
            }
        });
     }

    //選択された日付をtextに代入
    public static String EDate;
    public void onDateReturnValue(String Date) {
        TextView Datetext = (TextView) findViewById(R.id.DateText);
        Datetext.setText(Date);
        EDate = Date;
    }

    //選択された開始時刻をtextに代入
    public static String EStartTime;
    public void onTimeReturnValue(String startTime) {
        TextView Timetext = (TextView) findViewById(R.id.StartTimeText);
        Timetext.setText(startTime);
        EStartTime = startTime;

    }

    //選択された終了時刻をtextに代入
    public static String EEndTime;
    public void onEndTimeReturnValue(String endTime) {
        TextView endTimetext = (TextView) findViewById(R.id.EndTimetext);
        endTimetext.setText(endTime);
        EEndTime = endTime;
    }


    //オプションメニュー(紙飛行機の追加)
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_sub,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //オプションメニューが押された際の処理
    public static int Money;
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.actionok:
                EditText moneyText = (EditText)findViewById(R.id.money);
                Money = Integer.parseInt(moneyText.getText().toString());
                eventinsert = (EventInsert) new EventInsert(this).execute();

                long Dmoney=0;
                long money =0;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");

                //入力された時間の差を求めて給料計算をする
                try {
                    Date date = sdf.parse(InsertActivity.EDate + " " + InsertActivity.EStartTime);
                    Date Edate = sdf.parse(InsertActivity.EDate + " " + InsertActivity.EEndTime);

                    long dateTimeFrom = Edate.getTime();
                    long dateTimeTo = date.getTime();

                    long dayDiff =(dateTimeFrom - dateTimeTo) / (1000 * 60 * 60);
                    long dayDiff1 = (dateTimeFrom - dateTimeTo) / (1000 * 60) % 60;

                    Log.e(TAG, String.valueOf(dayDiff));
                    Log.e(TAG, String.valueOf(dayDiff1));

                    Dmoney = InsertActivity.Money;
                    money = Dmoney * dayDiff;

                    if(dayDiff1 == 15){
                        money += Dmoney * 0.25;
                    }else if(dayDiff1 == 30){
                        money += Dmoney * 0.5;
                    }else if(dayDiff1 == 45){
                        money += Dmoney * 0.75;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                final SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues val = new ContentValues();
                val.put("date",EDate);
                val.put("money",money);
                db.insert("Money",null,val);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



