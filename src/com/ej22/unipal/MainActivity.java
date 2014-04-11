package com.ej22.unipal;

import java.util.ArrayList;

import com.ej22.unipal.adapter.DrawerListItemAdapterSetup;
import com.ej22.unipal.model.DatabaseSetup;
import com.ej22.unipal.model.DrawerListItemSetup;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
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

	DatabaseSetup db = new DatabaseSetup(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//db.open();
		
		menuTitle = drawerTitle = getTitle();
		
		drawerListTitles = getResources().getStringArray(R.array.drawer_items_list);
		drawerListIcons = getResources().obtainTypedArray(R.array.drawer_items_icons);
		
		drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		drawerListView = (ListView)findViewById(R.id.list_menu);
		
		drawerListItem = new ArrayList<DrawerListItemSetup>();
						
		drawerListItem.add(new DrawerListItemSetup(drawerListTitles[0], drawerListIcons.getResourceId(0, -1)));
		drawerListItem.add(new DrawerListItemSetup(drawerListTitles[1], drawerListIcons.getResourceId(1, -1)));
		drawerListItem.add(new DrawerListItemSetup(drawerListTitles[2], drawerListIcons.getResourceId(2, -1)));
		drawerListItem.add(new DrawerListItemSetup(drawerListTitles[3], drawerListIcons.getResourceId(3, -1)));
		drawerListItem.add(new DrawerListItemSetup(drawerListTitles[4], drawerListIcons.getResourceId(4, -1)));
		
		drawerListIcons.recycle();
		
		adapter = new DrawerListItemAdapterSetup(getApplicationContext(), drawerListItem);
		drawerListView.setAdapter(adapter);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.book_icon,
				R.string.app_name, 
				R.string.app_name){
			
			public void onDrawerClosed(View view){
				getActionBar().setTitle(menuTitle);
			}
			
			public void onDrawerOpened(View drawerView){
				getActionBar().setTitle(drawerTitle);
			}
		};
		
		drawerLayout.setDrawerListener(drawerToggle);		
		
	}//end onCreate
	
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

}
