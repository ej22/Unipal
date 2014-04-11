package com.ej22.unipal;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OverViewFragment extends Fragment {
	
	public OverViewFragment(){};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
		return rootView;
	}
}
