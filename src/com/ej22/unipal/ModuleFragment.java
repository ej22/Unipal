/*
 * ModuleFragment.java
 * Author: Stephen Hanley
 * Student Number: C08364275
 * Date: 17/04/2014
 * 
 * Purpose: To inflate the fragment which will be used for displaying Modules in the database. 
 * It will also provide the user to create a new Module via an actionbar icon and a clicklistener
 * on the list item to enable editting and deletion in the EditModuleFragment.java class
 * 
 */
package com.ej22.unipal;

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

import com.ej22.unipal.adapter.ModuleCustomCursorAdapter;
import com.ej22.unipal.model.DatabaseSetup;

public class ModuleFragment extends Fragment
{
	//initial variables
	DatabaseSetup db;
	ListView modListView;

	public ModuleFragment()
	{
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_module, container,
				false);

		return rootView;

	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add_event_menu, menu);
		return;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		//instantiate database
		db = new DatabaseSetup(getActivity());
		db.open();
		//populate list view
		populateListView();
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.add_event_btn)
		{
			//call AddModuleFragment.java class adn replace in container
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.addToBackStack(null);
			ft.replace(R.id.frag_container, new AddModuleFragment());
			ft.commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void populateListView() {
		//database query to get all modules
		Cursor cursor = db.getAllModules();

		//let activity manage cursor **DEPRECIATED YET STILL WORKING**
		getActivity().startManagingCursor(cursor);

		//string array of fields from database
		String[] fieldNames = new String[] { DatabaseSetup.KEY_SUBJECT,
				DatabaseSetup.KEY_ABBREVIATION, DatabaseSetup.KEY_COLOUR };
		//int array of id's relating to the field names
		int[] fieldNameViewIds = new int[] { R.id.moduleName, R.id.moduleAbbre,
				R.id.moduleColorLine };
		//instantiate custom cursor adapter 
		ModuleCustomCursorAdapter myAdapter = new ModuleCustomCursorAdapter(
				getActivity(), R.layout.module_listview_row_layout, cursor,
				fieldNames, fieldNameViewIds);
		ListView lv = (ListView) getActivity()
				.findViewById(R.id.moduleListView);
		
		//adds padding to top and bottom of list 
		lv.addFooterView(new View(getActivity()), null, false);
		lv.addHeaderView(new View(getActivity()), null, false);

		lv.setAdapter(myAdapter);

		lv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos,
					long id) {
				//create EditModuleFragment 
				EditModuleFragment frag = new EditModuleFragment();
				//get details of editable module
				Cursor c = db.getModuleDetails(id);
				Bundle modInfo = new Bundle();
				//assign values of cursor to bundle
				modInfo.putLong("_id", c.getLong(0));
				modInfo.putString("subject", c.getString(1));
				modInfo.putString("abbrev", c.getString(2));
				modInfo.putInt("color", c.getInt(3));
				//set fragment arguments
				frag.setArguments(modInfo);

				//replace container with editmodulefragment and add this fragment to backstack
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.addToBackStack(null);
				ft.replace(R.id.frag_container, frag);
				ft.commit();

			}

		});
	}

}
