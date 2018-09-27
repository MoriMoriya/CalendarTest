package com.example.calendartest;

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
 * Created by 171y065 on 2018/09/26.
 */

public class EventDelete extends  AsyncTask<Integer,Integer,Integer> {
    GoogleAccountCredential mcredential = InsertActivity.credential;
    com.google.api.services.calendar.Calendar service;

    int Position = 0;

    public EventDelete(int position) {
        this.Position = position;
    }


    @Override
    protected Integer doInBackground(Integer... integers) {
        mcredential.setSelectedAccountName(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        Calendar service = new Calendar.Builder(transport, jsonFactory, mcredential).setApplicationName("Calendartest").build();

        String pageToken = null;
        ArrayList Dlist = new ArrayList();

        //イベントの取得
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
                Log.d(TAG,event.getSummary()+event.getId(),null);
                if(event.getSummary() != null) {
                        Dlist.add(event.getId());
                }
            }
            pageToken = events.getNextPageToken();
        }while(pageToken != null );

        //取得したイベントを削除する
        try {
            service.events().delete("primary", (String) Dlist.get(Position)).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
