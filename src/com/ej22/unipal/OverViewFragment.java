package com.ej22.unipal;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ej22.unipal.model.DatabaseSetup;

public class OverViewFragment extends Fragment {
	
	public OverViewFragment(){};
	
	TextView mod, exam, task;
	DatabaseSetup db;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
				
		View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
		
		mod = (TextView)rootView.findViewById(R.id.getModCount);
		exam = (TextView)rootView.findViewById(R.id.getExamCount);
		task = (TextView)rootView.findViewById(R.id.getTaskCount);
		
		return rootView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		db = new DatabaseSetup(getActivity());
		db.open();
		Cursor modC = db.getAllModules();
		mod.setText("" + modC.getCount());
		modC = db.getAllExams();
		exam.setText("" + modC.getCount());
		modC = db.getAllTasks();
		task.setText("" + modC.getCount());
	}
	
	
}
