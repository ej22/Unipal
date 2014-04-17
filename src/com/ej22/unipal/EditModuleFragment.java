/*
 * EditModuleFragment.java
 * Author: Stephen Hanley
 * Student Number: C08364275
 * Date: 17/04/2014
 * 
 * Purpose: To inflate the fragment which will be used for editing a module in the local SQLite database
 * LIBRARY REFERENCE: https://android.googlesource.com/platform/frameworks/opt/colorpicker/
 */
package com.ej22.unipal;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.ej22.unipal.model.DatabaseSetup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditModuleFragment extends Fragment{
	
	//initial variables
	DatabaseSetup db;
	EditText module, abbrev;
	ImageView square;
	String subject, bundleAbbrev;
	int colorResult, bundleColor;
	LinearLayout container;
	Long rowId;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.module_entry_container, container, false);
		
		//retrieve bundle and assign to variables
		Bundle info = getArguments();
		rowId = info.getLong("_id");
		subject = info.getString("subject");
		bundleAbbrev = info.getString("abbrev");
		bundleColor = info.getInt("color");
		
		module = (EditText)rootView.findViewById(R.id.module_);
		abbrev = (EditText)rootView.findViewById(R.id.abbreviation_);
		square = (ImageView)rootView.findViewById(R.id.squareImg);
		
		//set color of square to bundle color
		square.setColorFilter(bundleColor);
		
		module.setText(subject);
		abbrev.setText(bundleAbbrev);
		
		//implement ColorPicker Library
		final ColorPickerDialog cpg = ColorPickerDialog.newInstance(
	              R.string.color_picker_default_title, getColors(), 0, 3,
	              ColorPickerDialog.SIZE_SMALL);
		
		
		cpg.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
			
			@Override
			public void onColorSelected(int color) {
				// set color of square and assign to int for editting
				square.setColorFilter(color);
				colorResult = color;
			}
		});
		
		square.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// show dialog
				cpg.show(getFragmentManager(), "tag");
			}
			
		});
		return rootView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		
		//implement database
		db = new DatabaseSetup(getActivity());
		db.open();
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.update_menu, menu);
		return;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		int id = item.getItemId();
		if (id == R.id.cancel_btn) {
			//enable actionbar options
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);
			//pop fragment backstack
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			fm.popBackStack();
			ft.commit();
			return true;
		}
		if (id == R.id.accept_btn){
			//get text from widgets
			String mod = module.getText().toString();
			String abr = abbrev.getText().toString();
			
			String color = "" + colorResult;
			//call update method
			db.updateModule(rowId, mod, abr, color);
			Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
			
			//Code referenced from: http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard/15587937#15587937
			((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
		    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
			//reference complete
			
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			//call  new instance of module fragment to recall refreshlistview method
			ft.replace(R.id.frag_container, new ModuleFragment());
			ft.commit();
			return true;
		}
		if(id == R.id.discard_btn){
			
			//display alertdialog 
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Confirm Delete");
	        builder.setMessage(R.string.delete_confim)
	               .setPositiveButton(R.string.delete_btn, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       db.deleteModule(rowId);
	                       Toast.makeText(getActivity(), "Delete Successful", Toast.LENGTH_SHORT).show();
	                       
	                       FragmentManager fm = getFragmentManager();
	           				FragmentTransaction ft = fm.beginTransaction();
	           				//display new instance of fragment to refresh list view
	           				ft.replace(R.id.frag_container, new ModuleFragment());
	           				ft.commit();
	                       
	                   }
	               })
	               .setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       dialog.dismiss();
	                       
	                       FragmentManager fm = getFragmentManager();
	           			FragmentTransaction ft = fm.beginTransaction();
	           		//display new instance of fragment to refresh list view
	           			ft.replace(R.id.frag_container, new ModuleFragment());
	           			ft.commit();
	                   }
	               });
	        // Create the AlertDialog object and show it
	        AlertDialog dialog = builder.create();
	        dialog.show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public int[] getColors(){
		//assign string-array to a string array
		String[]tempColors = getResources().getStringArray(R.array.default_color_choice_values);
		int[] tempIntColors;
		tempIntColors = new int[tempColors.length];
		//parse colors of string array into an integer array for using in color picker 
		for (int i=0; i<tempColors.length;i++){
			tempIntColors[i] = Color.parseColor(tempColors[i]);
		}
		return tempIntColors;
		
	}
}
