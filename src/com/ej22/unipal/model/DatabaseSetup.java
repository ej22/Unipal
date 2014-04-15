package com.ej22.unipal.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSetup {

	//Database info
	private static final String DB_NAME = "UniPal";
	private static final int DB_VERSION = 1;
	
	//Table Names
	private static final String TABLE_MODULE = "Module";
	private static final String TABLE_MOD_EV = "Mod_Events";
	private static final String TABLE_EVENT = "Events";
	private static final String TABLE_MOD_GRA = "Mod_Grade";
	private static final String TABLE_GRADE = "Grade";
	
	//Common columns across two or more tables
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_SUBJECT = "subject";
	public static final String KEY_MOD_ID = "mod_id";
	
	//Module Table column values
	public static final String KEY_ABBREVIATION = "abbreviation";
	public static final String KEY_COLOUR = "colour";
	
	//Event Table Column Values
	public static final String KEY_TYPE = "type";
	public static final String KEY_SUBTYPE = "subtype";
	public static final String KEY_DUE_DATE = "due_date";
	public static final String KEY_DESC = "description";
	
	//Grade Table Column Values
	public static final String KEY_EVENT = "event";
	public static final String KEY_GRADE = "grade";
	public static final String KEY_MAX_GRADE = "max_grade";
	public static final String KEY_WEIGHT = "weight";
	public static final String KEY_TOTAL_PERCENT = "total_percent";
	
	//MOD_EVENT and MOD_GRADE table column values
	public static final String KEY_GRADE_ID = "grade_id";
	public static final String KEY_EVENT_ID = "event_id";
	
	//Create Table Statements
	private static final String CREATE_MODULE_TABLE = 
	"create table " + TABLE_MODULE + " (" + 
		KEY_ID + " integer primary key autoincrement, " + 
		KEY_NAME + "text not null, " +
		KEY_ABBREVIATION + "text,  " + 
		KEY_COLOUR + "text not null);";
	
	private static final String CREATE_MOD_EV_TABLE =
	"create table " + TABLE_MOD_EV + " (" +
		KEY_ID + " integer primary key autoincrement, " + 
		KEY_MOD_ID + "integer, " + 
		KEY_GRADE_ID + "integer);";
	
	private static final String CREATE_EVENT_TABLE =
	"create table " + TABLE_EVENT + " (" +
		KEY_ID + " integer primary key autoincrement, " + 
		KEY_NAME +  " text not null, " + 
		KEY_SUBJECT + " text not null, " + 
		KEY_TYPE + " text not null, " +
		KEY_SUBTYPE + " text, " + 
		KEY_DUE_DATE + " text not null, " + 
		KEY_DESC + " text);";
	
	private static final String CREATE_MOD_GRA_TABLE = 
	"create table " + TABLE_MOD_GRA + "( " +
		KEY_ID + " integer primary key autoincrement, " +
		KEY_MOD_ID + "integer, " + 
		KEY_GRADE_ID + "integer);";
	
	private static final String CREATE_GRADE_TABLE =
	"create table " + TABLE_GRADE + " (" +
		KEY_ID + " integer primary key autoincrement, " + 
		KEY_SUBJECT + " text not null, "  +
		KEY_EVENT + " text not null, " +
		KEY_GRADE + " integer not null, " + 
		KEY_MAX_GRADE + " integer not null, "  + 
		KEY_WEIGHT + " integer not null, " + 
		KEY_TOTAL_PERCENT + " integer not null);";
	
	//Variables for use throughout the class
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DatabaseSetup(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context){
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_EVENT_TABLE);
			db.execSQL(CREATE_GRADE_TABLE);
			db.execSQL(CREATE_MOD_EV_TABLE);
			db.execSQL(CREATE_MOD_GRA_TABLE);
			db.execSQL(CREATE_MODULE_TABLE);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
		
	}//end nested class
	
	public DatabaseSetup open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		DBHelper.close();
	}
	
	public long insertEvent(String name, String subject, String type, String subType, String date, String desc){
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_SUBJECT, subject);
		initialValues.put(KEY_TYPE, type);
		initialValues.put(KEY_SUBTYPE, subType);
		initialValues.put(KEY_DUE_DATE, date);
		initialValues.put(KEY_DESC, desc);
		
		
		return db.insert(TABLE_EVENT, null, initialValues);
	}
	
	public long insertModule(String subject, String abbreviation, String color){
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_SUBJECT, subject);
		initialValues.put(KEY_ABBREVIATION, abbreviation);
		initialValues.put(KEY_COLOUR, color);
		
		return db.insert(TABLE_MODULE,null,initialValues);
	}
	
	
}
