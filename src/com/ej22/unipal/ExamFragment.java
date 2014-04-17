/*
 * ExamFragment.java
 * Author: Stephen Hanley
 * Student Number: C08364275
 * Date: 17/04/2014
 * 
 * Purpose: To inflate the fragment which will be used for displaying Exams in the database. 
 * It will also provide the user to create a new Exam via an actionbar icon and a clicklistener
 * on the list item to enable editting and deletion in the EditExamFragment.java class
 * 
 */
package com.ej22.unipal;

import com.ej22.unipal.adapter.ExamTaskCustomCursorAdapter;
import com.ej22.unipal.model.DatabaseSetup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ExamFragment extends Fragment
{

	//initial variables
	ListView eventListView;
	DatabaseSetup db;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_exam, container,
				false);

		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		//instantiate database
		db = new DatabaseSetup(getActivity());
		db.open();
		//load listview from database
		populateEventListView();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add_event_menu, menu);
		return;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.add_event_btn)
		{
			//create EventFragment
			EventFragment frag = new EventFragment();
			Bundle extra = new Bundle();
			//add spinner selection int value to bundle
			extra.putInt("spinner selection", 1);
			frag.setArguments(extra);

			//replace this frag with you're new EventFragment and add this fragment to
			//the backstack
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.addToBackStack(null);
			ft.replace(R.id.frag_container, frag);
			ft.commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void populateEventListView() {
		Cursor cursor = db.getAllExams();

		//let activity manage the cursor status **DEPRECIATED YET STILL EFFECTIVE**
		getActivity().startManagingCursor(cursor);

		//setup array of database field names
		String[] fieldNames = new String[] { DatabaseSetup.KEY_DUE_DATE,
				DatabaseSetup.KEY_SUBJECT, DatabaseSetup.KEY_NAME };
		//setup id's of widgets you want them to go into 
		int[] fieldNameViewIds = new int[] { R.id.dateView, R.id.SubjectName,
				R.id.TaskName };

		//instantiate custom cursor adapter 
		ExamTaskCustomCursorAdapter myAdapter = new ExamTaskCustomCursorAdapter(
				getActivity(), R.layout.exam_task_listview_row_layout, cursor,
				fieldNames, fieldNameViewIds);
		ListView lv = (ListView) getActivity().findViewById(R.id.examListView);

		//adds padding to top and bottom of list  
		lv.addFooterView(new View(getActivity()), null, false);
		lv.addHeaderView(new View(getActivity()), null, false);

		lv.setAdapter(myAdapter);

		lv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				//create fragment of EditExamFragment.java
				EditExamFragment frag = new EditExamFragment();
				Bundle editInfo = new Bundle();
				//query database for all fields relating to Exam
				Cursor c = db.getExamDetails(id);
				//put info into bundle and set fragment arguments
				editInfo.putLong("_id", c.getLong(0));
				editInfo.putString("Name", c.getString(1));
				editInfo.putString("Subject", c.getString(2));
				editInfo.putString("EventType", c.getString(3));
				editInfo.putString("SubType", c.getString(4));
				editInfo.putString("Due_Date", c.getString(5));
				editInfo.putString("Desc", c.getString(6));
				frag.setArguments(editInfo);

				//replace this frag with new frag and add this frag to backstack
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.addToBackStack(null);
				ft.replace(R.id.frag_container, frag);
				ft.commit();
			}

		});

	}
}
