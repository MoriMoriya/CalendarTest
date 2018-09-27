package com.example.calendartest;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by 171y065 on 2018/09/14.
 */

public class EventInsert extends AsyncTask<Integer,Integer,Integer> {
    private GoogleAccountCredential mcredential = InsertActivity.credential;
    com.google.api.services.calendar.Calendar service;

    private String flag = "false";

    private Context context;
    private Database helper;

    EventInsert(Context context){
        this.context = context;
    }
    @Override
    protected Integer doInBackground(Integer... integers) {
        mcredential.setSelectedAccountName(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        createEvent();

        return 0;
    }

    private void createEvent() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, mcredential)
                .setApplicationName("CalendarTest")
                .build();

        String StartTime = String.valueOf(R.id.StartTimeText);
        String EndTime = String.valueOf(R.id.EndTimetext);
        String Day = String.valueOf(R.id.DateText);

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

        //イベントの追加
        Event event = new Event()
                .setSummary("アルバイト")
                .setLocation("")
                .setColorId("3")
                .setDescription(String.valueOf(money) + "円");

        DateTime startDateTime = new DateTime(InsertActivity.EDate + "T" + InsertActivity.EStartTime + "+09:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Tokyo");
        event.setStart(start);

        DateTime endDateTime = new DateTime(InsertActivity.EDate + "T" +InsertActivity.EEndTime + "+09:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Tokyo");
        event.setEnd(end);

        String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=1"};
        event.setRecurrence(Arrays.asList(recurrence));


        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("popup").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(60),
        };

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        try {
            service.events().insert(calendarId, event).execute();
            flag = "true";
        } catch (IOException e) {
            e.printStackTrace();
            flag = "false";
        }
    }

    //追加できた際の処理、追加できなかった際の処理
    @Override
    protected void onPostExecute(Integer result){
        if(flag.equals("true")){
            Toast.makeText(context,"予定入力成功",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"予定入力失敗",Toast.LENGTH_LONG).show();
        }
    }
}

