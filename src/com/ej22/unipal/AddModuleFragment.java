package com.ej22.unipal;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.ej22.unipal.model.DatabaseSetup;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.NumberPicker.OnValueChangeListener;

public class AddModuleFragment extends Fragment {
	
	public AddModuleFragment(){};
	
	DatabaseSetup db;
	EditText module, abbrev;
	ImageView square;
	String squareHEX;
	LinearLayout container;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.module_entry_container, container, false);		
		
		module = (EditText)rootView.findViewById(R.id.module_);
		abbrev = (EditText)rootView.findViewById(R.id.abbreviation_);
		square = (ImageView)rootView.findViewById(R.id.squareImg);
		
		final ColorPickerDialog cpg = ColorPickerDialog.newInstance(
	              R.string.color_picker_default_title, getColors(), 0, 3,
	              ColorPickerDialog.SIZE_SMALL);
		
		cpg.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
			
			@Override
			public void onColorSelected(int color) {
				// TODO Auto-generated method stub
				square.setColorFilter(color);
				squareHEX = String.format("#%06X", (0xFFFFFF & color));
				Toast.makeText(getActivity(), "COLOR: " + squareHEX, Toast.LENGTH_SHORT).show();
			}
		});
		
		square.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cpg.show(getFragmentManager(), "tag");
			}
			
		});
		return rootView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		
		db = new DatabaseSetup(getActivity());
		db.open();
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.event_menu, menu);
		return;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		int id = item.getItemId();
		if (id == R.id.menu_cancel_btn) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			fm.popBackStack();
			ft.commit();
			Toast.makeText(getActivity(), "CANCEL", Toast.LENGTH_SHORT).show();
			return true;
		}
		if (id == R.id.menu_save_btn){
			String mod = module.getText().toString();
			String abr = abbrev.getText().toString();
			String color = squareHEX;
			db.insertModule(mod, abr, color);
//			db.close();
			Toast.makeText(getActivity(), "SAVE", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public int[] getColors(){
		String[]tempColors = getResources().getStringArray(R.array.default_color_choice_values);
		int[] tempIntColors;
		tempIntColors = new int[tempColors.length];
		for (int i=0; i<tempColors.length;i++){
			tempIntColors[i] = Color.parseColor(tempColors[i]);
		}
		return tempIntColors;
		
	}

}
