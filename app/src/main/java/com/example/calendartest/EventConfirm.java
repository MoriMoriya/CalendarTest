package com.example.calendartest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

class EventConfirm extends  AsyncTask<Integer,Integer,Integer> {
    private GoogleAccountCredential mcredential = InsertActivity.credential;
    com.google.api.services.calendar.Calendar service;

    static ArrayList<String> list = new ArrayList<String>();


    private Context context;
    private int i=0;

    EventConfirm(Context context){
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        mcredential.setSelectedAccountName(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        ConfirmEvent();
        return 0;
    }

    private void ConfirmEvent() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        Calendar service = new Calendar.Builder(transport, jsonFactory, mcredential)
                .setApplicationName("CalendarTest").build();

        String pageToken = null;

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
    }
}
