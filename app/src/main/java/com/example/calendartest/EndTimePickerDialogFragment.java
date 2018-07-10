package com.example.calendartest;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;


public class EndTimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog endtimePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
        return endtimePickerDialog;
    }
    public void onTimeSet(TimePicker view,int hourOfDay, int minute){
        //時刻が選択された時の処理
        String str = String.valueOf(hourOfDay) + String.valueOf(minute);
        InsertActivity callingActivity =(InsertActivity) getActivity();
                callingActivity.onEndTimeReturnValue(str);
                dismiss();
    }
}

