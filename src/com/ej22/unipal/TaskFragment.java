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

public class TaskFragment extends Fragment{

	ListView eventListView;
	DatabaseSetup db;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_task, container, false);
		return rootView;
	}
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		
		db = new DatabaseSetup(getActivity());
		db.open();
		
		populateTaskListView();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.add_event_menu, menu);
		return;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.add_event_btn){
			
			EventFragment frag = new EventFragment();
			Bundle extra = new Bundle();
			extra.putInt("spinner selection", 0);
			frag.setArguments(extra);
			
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
		Cursor cursorTask = db.getAllTasks();
		
		getActivity().startManagingCursor(cursorTask);
		
		//Fix This Tomorrow
		String[] fieldNames = new String[]{DatabaseSetup.KEY_DUE_DATE, DatabaseSetup.KEY_SUBJECT, DatabaseSetup.KEY_NAME};
		int[] fieldNameViewIds = new int[]{R.id.dateView, R.id.SubjectName, R.id.TaskName};
		
		ExamTaskCustomCursorAdapter myAdapter = new ExamTaskCustomCursorAdapter(getActivity(), R.layout.exam_task_listview_row_layout, cursorTask, fieldNames, fieldNameViewIds);		
		ListView lv = (ListView)getActivity().findViewById(R.id.taskListView);
		
		lv.addFooterView(new View(getActivity()), null, false);
		lv.addHeaderView(new View(getActivity()), null, false);
		
		lv.setAdapter(myAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				EditTaskFragment frag = new EditTaskFragment();
				Bundle editInfo = new Bundle();
				Cursor c = db.getTaskDetails(id);
				editInfo.putLong("_id", c.getLong(0));
				editInfo.putString("Name", c.getString(1));
				editInfo.putString("Subject", c.getString(2));
				editInfo.putString("EventType", c.getString(3));
				editInfo.putString("SubType", c.getString(4));
				editInfo.putString("Due_Date", c.getString(5));
				editInfo.putString("Desc", c.getString(6));
				frag.setArguments(editInfo);
				
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.addToBackStack(null);
				ft.replace(R.id.frag_container, frag);
				ft.commit();
			}
			
		});
	}
}
