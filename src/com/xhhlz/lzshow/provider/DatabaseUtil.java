package com.xhhlz.lzshow.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.sdk.pen.pen.*;

/**
 * 操作数据库的类，完成数据表插入、删除、查询以及修改数据字段的相关操作
 * @author enming xie
 *
 */
public class DatabaseUtil {	
	private static SQLiteDatabase db = null;
	private static DatabaseHelper helper = null;

	//向表插入字段
	public static long insert(Context context, String tableName, String id, ContentValues values) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		long rows = db.insert(tableName,id,values);
		closeDatabase();
		return rows;
	}
	
	//向表刪除字段
	public static int delete(Context context, String tableName, String where, String[] whereArgs) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		int rows = db.delete(tableName,where,whereArgs);
		closeDatabase();
		return rows;
	}
	
	//使用SQL语句删除字段
	public static void delete(Context context, String sql) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		db.execSQL(sql);
		closeDatabase();
	}
	
	//向表修改字段
	public static int update(Context context, String tableName, ContentValues values, String where, String[] whereArgs) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		int rows = db.update(tableName,values,where,whereArgs);
		closeDatabase();
		return rows;
	}
	
	//向表查询数据
	public static Cursor query(Context context, String table, 
			String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		helper = new DatabaseHelper(context);
		db = helper.getReadableDatabase();
		return db.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);
	}
	
	public static void closeDatabase() {
		db.close();
	}
}
