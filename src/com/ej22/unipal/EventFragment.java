package com.ej22.unipal;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ej22.unipal.model.DatabaseSetup;

public class EventFragment extends Fragment{

	private int day, month, year;
	DatabaseSetup db;
	
	//Needed to do insert
	TextView dueDate;
	EditText name, subject, subtype, desc;
	Spinner eventType;
	java.sql.Date date;
	int selection;
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		db = new DatabaseSetup(getActivity());
		db.open();
		
		
		
		Toast.makeText(getActivity(), "" + selection, Toast.LENGTH_SHORT).show();
		
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setHomeButtonEnabled(false);
		
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.fragment_event, container, false);
		
		Bundle extras = getArguments();
		selection = extras.getInt("spinner selection");
		
		name = (EditText)rootView.findViewById(R.id.EnterName);
		subject = (EditText)rootView.findViewById(R.id.EnterSubject);
		subtype = (EditText)rootView.findViewById(R.id.EnterSubType);
		desc = (EditText)rootView.findViewById(R.id.descriptionEditText);
		eventType = (Spinner)rootView.findViewById(R.id.spinnerEventType);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinnerEventTypes, android.R.layout.simple_spinner_item);
		eventType.setAdapter(adapter);
		eventType.setSelection(selection);
		
		final Calendar c = Calendar.getInstance();
		day = c.get(Calendar.DATE);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);
		
		dueDate = (TextView)rootView.findViewById(R.id.dueDateDialog);
		dueDate.setText("" + day + " of " + getStringMonth(month) + " " + year);
		dueDate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				DatePickerDialog dpg = new DatePickerDialog(getActivity(), new OnDateSetListener(){

					@Override
					public void onDateSet(DatePicker picker, int selectedYear, int selectedMonth,
							int selectedDay) {
						
						Toast.makeText(getActivity(), "Date Set at: " + selectedDay + "th of " + getStringMonth(selectedMonth) + " " + selectedYear, Toast.LENGTH_LONG).show();
						dueDate.setText("" + selectedDay + " of " + getStringMonth(selectedMonth) + " " + selectedYear);
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
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			fm.popBackStack();
			ft.commit();
			Toast.makeText(getActivity(), "CANCEL", Toast.LENGTH_SHORT).show();
			return true;
		}
		if (id == R.id.menu_save_btn){
			try{
				String s1 = name.getText().toString();		
				String s2 = subject.getText().toString();
				String s3 = eventType.getSelectedItem().toString();
				String s4 = subtype.getText().toString();
				String s5 = dueDate.getText().toString();
				String s6 = desc.getText().toString();
				
				if(s3.equals("Task")){
					db.insertTask(s1, s2, s3, s4, s5, s6);
				}
				else if (s3.equals("Exam")){
					db.insertExam(s1, s2, s3, s4, s5, s6);
				}
				
			}catch(SQLException e){
				Log.e("InsertFail", "Failed to insert");
			}
			Toast.makeText(getActivity(), "SAVE", Toast.LENGTH_SHORT).show();
			
			//Code referenced from: http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard/15587937#15587937
			((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
		    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
			//reference complete
			
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
