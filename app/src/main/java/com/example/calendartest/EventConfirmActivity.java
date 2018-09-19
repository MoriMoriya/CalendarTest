package com.example.calendartest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class EventConfirmActivity extends AppCompatActivity {

    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_confirm);
        ListView listView = (ListView) findViewById(R.id.listView);

        EventConfirm eventconfirm = (EventConfirm) new EventConfirm(this).execute();

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,EventConfirm.list);

        listView.setAdapter(adapter);

    }
}
