package com.ej22.unipal;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class OverViewFragment extends Fragment {
	
	public OverViewFragment(){};
	
	Button btn;
	
	private int day, month, year;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		final Calendar c = Calendar.getInstance();
		day = c.get(Calendar.DATE);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);
		
		View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
		btn = (Button)rootView.findViewById(R.id.btn);
		
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				DatePickerDialog dpg = new DatePickerDialog(getActivity(), new OnDateSetListener(){

					@Override
					public void onDateSet(DatePicker picker, int selectedYear, int selectedMonth,
							int selectedDay) {
						
						Toast.makeText(getActivity(), "Date Set at: " + selectedDay + "th of " + selectedMonth + " " + selectedYear, Toast.LENGTH_LONG).show();
						
					}
					
				}, year,month,day);
				dpg.show();
				
			}
			
		});
		return rootView;
	}

}
