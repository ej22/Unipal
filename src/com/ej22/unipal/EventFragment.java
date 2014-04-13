package com.ej22.unipal;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class EventFragment extends Fragment{

	private int day, month, year;
	TextView spinnerTxt;
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		final Calendar c = Calendar.getInstance();
		day = c.get(Calendar.DATE);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);
		
		View rootView = inflater.inflate(R.layout.fragment_event, container, false);
		
		spinnerTxt = (TextView)rootView.findViewById(R.id.dueDateDialog);
		spinnerTxt.setText("" + day + " of " + getStringMonth(month) + " " + year);
		spinnerTxt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				DatePickerDialog dpg = new DatePickerDialog(getActivity(), new OnDateSetListener(){

					@Override
					public void onDateSet(DatePicker picker, int selectedYear, int selectedMonth,
							int selectedDay) {
						
						Toast.makeText(getActivity(), "Date Set at: " + selectedDay + "th of " + getStringMonth(selectedMonth) + " " + selectedYear, Toast.LENGTH_LONG).show();
						spinnerTxt.setText("" + selectedDay + " of " + getStringMonth(selectedMonth) + " " + selectedYear);
					}
					
				}, year,month,day);
				dpg.show();
				
			}
			
		});
		return rootView;
		
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.event_menu, menu);
		return;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.menu_cancel_btn) {
			Toast.makeText(getActivity(), "CANCEL", Toast.LENGTH_SHORT).show();
			return true;
		}
		if (id == R.id.menu_save_btn){
			Toast.makeText(getActivity(), "CANCEL", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private String getStringMonth(int month){
		month = month + 1 ;
		switch(month){
		case 1:	return "January";
		case 2:	return "February";
		case 3:	return "March";
		case 4: return "April";
		case 5:	return "May";
		case 6: return "June";
		case 7:	return "July";
		case 8:	return "August";
		case 9:	return "September";
		case 10:return "October";
		case 11:return "November";
		case 12:return "December";
		case 0:	Log.e("getStringMonth","0 case initiated");
		}
		return null;
	}
}
