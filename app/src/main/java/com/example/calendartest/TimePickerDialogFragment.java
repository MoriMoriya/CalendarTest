package com.example.calendartest;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;



public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
        return timePickerDialog;
    }
    public void onTimeSet(TimePicker view,int hourOfDay, int minute){
        //時刻が選択された時の処理

        String str = String.valueOf(hourOfDay) + String.valueOf(minute);
        InsertActivity callingActivity =(InsertActivity) getActivity();
        callingActivity.onTimeReturnValue(str);
        dismiss();
    }
}

