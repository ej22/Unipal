/*
 * OverViewFragment.java
 * Author: Stephen Hanley
 * Student Number: C08364275
 * Date: 17/04/2014
 * 
 * Purpose: To inflate the fragment which will be used for displaying an introduction to the app
 * including information such as current amount of modules, task and events 
 */
package com.ej22.unipal;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ej22.unipal.model.DatabaseSetup;

public class OverViewFragment extends Fragment
{

	public OverViewFragment()
	{
	};

	//initial variables
	TextView mod, exam, task;
	DatabaseSetup db;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_overview, container,
				false);

		mod = (TextView) rootView.findViewById(R.id.getModCount);
		exam = (TextView) rootView.findViewById(R.id.getExamCount);
		task = (TextView) rootView.findViewById(R.id.getTaskCount);

		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//open database
		db = new DatabaseSetup(getActivity());
		db.open();
		
		//query for all modules
		Cursor modC = db.getAllModules();
		//assign number of modules to the textView
		mod.setText("" + modC.getCount());
		
		modC = db.getAllExams();
		exam.setText("" + modC.getCount());
		
		modC = db.getAllTasks();
		task.setText("" + modC.getCount());
	}

}
