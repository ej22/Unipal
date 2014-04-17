/*
 * MainActivity.java
 * Author: Stephen Hanley
 * Student Number: C08364275
 * Date: 17/04/2014
 * 
 * Purpose: To inflate the fragment which will be used for handling the drawing of the navigation
 * drawer and loading the default fragment when you load the application
 * 
 */
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity
{
	//initial variables
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
		//open database
		db = new DatabaseSetup(this);
		db.open();
		//set the menu and drawer titles to be the same 
		menuTitle = drawerTitle = getTitle();

		//set array to hold the nav menu names
		drawerListTitles = getResources().getStringArray(
				R.array.drawer_items_list);
		//set array to hold the nav menu icons
		drawerListIcons = getResources().obtainTypedArray(
				R.array.drawer_items_icons);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerListView = (ListView) findViewById(R.id.list_menu);

		drawerListItem = new ArrayList<DrawerListItemSetup>();

		for (int i = 0; i < drawerListTitles.length; i++)
		{
			//add names and icons to drawerlist
			drawerListItem.add(new DrawerListItemSetup(drawerListTitles[i],
					drawerListIcons.getResourceId(i, -1)));
		}

		adapter = new DrawerListItemAdapterSetup(getApplicationContext(),
				drawerListItem);
		drawerListView.setAdapter(adapter);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		//define what happens when you open and close the drawer
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name)
		{

			public void onDrawerClosed(View view) {
				getActionBar().setTitle(menuTitle);
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(drawerTitle);
			}
		};

		if (savedInstanceState == null)
		{
			//set the initial display to be the first case in switch statement
			displayView(0);
		}

		drawerLayout.setDrawerListener(drawerToggle);

		drawerListView.setOnItemClickListener(new SlideMenuClickListener());
	}// end onCreate

	private class SlideMenuClickListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			//call display method for the item at that position
			displayView(pos);
		}

	}

	//method to help display the various fragments based on what is clicked in the nav drawer
	private void displayView(int pos) {
		Fragment frag = null;
		switch (pos)
		{
		case 0:
			frag = new OverViewFragment();
			break;
		case 1:
			frag = new ModuleFragment();
			break;
		case 2:
			frag = new TaskFragment();
			break;
		case 3:
			frag = new ExamFragment();
			break;
		default:
			break;
		}

		if (frag != null)
		{
			//replace fragment container with a fragment
			FragmentManager fragManager = getFragmentManager();
			fragManager.beginTransaction().replace(R.id.frag_container, frag)
					.commit();
			
			// update selected item and title, then close the drawer
			drawerListView.setItemChecked(pos, true);
			drawerListView.setSelected(true);
			setTitle(drawerListTitles[pos]);
			drawerLayout.closeDrawer(drawerListView);
		} else
		{
			Log.e("MainActivity", "Problem Switching Fragments");
		}
	}

	public void setTitle(CharSequence title) {
		menuTitle = title;
		getActionBar().setTitle(menuTitle);
	}

	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		 // Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		drawerToggle.onConfigurationChanged(newConfig);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		switch (item.getItemId())
		{
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
