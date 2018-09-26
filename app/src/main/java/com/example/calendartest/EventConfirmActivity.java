package com.example.calendartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class EventConfirmActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_confirm);
        //ListView listView = (ListView) findViewById(R.id.listView);

        EventConfirm eventconfirm = (EventConfirm) new EventConfirm(this).execute();



/*
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,EventConfirm.list);

        listView.setAdapter(adapter);
*/
    }
    /*
    public void getList(ArrayList<String> result){
        ListView lv = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,result);
        lv.setAdapter(adapter);
    }
*/
}
