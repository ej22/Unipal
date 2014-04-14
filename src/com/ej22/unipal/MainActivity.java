package com.ej22.unipal;

import java.util.ArrayList;

import com.ej22.unipal.adapter.DrawerListItemAdapterSetup;
import com.ej22.unipal.model.DatabaseSetup;
import com.ej22.unipal.model.DrawerListItemSetup;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle drawerToggle;
	
	private CharSequence drawerTitle, menuTitle;
	
	private String[] drawerListTitles;
	private TypedArray drawerListIcons;
	private ArrayList<DrawerListItemSetup> drawerListItem;
	private DrawerListItemAdapterSetup adapter;

	DatabaseSetup db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new DatabaseSetup(this);
		
		
		menuTitle = drawerTitle = getTitle();
		
		drawerListTitles = getResources().getStringArray(R.array.drawer_items_list);
		drawerListIcons = getResources().obtainTypedArray(R.array.drawer_items_icons);
		
		drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		drawerListView = (ListView)findViewById(R.id.list_menu);
		
		drawerListItem = new ArrayList<DrawerListItemSetup>();

		for(int i=0;i<drawerListTitles.length;i++){
			drawerListItem.add(new DrawerListItemSetup(drawerListTitles[i], drawerListIcons.getResourceId(i, -1)));
		}
		
		drawerListIcons.recycle();
		
		adapter = new DrawerListItemAdapterSetup(getApplicationContext(), drawerListItem);
		drawerListView.setAdapter(adapter);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer,
		R.string.app_name, 
		R.string.app_name){
			
			public void onDrawerClosed(View view){
				getActionBar().setTitle(menuTitle);
			}
			
			public void onDrawerOpened(View drawerView){
				getActionBar().setTitle(drawerTitle);
			}
		};
		
		if(savedInstanceState == null){
			displayView(0);
		}
		
		drawerLayout.setDrawerListener(drawerToggle);		
		
		drawerListView.setOnItemClickListener(new SlideMenuClickListener());
	}//end onCreate
	
	private class SlideMenuClickListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos,
		long id) {
			// TODO Auto-generated method stub
			displayView(pos);
		}

	}
	
	private void displayView(int pos) {
		Fragment frag = null;
		switch(pos){
		case 0:
			frag = new OverViewFragment();
			break;
		case 1:
			frag = new ModuleFragment();
			break;
		case 2:
			frag = new GradeFragment();
			break;
		case 3:
			frag = new TaskFragment();
			break;
		case 4:
			frag = new ExamFragment();
			break;
		default:
			break;
		}
		
		if (frag!= null){
			FragmentManager fragManager = getFragmentManager();
			fragManager.beginTransaction().replace(R.id.frag_container, frag).commit();
			drawerListView.setItemChecked(pos, true);
			drawerListView.setSelected(true);
			setTitle(drawerListTitles[pos]);
			drawerLayout.closeDrawer(drawerListView);
		}else{
			Log.e("MainActivity", "Problem Switching Fragments");
		}
	}
	
	public void setTitle(CharSequence title){
		menuTitle=title;
		getActionBar().setTitle(menuTitle);
	}
	
	protected void onPostCreate(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
	
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
