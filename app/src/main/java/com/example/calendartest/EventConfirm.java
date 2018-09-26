package com.example.calendartest;

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
import static com.example.calendartest.R.id.listView;

/**
 * Created by 171y065 on 2018/09/18.
 */

class EventConfirm extends AsyncTask<String,Integer,ArrayList> {
    GoogleAccountCredential mcredential = InsertActivity.credential;
    com.google.api.services.calendar.Calendar service;

    EventConfirmActivity mEventConfirmActivity;


    public EventConfirm(EventConfirmActivity context) {
        super();
        mEventConfirmActivity = context;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        mcredential.setSelectedAccountName(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        Calendar service = new Calendar.Builder(transport, jsonFactory, mcredential).setApplicationName("Calendartest").build();

        String pageToken = null;
        ArrayList list = new ArrayList();

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
                    list.add(event.getSummary());
                }
            }
            pageToken = events.getNextPageToken();
        }while(pageToken != null );
        return list;
    }

    @Override
    protected void onPostExecute(ArrayList result){
        ListView lv = mEventConfirmActivity.findViewById(listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mEventConfirmActivity, android.R.layout.simple_list_item_1, result);
        lv.setAdapter(adapter);
    }
}
