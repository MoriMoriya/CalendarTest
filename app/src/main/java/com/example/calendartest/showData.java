package com.example.calendartest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

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
        Cursor cursor = db.query(
                "Money",
                new String[]{"_id","date","money"},
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        StringBuilder sbuilder = new StringBuilder();
        final ArrayList<Integer> arrayList = new ArrayList<>();

        for(int i = 0; i < cursor.getCount();i++){
            /*
            sbuilder.append(cursor.getString(0));
            sbuilder.append(":  ");
            sbuilder.append(cursor.getInt(1));
            sbuilder.append("円\n\n");
            */
            arrayList.add(cursor.getInt(0));
            allMoney += cursor.getInt(2);
            data.add(arrayList.get(i) + cursor.getString(1) + ":  "+ cursor.getInt(2) + "円");
            cursor.moveToNext();
        }
        data.add("累計:　　　   " + allMoney + "円");


        Calendar cl = Calendar.getInstance();

        int clMonth = cl.get(Calendar.MONTH)+1;
        int dateclMonth = cl.get(Calendar.MONTH)+2;

        int clDay = cl.get(Calendar.DAY_OF_MONTH);

        String clmonth = String.valueOf(clMonth);
        String clday = String.valueOf(clDay);
        String dateclmonth = String.valueOf(dateclMonth);

        if(dateclMonth <10){
            dateclmonth = "0" + dateclmonth;
        }
        if(clMonth < 10){
            clmonth = "0" + clmonth;
        }
        if(clDay < 10){
            clday = "0" + clday;
        }

        String cal = cl.get(Calendar.YEAR)  + "-" + clmonth + "-" + clday;
        String endcal = cl.get(Calendar.YEAR) + "-" + dateclmonth + "-" + clday;
        Log.d("",cal + "\n" + endcal ,null);
        Cursor cursor1 = db.rawQuery("SELECT * FROM Money WHERE STRFTIME('%Y-%m'," + cal + ") < STRFTIME('%Y-%m'," + endcal + ")" ,null);
        cursor1.moveToFirst();
        int monthMoney =0;
        for(int i =0; i< cursor1.getCount(); i++){

            monthMoney += cursor1.getInt(2);
            Log.d("DB_RawQuery", String.valueOf(cursor1.getInt(2)),null);
            cursor1.moveToNext();
        }
        data.add("月:　　　　   " + monthMoney + "円");

        //where strftime('YYYY-mm'," + endcal + ") > " + "strftime('YYYY-mm',"+ cal +")


        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
        ListView listView =(ListView)findViewById(R.id.showList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(showData.this);
                builder.setMessage("削除しますか？").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        data.remove(position);
                        db.delete("money", "_id=" + arrayList.get(position), null);
                        arrayList.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(showData.this,"削除しました",Toast.LENGTH_LONG);
                    }
                }).setNegativeButton("キャンセル",null).setCancelable(true);
                builder.show();
                return false;
            }
        });
        cursor.close();
    }
}
