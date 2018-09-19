package com.example.calendartest;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

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
        String strHourofday = String.valueOf(hourOfDay);
        String strMinute = String.valueOf(minute);
        if(hourOfDay <10){
            strHourofday = "0" + hourOfDay;
        }
        if(minute < 10){
            strMinute = "0" + minute;
        }
        String str = strHourofday + ":" + strMinute + ":00";
        InsertActivity callingActivity =(InsertActivity) getActivity();
        callingActivity.onTimeReturnValue(str);
        dismiss();
    }
}

