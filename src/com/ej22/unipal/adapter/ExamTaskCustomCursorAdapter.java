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

public class ExamTaskCustomCursorAdapter extends SimpleCursorAdapter{
	private Context context;
	private int layout;
	private Cursor cursor;
	final private LayoutInflater inflate;
	
	public ExamTaskCustomCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context = context;
		this.layout = layout;
		cursor = c;
		this.inflate = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
            return inflate.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView date=(TextView)view.findViewById(R.id.dateView);
        TextView subject=(TextView)view.findViewById(R.id.SubjectName);
        TextView task=(TextView)view.findViewById(R.id.TaskName);
        
        int dateSelect = cursor.getColumnIndex(DatabaseSetup.KEY_DUE_DATE);
        int subjectSelect = cursor.getColumnIndex(DatabaseSetup.KEY_SUBJECT);
        int taskSelect = cursor.getColumnIndex(DatabaseSetup.KEY_NAME);
        
        date.setText(cursor.getString(dateSelect));
        subject.setText(cursor.getString(subjectSelect));
        task.setText(cursor.getString(taskSelect));
    }
        
}
