package com.example.calendartest;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class InsertActivity extends AppCompatActivity {

    private AuthCredential ac = GoogleSignInActivity.ac;

    public static final AuthCredential credential = GoogleAuthProvider.getCredential(GoogleSignInActivity.mAccount.getIdToken(), null);
    private static String TAG = "InsertActivity";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    static final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    static final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    static final int MY_PERMISSION_REQUEST_WRITE_CALENDAR = 1;

    private static final String PREF_ACCOUNT_NAME = "171y065@epson-isc.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ListView listView;

        //ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_CALENDAR); パーミッションのチェック
        //final int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_CALENDAR);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSION_REQUEST_WRITE_CALENDAR);
            }
        }

/*        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
*/
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


                SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                GoogleAccountCredential Credential1 = GoogleAccountCredential.usingOAuth2(getApplication(), Arrays.asList(CalendarScopes.CALENDAR)).setBackOff(new ExponentialBackOff())
                        .setSelectedAccountName(GoogleSignInActivity.mAuth.getCurrentUser().getEmail());
                text2.setText(Credential1.getSelectedAccountName());
/*
                com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
                        transport, jsonFactory,Credential1).setApplicationName("test").build();

                //service = new Calendar.Builder(transport, jsonFactory, Credential1).setApplicationName("CalendarTest").build();

                Event event = new Event()
                        .setSummary("Google I/O 2018")
                        .setLocation("")
                        .setDescription("");

                DateTime startDateTime = new DateTime("2018-07-25T09:00:00-07:00");
                EventDateTime start = new EventDateTime()
                        .setDateTime(startDateTime)
                        .setTimeZone("America/Los_Angeles");
                event.setStart(start);

                DateTime endDateTime = new DateTime("2018-07-25T17:00:00-07:00");
                EventDateTime end = new EventDateTime()
                        .setDateTime(endDateTime)
                        .setTimeZone("America/Los_Angeles");
                event.setEnd(end);

                String[] recurrence = new String[]{"RRULE:FREQ=DIALY;COUNT=2"};
                event.setRecurrence(Arrays.asList(recurrence));

                EventAttendee[] attendees = new EventAttendee[]{
                        new EventAttendee().setEmail("lpage@example.com"),
                        new EventAttendee().setEmail("sbrin@example.com")
                };
                event.setAttendees(Arrays.asList(attendees));

                EventReminder[] reminderOverrides = new EventReminder[]{
                        new EventReminder().setMethod("email").setMinutes(24 * 60),
                        new EventReminder().setMethod("popup").setMinutes(10),
                };
                Event.Reminders reminders = new Event.Reminders()
                        .setUseDefault(false)
                        .setOverrides(Arrays.asList(reminderOverrides));

                event.setReminders(reminders);
                String calendarId = "primary";

                try {
                    event = service.events().insert(calendarId, event).execute();
                    Log.d(TAG,event.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
*/
            }
        });

    }

    public void onDateReturnValue(String Date) {        //入力された日付をtextに代入
        TextView Datetext = (TextView) findViewById(R.id.DateText);
        Datetext.setText(Date);
    }

    public void onTimeReturnValue(String startTime) {        //入力された開始時刻をtextに代入
        TextView Timetext = (TextView) findViewById(R.id.StartTimeText);
        Timetext.setText(startTime);
    }

    public void onEndTimeReturnValue(String endTime) {     //入力された終了時刻をtextに代入
        TextView endTimetext = (TextView) findViewById(R.id.EndTimetext);
        endTimetext.setText(endTime);
    }
}



