package com.example.calendartest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class showData extends AppCompatActivity {

    private TextView textView;
    private Database helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        helper = new Database(getApplicationContext());

        Button readBtn = findViewById(R.id.readButton);
        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    readData();
            }
        });

        textView = findViewById(R.id.textView2);
    }

    private void readData(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                "Money",
                new String[]{"date","money"},
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        StringBuilder sbuilder = new StringBuilder();

        for(int i = 0; i < cursor.getCount();i++){
            sbuilder.append(cursor.getString(0));
            sbuilder.append(":  ");
            sbuilder.append(cursor.getInt(1));
            sbuilder.append("yen\n\n");
            cursor.moveToNext();
        }
        cursor.close();

        textView.setText(sbuilder.toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_db,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //オプションメニューが押された際の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.actionok:
                readData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
