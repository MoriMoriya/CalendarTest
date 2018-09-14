package com.example.calendartest;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by 171y065 on 2018/09/14.
 */

public class EventInsert extends AsyncTask<Integer,Integer,Integer> {
    private GoogleAccountCredential mcredential = InsertActivity.credential;
    com.google.api.services.calendar.Calendar service;

    @Override
    protected Integer doInBackground(Integer... integers) {
        mcredential.setSelectedAccountName("171y065@epson-isc.com");
        createEvent();
        return null;
    }

    private void createEvent() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, mcredential)
                .setApplicationName("CalendarTest")
                .build();


        Event event = new Event()
                .setSummary("TESTTEST")
                .setLocation("")
                .setDescription("");

        DateTime startDateTime = new DateTime("2018-09-15T18:00:00+09:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Tokyo");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2018-09-15T20:00:00+09:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Tokyo");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
        event.setRecurrence(Arrays.asList(recurrence));

/*        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("lpage@example.com"),
                new EventAttendee().setEmail("sbrin@example.com"),
        };
        event.setAttendees(Arrays.asList(attendees));
*/
        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(30),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        try {
            service.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.d("",event.getHtmlLink());
    }

}
