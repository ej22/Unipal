/*
 * TaskFragment.java
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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TaskFragment extends Fragment
{
	//initial variables
	ListView eventListView;
	DatabaseSetup db;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_task, container,
				false);
		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		//initialize database
		db = new DatabaseSetup(getActivity());
		db.open();
		//populate listview
		populateTaskListView();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add_event_menu, menu);
		return;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.add_event_btn)
		{
			//create eventfragment and create bundle to hold spinner selection
			EventFragment frag = new EventFragment();
			Bundle extra = new Bundle();
			extra.putInt("spinner selection", 0);
			frag.setArguments(extra);

			//replace container with eventfragment and add taskfragment to backstack
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.addToBackStack(null);
			ft.replace(R.id.frag_container, frag);
			ft.commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void populateTaskListView() {
		//database query to get all tasks in database
		Cursor cursorTask = db.getAllTasks();

		getActivity().startManagingCursor(cursorTask);

		//string array of field names fromd database
		String[] fieldNames = new String[] { DatabaseSetup.KEY_DUE_DATE,
				DatabaseSetup.KEY_SUBJECT, DatabaseSetup.KEY_NAME };
		//int array of id's of widgets relating to database fields
		int[] fieldNameViewIds = new int[] { R.id.dateView, R.id.SubjectName,
				R.id.TaskName };

		//initialize customcursoradapter
		ExamTaskCustomCursorAdapter myAdapter = new ExamTaskCustomCursorAdapter(
				getActivity(), R.layout.exam_task_listview_row_layout,
				cursorTask, fieldNames, fieldNameViewIds);
		ListView lv = (ListView) getActivity().findViewById(R.id.taskListView);

		//add padding to top and bottom of list
		lv.addFooterView(new View(getActivity()), null, false);
		lv.addHeaderView(new View(getActivity()), null, false);

		lv.setAdapter(myAdapter);

		lv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos,
					long id) {
				// create EdittaskFragment
				EditTaskFragment frag = new EditTaskFragment();
				Bundle editInfo = new Bundle();
				//query database for details of selected task
				Cursor c = db.getTaskDetails(id);
				//put info into bundle
				editInfo.putLong("_id", c.getLong(0));
				editInfo.putString("Name", c.getString(1));
				editInfo.putString("Subject", c.getString(2));
				editInfo.putString("EventType", c.getString(3));
				editInfo.putString("SubType", c.getString(4));
				editInfo.putString("Due_Date", c.getString(5));
				editInfo.putString("Desc", c.getString(6));
				//setargument of bundle to the frag
				frag.setArguments(editInfo);

				//replace container with new frag and add this frag to backstack
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.addToBackStack(null);
				ft.replace(R.id.frag_container, frag);
				ft.commit();
			}

		});
	}
}
