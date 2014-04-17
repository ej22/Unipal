/*
 * EventFragment.java
 * Author: Stephen Hanley
 * Student Number: C08364275
 * Date: 17/04/2014
 * 
 * Purpose: To inflate the fragment which will be used for inserting a event into the database
 * it will either insert into the task or exam table depending on which event type is chosen
 * by the user
 * 
 */
package com.ej22.unipal;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ej22.unipal.model.DatabaseSetup;

public class EventFragment extends Fragment
{
	//initial variables
	private int day, month, year;
	DatabaseSetup db;
	TextView dueDate;
	EditText name, subtype, desc;
	Spinner eventType, subject;
	java.sql.Date date;
	int selection;
	String moduleSelection;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		//instantiate database
		db = new DatabaseSetup(getActivity());
		db.open();
		//load modules from database and assign to spinner
		loadModuleSpinnerData();
		//diable action bar options
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setHomeButtonEnabled(false);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_event, container,
				false);

		//get bundle information and assign to variable
		Bundle extras = getArguments();
		selection = extras.getInt("spinner selection");

		name = (EditText) rootView.findViewById(R.id.EnterName);
		subject = (Spinner) rootView.findViewById(R.id.EnterSubject);
		subtype = (EditText) rootView.findViewById(R.id.EnterSubType);
		desc = (EditText) rootView.findViewById(R.id.descriptionEditText);
		eventType = (Spinner) rootView.findViewById(R.id.spinnerEventType);

		//setup event spinner from string-array
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.spinnerEventTypes,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventType.setAdapter(adapter);
		eventType.setSelection(selection);
		
		//get current date from calendar objects
		final Calendar c = Calendar.getInstance();
		day = c.get(Calendar.DATE);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);

		dueDate = (TextView) rootView.findViewById(R.id.dueDateDialog);
		dueDate.setText("" + day + " of " + getStringMonth(month) + " " + year);
		dueDate.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				//instantiate datepickerdialog
				DatePickerDialog dpg = new DatePickerDialog(getActivity(),
						new OnDateSetListener()
						{

							@Override
							public void onDateSet(DatePicker picker,
									int selectedYear, int selectedMonth,
									int selectedDay) {

								dueDate.setText("" + selectedDay + " of "
										+ getStringMonth(selectedMonth) + " "
										+ selectedYear);
							}

						}, year, month, day);
				dpg.show();

			}

		});
		return rootView;

	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.event_menu, menu);
		return;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.menu_cancel_btn)
		{
			//enable action bar options
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);
			//pop fragment backstack
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			fm.popBackStack();
			ft.commit();
			return true;
		}
		if (id == R.id.menu_save_btn)
		{
			//enable action bar options
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);
			try
			{
				//get data from widgets
				String s1 = name.getText().toString();
				String s2 = subject.getSelectedItem().toString();
				String s3 = eventType.getSelectedItem().toString();
				String s4 = subtype.getText().toString();
				String s5 = dueDate.getText().toString();
				String s6 = desc.getText().toString();

				//if event type is task
				if (s3.equals("Task"))
				{
					//insert a new task
					db.insertTask(s1, s2, s3, s4, s5, s6);
				}//if event type is exam 
				else if (s3.equals("Exam"))
				{
					//insert a new exam
					db.insertExam(s1, s2, s3, s4, s5, s6);
				}

			} catch (SQLException e)
			{
				Log.e("InsertFail", "Failed to insert");
			}
			Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();

			// Code referenced from:
			// http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard/15587937#15587937
			((InputMethodManager) getActivity().getSystemService(
					Activity.INPUT_METHOD_SERVICE)).toggleSoftInput(
					InputMethodManager.SHOW_IMPLICIT, 0);
			// reference complete

			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			//if event type is task
			if (eventType.getSelectedItem().toString().equals("Task"))
			{
				//create new instance of TaskFragment to bring you back to the correct screen
				//and refresh the list view
				ft.replace(R.id.frag_container, new TaskFragment());
			} else if (eventType.getSelectedItem().toString().equals("Exam"))
			{
				//create new instance of ExamFragment to bring you back to the correct screen
				//and refresh the list view
				ft.replace(R.id.frag_container, new ExamFragment());
			}

			ft.commit();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//method to turn month int into appropriate string
	private String getStringMonth(int month) {
		month = month + 1;
		switch (month)
		{
		case 1:
			return "January";
		case 2:
			return "February";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";
		case 0:
			Log.e("getStringMonth", "0 case initiated");
		}
		return null;
	}

	private void loadModuleSpinnerData() {
		//load database query into string list
		List<String> titles = db.getMouduleTitles();
		//setup adapter and assign to spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, titles);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subject.setAdapter(adapter);
	}
}
