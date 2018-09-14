package com.example.calendartest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by 171y065 on 2018/06/08.
 */

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);

        return datePickerDialog;

    }
    public void onDateSet(DatePicker view, int year, int month, int day){
        //日付が入力された時の処理

        month += 1; //月は+1しないといけない

        String strmonth = String.valueOf(month);

        if(month < 10){
            strmonth = "0" + month;
        }
        String str = String.valueOf(year)  + "-" + strmonth + "-" + String.valueOf(day);

        InsertActivity callingActivity =(InsertActivity) getActivity();
        callingActivity.onDateReturnValue(str);
        dismiss();
    }
}
