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
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

/**
 * Created by 171y065 on 2018/09/26.
 */

public class EventDelete extends  AsyncTask<Integer,Integer,Integer> {
    GoogleAccountCredential mcredential = InsertActivity.credential;
    com.google.api.services.calendar.Calendar service;



    @Override
    protected Integer doInBackground(Integer... integers) {
        mcredential.setSelectedAccountName(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        Calendar service = new Calendar.Builder(transport, jsonFactory, mcredential).setApplicationName("Calendartest").build();

        try {
            Event event = service.events().get("primary","eventId").execute();
            Log.d("",event.getId(),null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
