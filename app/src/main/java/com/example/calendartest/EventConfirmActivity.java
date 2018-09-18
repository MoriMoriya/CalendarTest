package com.example.calendartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class EventConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_confirm);

        //EventConfirm eventconfirm = (EventConfirm) new EventConfirm().execute();

        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,EventConfirm.list);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
