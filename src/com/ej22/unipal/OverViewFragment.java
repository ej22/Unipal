package com.ej22.unipal;

import java.util.Calendar;

import com.android.colorpicker.*;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.*;

public class OverViewFragment extends Fragment {
	
	public OverViewFragment(){};
	
	Button btn;
	ImageView img;
	
	private int day, month, year;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		final Calendar c = Calendar.getInstance();
		day = c.get(Calendar.DATE);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);
		
		View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
		img = (ImageView)rootView.findViewById(R.id.imageIC);
		btn = (Button)rootView.findViewById(R.id.btn);
		
		//Start ColorPickerDialog
		final ColorPickerDialog cpg = ColorPickerDialog.newInstance(
	              R.string.color_picker_default_title, getColors(), 0, 3,
	              ColorPickerDialog.SIZE_SMALL);
		
		cpg.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
			
			@Override
			public void onColorSelected(int color) {
				// TODO Auto-generated method stub
				img.setColorFilter(color);
				Toast.makeText(getActivity(), "COLOR: " + String.format("#%06X", (0xFFFFFF & color)), Toast.LENGTH_SHORT).show();
			}
		});
		
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				cpg.show(getFragmentManager(), "tag");
			}//end onClick
			
		});
		return rootView;
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
