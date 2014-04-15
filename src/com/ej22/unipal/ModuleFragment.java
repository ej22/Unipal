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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.ej22.unipal.adapter.ModuleCustomCursorAdapter;
import com.ej22.unipal.model.DatabaseSetup;

public class ModuleFragment extends Fragment{

	DatabaseSetup db;
	ListView modListView;
	public ModuleFragment(){};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.fragment_module, container, false);
		modListView = (ListView)rootView.findViewById(R.id.moduleListView);
		
		return rootView;
		
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.add_event_menu, menu);
		return;
	}
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		
		db = new DatabaseSetup(getActivity());
		db.open();
		
		populateListView();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.add_event_btn) {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.addToBackStack(null);
			ft.replace(R.id.frag_container, new AddModuleFragment());
			ft.commit();
			Toast.makeText(getActivity(), "Switching", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void populateListView() {
		Cursor cursor = db.getAllModules();
		
		String[] fieldNames = new String[]{DatabaseSetup.KEY_SUBJECT, DatabaseSetup.KEY_ABBREVIATION, DatabaseSetup.KEY_COLOUR};
		int[] fieldNameViewIds = new int[]{R.id.moduleName, R.id.moduleAbbre, R.id.moduleColorLine};
		
		ModuleCustomCursorAdapter myAdapter = new ModuleCustomCursorAdapter(getActivity(), R.layout.module_listview_row_layout, cursor, fieldNames, fieldNameViewIds);		
		ListView lv = (ListView)getActivity().findViewById(R.id.moduleListView);
		
		lv.addFooterView(new View(getActivity()), null, false);
		lv.addHeaderView(new View(getActivity()), null, false);
		
		lv.setAdapter(myAdapter);
	}

}
