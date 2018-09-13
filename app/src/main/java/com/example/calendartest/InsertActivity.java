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
import com.google.api.services.calendar.CalendarScopes;
import com.google.firebase.auth.AuthCredential;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class InsertActivity extends AppCompatActivity{

    private AuthCredential ac = GoogleSignInActivity.ac;

    private static String TAG = "InsertActivity";


    private static final int MY_PERMISSION_REQUEST_WRITE_CALENDAR = 1;

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    public static GoogleAccountCredential credential;

    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    com.google.api.services.calendar.Calendar service;

    private static final String PREF_ACCOUNT_NAME = "171y065@epson-isc.com";


    private TestTask testTask;

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

                testTask = new TestTask();

            }
        });
     }

     public void createEvent(GoogleAccountCredential credential) throws IOException {
         service = new com.google.api.services.calendar.Calendar.Builder(
                 transport,jsonFactory,credential).setApplicationName("CalendarTest")
                 .build();
         credential.setSelectedAccountName("171y065@epson-isc.com");

/*         Event event = new Event()
                 .setSummary("Google I/O 2015")
                 .setLocation("800 Howard St., San Francisco, CA 94103")
                 .setDescription("A chance to hear more about Google's developer products.");

         DateTime startDateTime = new DateTime("2015-05-28T09:00:00-07:00");
         EventDateTime start = new EventDateTime()
                 .setDateTime(startDateTime)
                 .setTimeZone("America/Los_Angeles");
         event.setStart(start);

         DateTime endDateTime = new DateTime("2015-05-28T17:00:00-07:00");
         EventDateTime end = new EventDateTime()
                 .setDateTime(endDateTime)
                 .setTimeZone("America/Los_Angeles");
         event.setEnd(end);

         String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
         event.setRecurrence(Arrays.asList(recurrence));

         EventAttendee[] attendees = new EventAttendee[] {
                 new EventAttendee().setEmail("lpage@example.com"),
                 new EventAttendee().setEmail("sbrin@example.com"),
         };
         event.setAttendees(Arrays.asList(attendees));

         EventReminder[] reminderOverrides = new EventReminder[] {
                 new EventReminder().setMethod("email").setMinutes(24 * 60),
                 new EventReminder().setMethod("popup").setMinutes(10),
         };
         Event.Reminders reminders = new Event.Reminders()
                 .setUseDefault(false)
                 .setOverrides(Arrays.asList(reminderOverrides));
         event.setReminders(reminders);

         String calendarId = "primary";
         event = service.events().insert(calendarId, event).execute();
         System.out.printf("Event created: %s\n", event.getHtmlLink());
*/
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



