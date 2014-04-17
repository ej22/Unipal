package com.ej22.unipal.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
	private static final String TABLE_EXAM = "Exam";
	private static final String TABLE_TASK = "Task";

	//Common columns across two or more tables
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_SUBJECT = "subject";
	public static final String KEY_MOD_ID = "mod_id";
	
	//Module Table column values
	public static final String KEY_ABBREVIATION = "abbreviation";
	public static final String KEY_COLOUR = "colour";
	public static final String[] allModules = {KEY_ID, KEY_SUBJECT, KEY_ABBREVIATION, KEY_COLOUR};
	
	//Event Table Column Values
	public static final String KEY_TYPE = "type";
	public static final String KEY_SUBTYPE = "subtype";
	public static final String KEY_DUE_DATE = "due_date";
	public static final String KEY_DESC = "description";
	public static final String[] allEvents = {KEY_ID,KEY_NAME,KEY_SUBJECT, KEY_TYPE, KEY_DUE_DATE};
	
	public static final String KEY_EVENT_ID = "event_id";
	
	//Create Table Statements
	private static final String CREATE_MODULE_TABLE = 
	"create table " + TABLE_MODULE + " (" + 
		KEY_ID + " integer primary key autoincrement, " + 
		KEY_SUBJECT + " text not null, " +
		KEY_ABBREVIATION + " text,  " + 
		KEY_COLOUR + " int not null);";
	
	private static final String CREATE_MOD_EV_TABLE =
	"create table " + TABLE_MOD_EV + " (" +
		KEY_ID + " integer primary key autoincrement, " + 
		KEY_MOD_ID + "integer, " + 
		KEY_EVENT_ID + "integer);";
	
	private static final String CREATE_TASK_TABLE =
	"create table " + TABLE_EXAM + " (" +
		KEY_ID + " integer primary key autoincrement, " + 
		KEY_NAME +  " text not null, " + 
		KEY_SUBJECT + " text not null, " + 
		KEY_TYPE + " text not null, " +
		KEY_SUBTYPE + " text, " + 
		KEY_DUE_DATE + " text not null, " + 
		KEY_DESC + " text);";
	
	private static final String CREATE_EXAM_TABLE =
			"create table " + TABLE_TASK + " (" +
				KEY_ID + " integer primary key autoincrement, " + 
				KEY_NAME +  " text not null, " + 
				KEY_SUBJECT + " text not null, " + 
				KEY_TYPE + " text not null, " +
				KEY_SUBTYPE + " text, " + 
				KEY_DUE_DATE + " text not null, " + 
				KEY_DESC + " text);";
	
	
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
			db.execSQL(CREATE_EXAM_TABLE);
			db.execSQL(CREATE_TASK_TABLE);
			db.execSQL(CREATE_MOD_EV_TABLE);
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
	
	//----------------------------INSERT-------------------------------------//
	
	public long insertExam(String name, String subject, String type, String subType, String date, String desc){
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_SUBJECT, subject);
		initialValues.put(KEY_TYPE, type);
		initialValues.put(KEY_SUBTYPE, subType);
		initialValues.put(KEY_DUE_DATE, date);
		initialValues.put(KEY_DESC, desc);
		
		
		return db.insert(TABLE_EXAM, null, initialValues);
	}
	
	public long insertTask(String name, String subject, String type, String subType, String date, String desc){
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_SUBJECT, subject);
		initialValues.put(KEY_TYPE, type);
		initialValues.put(KEY_SUBTYPE, subType);
		initialValues.put(KEY_DUE_DATE, date);
		initialValues.put(KEY_DESC, desc);
		
		
		return db.insert(TABLE_TASK, null, initialValues);
	}
	
	public long insertModule(String subject, String abbreviation, String color){
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_SUBJECT, subject);
		initialValues.put(KEY_ABBREVIATION, abbreviation);
		initialValues.put(KEY_COLOUR, color);
		
		return db.insert(TABLE_MODULE,null,initialValues);
	}
	
	//----------------------------GET-------------------------------------//
	
	public Cursor getAllModules() {
		String where = null;
		Cursor c = 	db.query(true, TABLE_MODULE, allModules, 
							where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}	
	
	public Cursor getAllTasks() {
		Cursor cTask = db.query(true, TABLE_TASK, allEvents,
				null, null,null,null,null,null);
		
		if (cTask != null){
			cTask.moveToFirst();
		}
		
		return cTask;
	}
	
	public Cursor getAllExams() {
		Cursor c = 	db.query(true, TABLE_EXAM, allEvents,
				null, null,null,null,null,null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	public List<String> getMouduleTitles(){
		List<String> titles = new ArrayList<String>();
		
		String query = "Select " + KEY_SUBJECT + " from " + TABLE_MODULE;
		Cursor cursor = db.rawQuery(query, null);
		
		if(cursor.moveToFirst()){
			do{
				titles.add(cursor.getString(0));
			}while(cursor.moveToNext());
			cursor.close();
		}
		return titles;
	}
	
	public Cursor getModuleDetails (long id)
	{
		String where = KEY_ID + "=" + id;
		Cursor cursor = db.query(true, TABLE_MODULE, allModules,
				where,null,null,null,null,null);
		
		if(cursor != null){
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	
	//----------------------------DELETE-------------------------------------//
	
	public boolean deleteModule(long id){
		return db.delete(TABLE_MODULE, KEY_ID + "=" + id, null) >0;
	}
	
	public boolean deleteTask(long id){
		return db.delete(TABLE_TASK, KEY_ID + "=" + id, null) >0;
	}
	
	public boolean deleteExam(long id){
		return db.delete(TABLE_EXAM, KEY_ID + "=" + id, null) >0;
	}
	
	//----------------------------UPDATE-------------------------------------//
	
	public boolean updateModule(long id, String subject, String abbreviation, String color){
		ContentValues args = new ContentValues();
		args.put(KEY_SUBJECT, subject);
		args.put(KEY_ABBREVIATION, abbreviation);
		args.put(KEY_COLOUR, color);
		return db.update(TABLE_MODULE, args, KEY_ID + "=" + id, null)>0;
	}
}
