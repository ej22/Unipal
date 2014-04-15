package com.ej22.unipal;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.NumberPicker.OnValueChangeListener;

public class AddModuleFragment extends Fragment {
	
	Button btn;
	
	public AddModuleFragment(){};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.module_entry_container, container, false);
		
		
		btn = (Button)rootView.findViewById(R.id.testDBTN);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				show();
			}
			
		});
		return rootView;
		
	}
	


	public void show(){
		final Dialog npDialog = new Dialog(getActivity());
		npDialog.setTitle("Add Modules");
		npDialog.setContentView(R.layout.numberpicker_layout);
		Button setBtn = (Button)npDialog.findViewById(R.id.setBtn);
		Button cnlBtn = (Button)npDialog.findViewById(R.id.CancelButton_NumberPicker);
		
		final NumberPicker numberPicker = (NumberPicker)npDialog.findViewById(R.id.numberPicker);
		numberPicker.setMaxValue(10);
		numberPicker.setMinValue(0);
		numberPicker.setWrapSelectorWheel(false);
		numberPicker.setOnValueChangedListener(new OnValueChangeListener(){

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		setBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Number selected: " + numberPicker.getValue() , Toast.LENGTH_SHORT).show();
				
			}
			
		});
		
		cnlBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				npDialog.dismiss();
			}
			
		});
		
		npDialog.show();
		
	}

}
