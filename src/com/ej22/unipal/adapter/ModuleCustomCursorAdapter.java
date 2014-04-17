package com.ej22.unipal.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ej22.unipal.R;
import com.ej22.unipal.model.DatabaseSetup;

public class ModuleCustomCursorAdapter extends SimpleCursorAdapter
{

	private Context context;
	private int layout;
	private Cursor cursor;
	final private LayoutInflater inflate;

	public ModuleCustomCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to)
	{
		super(context, layout, c, from, to);
		this.context = context;
		this.layout = layout;
		cursor = c;
		this.inflate = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return inflate.inflate(layout, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		TextView modName = (TextView) view.findViewById(R.id.moduleName);
		TextView modAbbrev = (TextView) view.findViewById(R.id.moduleAbbre);
		ImageView modImg = (ImageView) view.findViewById(R.id.lineImg);

		int subject = cursor.getColumnIndex(DatabaseSetup.KEY_SUBJECT);
		int abbrev = cursor.getColumnIndex(DatabaseSetup.KEY_ABBREVIATION);
		int imgColor = cursor.getColumnIndex(DatabaseSetup.KEY_COLOUR);

		modName.setText(cursor.getString(subject));
		modAbbrev.setText(cursor.getString(abbrev));
		modImg.setColorFilter(cursor.getInt(imgColor));

	}

}
