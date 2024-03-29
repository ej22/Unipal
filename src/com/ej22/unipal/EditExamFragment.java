/*
 * EditExamFragment.java
 * Author: Stephen Hanley
 * Student Number: C08364275
 * Date: 17/04/2014
 * 
 * Purpose: To inflate the fragment which will be used for editing a exam in the local SQLite database
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ej22.unipal.model.DatabaseSetup;

public class EditExamFragment extends Fragment
{

	//global variables
	private int day, month, year;
	DatabaseSetup db;
	TextView dueDate;
	EditText name, subtype, desc;
	Cursor c;
	Spinner eventType, subject;
	static ArrayAdapter<String> subjectAdapter;
	int eventSelection, subjectSelection;
	long rowId;

	//Strings to hold information sent through a bundle
	String moduleSelection, bundleName, bundleSubject, bundleEventType,
			bundleSubType, bundleDueDate, bundleDesc;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//set that this fragment has the options menu available
		setHasOptionsMenu(true);
		//open database
		db = new DatabaseSetup(getActivity());
		db.open();

		//call method to load modules titles from database and assign them to spinner
		loadModuleSpinnerData();

		//disable various action bar options that while editing
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setHomeButtonEnabled(false);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_event, container,
				false);

		//retrieve bundle of arguments and assign them to variables
		Bundle extras = getArguments();
		rowId = extras.getLong("_id");
		bundleName = extras.getString("Name");
		bundleSubject = extras.getString("Subject");
		bundleEventType = extras.getString("EventType");
		bundleSubType = extras.getString("SubType");
		bundleDueDate = extras.getString("Due_Date");
		bundleDesc = extras.getString("Desc");
		//setting this as 1 so as the spinner will show "Exam" by default
		eventSelection = 1;

		name = (EditText) rootView.findViewById(R.id.EnterName);
		subject = (Spinner) rootView.findViewById(R.id.EnterSubject);
		subtype = (EditText) rootView.findViewById(R.id.EnterSubType);
		desc = (EditText) rootView.findViewById(R.id.descriptionEditText);
		eventType = (Spinner) rootView.findViewById(R.id.spinnerEventType);

		//set text from information passed through bundle
		name.setText(bundleName);
		subtype.setText(bundleSubType);
		desc.setText(bundleDesc);

		//setup event type array from string-array  and set adapter to it. 
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.spinnerEventTypes,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventType.setAdapter(adapter);
		//set spinner to the previous selection
		eventType.setSelection(eventSelection);

		//calendar objects to get today's date
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
			//enable actionbar options
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
			//enable actionbar options
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);

			try
			{
				//get the values from various widgets and assign them to strings
				String s1 = name.getText().toString();
				String s2 = subject.getSelectedItem().toString();
				String s3 = eventType.getSelectedItem().toString();
				String s4 = subtype.getText().toString();
				String s5 = dueDate.getText().toString();
				String s6 = desc.getText().toString();

				//update exam module
				db.updateExam(rowId, s1, s2, s3, s4, s5, s6);

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

			//FragmentTransaction to ensure that the updateListViewMethod in ExamFragment is recalled to update
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();

			ft.replace(R.id.frag_container, new ExamFragment());
			ft.commit();

			return true;
		}
		if (id == R.id.discard_btn)
		{
			//enable actionbar options
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);

			//make alert dialog to prompt user warning about delete
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Confirm Delete");
			builder.setMessage(R.string.delete_confim)
					.setPositiveButton(R.string.delete_btn,
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,
										int id) {
									db.deleteExam(rowId);
									Toast.makeText(getActivity(),
											"Delete Successful",
											Toast.LENGTH_SHORT).show();

									FragmentManager fm = getFragmentManager();
									FragmentTransaction ft = fm
											.beginTransaction();
									
									//replace with new ExamFragment so updateListView method is recalled
									ft.replace(R.id.frag_container,
											new ExamFragment());
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
									
									//replace with new ExamFragment so updateListView method is recalled
									ft.replace(R.id.frag_container,
											new ExamFragment());
									ft.commit();
								}
							});
			// Create the AlertDialog object and return it
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
