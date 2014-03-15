package com.xhhlz.lzshow.provider;

import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.xhhlz.lzshow.constant.Record;
import com.xhhlz.lzshow.constant.UserRecord;

/**
 * 操作数据库的一个类，完成创建Record表和删除Record表的操作
 * @author enming xie
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper{

	private static int DATABASE_VERSION = 1;  //数据库版本
	private static String DATABASE_NAME = "lzshow"; //数据库名称
	
	//创建Record表
	private static String CREATE_RECORD_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + Record.TABLE_NAME + "("
		+ Record.ID + " INTEGER PRIMARY KEY,"
		+ Record.DATE + " TEXT,"
		+ Record.FONTNUM + " INTEGER)";
	
	private static String CREATE_USER_RECORD_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "+UserRecord.TABLE_NAME+"("
	+UserRecord.ID+" INTEGER PRIMARY KEY,"
	+UserRecord.FONTLIB_NAME+" TEXT,"
	+UserRecord.UPDATE_INDEX+" INTEGER,"
	+UserRecord.UPDATE_TIME+" TEXT)";
	
	//删除Record表
	private static String DROP_RECORD_TABLE_SQL = "DROP TABLE IF EXISTS " + Record.TABLE_NAME;
	
	private static String DROP_USER_RECORD_TABLE_SQL = "DROP TABLE IF EXISTS " + UserRecord.TABLE_NAME;
	
	
	public DatabaseHelper(Context context) {
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}
	
	/**
	 * 创建Record表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_RECORD_TABLE_SQL); //创建Record表
		db.execSQL(CREATE_USER_RECORD_TABLE_SQL);
		autoInsertData(db);
	}

	/**
	 * 更新Record表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_RECORD_TABLE_SQL); //删除Record表
		db.execSQL(DROP_USER_RECORD_TABLE_SQL);
		onCreate(db);//创建Record表
	}
	
	private ContentValues autoGenerateValues(String date, int fontNum) {
		ContentValues values = new ContentValues();
		values.put(Record.DATE, date);
		values.put(Record.FONTNUM, fontNum);
		return values;
	}
	
	private void autoInsertData(SQLiteDatabase db) {
		// start from 2012-6-1 2012-7-18
		String date = "2012-06-";
		Random random = new Random();
		int fontNum = 0;
		int min = 20;
		int max = 200;
		for(int day=1; day<31; day++)
		{
			fontNum = random.nextInt(max)%(max-min+1) + min;
			db.insert(Record.TABLE_NAME, Record.ID, autoGenerateValues(date+(day<10?"0"+day:day), fontNum));
		}
		date = "2012-07-";
		for(int day=1; day<19; day++)
		{
			fontNum = random.nextInt(max)%(max-min+1) + min;
			db.insert(Record.TABLE_NAME, Record.ID, autoGenerateValues(date+(day<10?"0"+day:day), fontNum));
		}
	}
}
