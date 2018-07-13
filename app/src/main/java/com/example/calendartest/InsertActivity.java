package com.example.calendartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.firebase.auth.AuthCredential;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InsertActivity extends AppCompatActivity{

    HttpTransport transport = AndroidHttp.newCompatibleTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CLIENT_SECRET_DIR = "client_secret.json";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

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

                AuthCredential credential = GoogleSignInActivity.nc;
                HttpTransport transport = AndroidHttp.newCompatibleTransport();
                JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

                com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
                        transport,jsonFactory, (HttpRequestInitializer) credential).setApplicationName("test").build();

                com.google.api.services.calendar.model.Calendar calendar = new Calendar();
                calendar.setSummary("calendarSummary");
                calendar.setTimeZone("America/Los_Angeles");
                try {
                    Calendar createdCalendar = service.calendars().insert(calendar).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Event event = new Event()
                        .setSummary("Google I/O 2018")
                        .setLocation("")
                        .setDescription("");

                DateTime startDateTime = new DateTime("2018-07-20T09:00:00-07:00");
                EventDateTime start = new EventDateTime()
                        .setDateTime(startDateTime)
                        .setTimeZone("Japan/Tokyo");
                event.setStart(start);

                DateTime endDateTime = new DateTime("2018-07-20T17:00:00-07:00");
                EventDateTime end = new EventDateTime()
                        .setDateTime(endDateTime)
                        .setTimeZone("Japan/Tokyo");
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
                } catch (IOException e) {
                    e.printStackTrace();
                }


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

