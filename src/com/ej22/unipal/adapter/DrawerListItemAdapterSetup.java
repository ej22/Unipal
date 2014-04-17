package com.ej22.unipal.adapter;

import java.util.ArrayList;

import com.ej22.unipal.R;
import com.ej22.unipal.model.DrawerListItemSetup;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerListItemAdapterSetup extends BaseAdapter
{

	private Context context;
	private ArrayList<DrawerListItemSetup> drawerListItem;

	public DrawerListItemAdapterSetup(Context context,
			ArrayList<DrawerListItemSetup> drawerListItem)
	{
		this.context = context;
		this.drawerListItem = drawerListItem;
	}

	@Override
	public Object getItem(int pos) {
		return drawerListItem.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_custom_layout,
					null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView title = (TextView) convertView.findViewById(R.id.title);

		imgIcon.setImageResource(drawerListItem.get(pos).getIcon());
		title.setText(drawerListItem.get(pos).getTitle());

		return convertView;
	}

	@Override
	public int getCount() {
		return drawerListItem.size();
	}

}
