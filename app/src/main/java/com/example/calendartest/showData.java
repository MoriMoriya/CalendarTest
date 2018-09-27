package com.example.calendartest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class showData extends AppCompatActivity {

    private TextView textView;
    private Database helper;
    int allMoney = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        helper = new Database(getApplicationContext());

        readData();
    }

    private void readData(){

        final ArrayList data = new ArrayList();
        final SQLiteDatabase db = helper.getReadableDatabase();
        final Cursor cursor = db.query(
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
            /*
            sbuilder.append(cursor.getString(0));
            sbuilder.append(":  ");
            sbuilder.append(cursor.getInt(1));
            sbuilder.append("円\n\n");
            */
            allMoney += cursor.getInt(1);

            data.add(cursor.getString(0) + ":  "+ cursor.getInt(1) + "円");
            cursor.moveToNext();
        }
        data.add("累計:　　　   " + allMoney + "円");

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
        ListView listView =(ListView)findViewById(R.id.showList);
        listView.setAdapter(adapter);
        //cursor.close();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(showData.this);
                builder.setMessage("削除しますか？").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        
                        /*
                        data.remove(position);
                        db.delete("money", "_id= ?", null);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(showData.this,"削除しました",Toast.LENGTH_LONG);
                        */
                    }
                }).setNegativeButton("キャンセル",null).setCancelable(true);
                builder.show();
                return false;
            }
        });
    }
}
