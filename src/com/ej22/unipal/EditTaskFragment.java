/*
 * EditModuleFragment.java
 * Author: Stephen Hanley
 * Student Number: C08364275
 * Date: 17/04/2014
 * 
 * Purpose: To inflate the fragment which will be used for editing a task in the local SQLite database
 * 
 */
package com.ej22.unipal;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.Cursor;
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

public class EditTaskFragment extends Fragment
{
	//initial variables
	private int day, month, year;
	DatabaseSetup db;
	TextView dueDate;
	EditText name, subtype, desc;
	Cursor c;
	Spinner eventType, subject;
	static ArrayAdapter<String> subjectAdapter;
	int eventSelection, subjectSelection;
	long rowId;

	String moduleSelection, bundleName, bundleSubject, bundleEventType,
			bundleSubType, bundleDueDate, bundleDesc;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//set optionsmenu
		setHasOptionsMenu(true);
		//instantiate database connection
		db = new DatabaseSetup(getActivity());
		db.open();
		//load data from database and set it to spinner
		loadModuleSpinnerData();
		
		//disable action bar options 
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setHomeButtonEnabled(false);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_event, container,
				false);

		//get bundle arguemnts and assign to variables
		Bundle extras = getArguments();
		rowId = extras.getLong("_id");
		bundleName = extras.getString("Name");
		bundleSubject = extras.getString("Subject");
		bundleEventType = extras.getString("EventType");
		bundleSubType = extras.getString("SubType");
		bundleDueDate = extras.getString("Due_Date");
		bundleDesc = extras.getString("Desc");

		//set event spinner to Task by default
		eventSelection = 0;

		name = (EditText) rootView.findViewById(R.id.EnterName);
		subject = (Spinner) rootView.findViewById(R.id.EnterSubject);
		subtype = (EditText) rootView.findViewById(R.id.EnterSubType);
		desc = (EditText) rootView.findViewById(R.id.descriptionEditText);
		eventType = (Spinner) rootView.findViewById(R.id.spinnerEventType);

		name.setText(bundleName);
		subtype.setText(bundleSubType);
		desc.setText(bundleDesc);

		//load event spinner from string-array and set selection to Task
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.spinnerEventTypes,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventType.setAdapter(adapter);
		eventType.setSelection(eventSelection);
		
		//calendar to get todays date
		final Calendar c = Calendar.getInstance();
		day = c.get(Calendar.DATE);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);

		dueDate = (TextView) rootView.findViewById(R.id.dueDateDialog);
		dueDate.setText(bundleDueDate);
		dueDate.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				//instantiate datepicker 
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
		inflater.inflate(R.menu.update_menu, menu);
		return;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.cancel_btn)
		{
			//enable action bar options
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);
			//pop backstack of fragment
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			fm.popBackStack();
			ft.commit();
			return true;
		}
		if (id == R.id.accept_btn)
		{
			//enable action bar options
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);

			try
			{
				//get vaules from widgets
				String s1 = name.getText().toString();
				String s2 = subject.getSelectedItem().toString();
				String s3 = eventType.getSelectedItem().toString();
				String s4 = subtype.getText().toString();
				String s5 = dueDate.getText().toString();
				String s6 = desc.getText().toString();
				//update database
				db.updateTask(rowId, s1, s2, s3, s4, s5, s6);

			} catch (SQLException e)
			{
				Log.e("InsertFail", "Failed to insert");
			}
			Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();

			// Code referenced from:
			// http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard/15587937#15587937
			((InputMethodManager) getActivity().getSystemService(
					Activity.INPUT_METHOD_SERVICE)).toggleSoftInput(
					InputMethodManager.SHOW_IMPLICIT, 0);
			// reference complete

			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			//call new instance of taskfragment to as to refresh list view
			ft.replace(R.id.frag_container, new TaskFragment());
			ft.commit();

			return true;
		}
		if (id == R.id.discard_btn)
		{
			//enable action bar options
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);

			//call alert dialog builder
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Confirm Delete");
			builder.setMessage(R.string.delete_confim)
					.setPositiveButton(R.string.delete_btn,
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,
										int id) {
									db.deleteTask(rowId);
									Toast.makeText(getActivity(),
											"Delete Successful",
											Toast.LENGTH_SHORT).show();

									FragmentManager fm = getFragmentManager();
									FragmentTransaction ft = fm
											.beginTransaction();
									//display new instance of taskfragment to refresh list view
									ft.replace(R.id.frag_container,
											new TaskFragment());
									ft.commit();

								}
							})
					.setNegativeButton(R.string.cancel_btn,
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();

									FragmentManager fm = getFragmentManager();
									FragmentTransaction ft = fm
											.beginTransaction();
									//display new instance of taskfragment to refresh list view
									ft.replace(R.id.frag_container,
											new TaskFragment());
									ft.commit();
								}
							});
			// Create the AlertDialog object and show it
			AlertDialog dialog = builder.create();
			dialog.show();

		}
		return super.onOptionsItemSelected(item);
	}

	//method to change int value into appropriate string value for month
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

	//method to load data from database into spinner
	private void loadModuleSpinnerData() {
		List<String> titles = db.getMouduleTitles();
		subjectAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, titles);
		subjectAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subject.setAdapter(subjectAdapter);

		//for loop to search that if the subject passed in bundle matches one of the module titles
		//set the spinner to that subject
		for (int i = 0; i < titles.size(); i++)
		{
			if (bundleSubject.matches(titles.get(i)))
			{
				subject.setSelection(i);
				break;
			}
		}
	}
}
