package com.example.calendartest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 171y065 on 2018/09/18.
 */

class EventConfirm extends  AsyncTask<String,Integer,ArrayList> {
    private GoogleAccountCredential mcredential = InsertActivity.credential;
    com.google.api.services.calendar.Calendar service;

    private EventConfirmActivity mEventConfirmActivity = new EventConfirmActivity();
    private ListView lv;
    private Context context;



    private int i=0;

    EventConfirm(EventConfirmActivity context){
        super();
        mEventConfirmActivity = context;
    }

    @Override
    protected ArrayList doInBackground(String... params) {
        mcredential.setSelectedAccountName(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        Calendar service = new Calendar.Builder(transport, jsonFactory, mcredential)
                .setApplicationName("CalendarTest").build();

        String pageToken = null;
        ArrayList<String> list = new ArrayList<String>();

        do {
            Events events = null;
            try {
                events = service.events().list("primary").setPageToken(pageToken).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert events != null;
            List<Event> items = events.getItems();

            for (Event event : items) {
                Log.d(TAG, event.getSummary());
                list.add(event.getSummary() +"\n" + event.getDescription() + "\n" + event.getStart().getDateTime());
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        return list;
    }

    /*
    private ArrayList<String> ConfirmEvent() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        Calendar service = new Calendar.Builder(transport, jsonFactory, mcredential)
                .setApplicationName("CalendarTest").build();

        String pageToken = null;
        ArrayList<String> list = new ArrayList<String>();

        do {
            Events events = null;
            try {
                events = service.events().list("primary").setPageToken(pageToken).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert events != null;
            List<Event> items = events.getItems();

            for (Event event : items) {
                Log.d(TAG, event.getSummary());
                list.add(event.getSummary());
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        return list;
    }
    */
    @Override
    protected void onPostExecute(ArrayList result){
        ListView lv= (ListView) mEventConfirmActivity.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mEventConfirmActivity,android.R.layout.simple_list_item_1,result);
        lv.setAdapter(adapter);
    }
}
