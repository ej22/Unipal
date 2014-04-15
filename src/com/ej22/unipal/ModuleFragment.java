package com.ej22.unipal;

import java.util.List;

import com.ej22.unipal.model.DatabaseSetup;
import com.ej22.unipal.model.Module;

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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
		
		List<Module> modules = db.getAllModules();
		
		ArrayAdapter<Module> adapter = new ArrayAdapter<Module>(getActivity(),
		        android.R.layout.simple_list_item_1, modules);
		
		modListView.setAdapter(adapter);
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
	
}
