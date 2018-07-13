package com.example.calendartest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    private final int FORM_REQUESTCODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent insertintent = new Intent(MainActivity.this, InsertActivity.class);
                startActivity(insertintent);
            }
        });
        if(FirebaseInstanceId.getInstance() == null){
            Intent Signintent = new Intent(MainActivity.this,GoogleSignInActivity.class);
            startActivity(Signintent);
        }
        Button signBtn = (Button) findViewById(R.id.SignBtn);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Signintent = new Intent(MainActivity.this, GoogleSignInActivity.class);
                startActivity(Signintent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingintent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingintent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onDayClick(Date dateClicked){
        DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }
}
