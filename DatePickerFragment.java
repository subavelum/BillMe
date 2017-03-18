package com.map.suba.dontgiveup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
//import android.icu.util.Calendar;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatePickerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DatePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatePickerFragment extends DialogFragment  {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month= c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (MainActivity)getActivity(), year,month,day);

    }

   /* public void onDateSet(DatePicker view, int year, int month, int day){
       // TextView tv=(TextView)getActivity().findViewById(R.id.txt_data);
        //tv.setText(tv.getText()+"\nYear: "+year);
        //tv.setText(tv.getText()+"\nMonth: "+month);
       // tv.setText(tv.getText()+"\nDay of Month: "+day);
        month=month+1;// month starts from 0, so addition 1 is required to correct month display

        String stringOfDate= day+"/"+month+"/"+year;

        //tv.setText(tv.getText()+"\n\nDue date: "+stringOfDate);
        //return  stringOfDate;

    }*/




}
