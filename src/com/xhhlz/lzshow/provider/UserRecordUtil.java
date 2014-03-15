package com.xhhlz.lzshow.provider;

import java.text.ParseException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xhhlz.lzshow.constant.UserRecord;
import com.xhhlz.lzshow.util.DateUtil;

public class UserRecordUtil {

	public static UserRecord queryLatestUserRecord(Context context)
			throws ParseException {
		UserRecord latestRecord = null;

		String latestFontLib = null;
		String latestTime = null;
		int latestIndex = -1;

		Cursor cursor = DatabaseUtil
				.query(context, UserRecord.TABLE_NAME, new String[] {
						UserRecord.UPDATE_TIME, UserRecord.FONTLIB_NAME,
						UserRecord.UPDATE_INDEX }, null, null, null, null, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String updateTime = cursor.getString(0);
			String fontLibTime = cursor.getString(1);
			int updateIndex = cursor.getInt(2);

			if (latestFontLib == null) {
				latestFontLib = fontLibTime;
				latestTime = updateTime;
				latestIndex = updateIndex;
			} else {
				// compare
				if (DateUtil.isBefore(latestTime, updateTime)) {
					latestFontLib = fontLibTime;
					latestTime = updateTime;
					latestIndex = updateIndex;
				}
			}
		}
		closedb(cursor);

		if (latestFontLib != null) {
			latestRecord = new UserRecord();
			latestRecord.setFontLibName(latestFontLib);
			latestRecord.setUpdateIndex(latestIndex);
		}

		return latestRecord;
	}

	public static int queryCurWordIndexByFontLibName(String fontLibName,
			Context context) {
		int curWordIndex = -1;
		Cursor cursor = DatabaseUtil.query(context, UserRecord.TABLE_NAME,
				new String[] { UserRecord.UPDATE_INDEX },
				UserRecord.FONTLIB_NAME + "=?", new String[] { fontLibName },
				null, null, null);

		if (cursor.moveToFirst())
			curWordIndex = cursor.getInt(0);

		closedb(cursor);

		return curWordIndex;
	}

	public static void insertLatestUserRecord(Context context,
			String fontLibName, int index) {

		Cursor cursor = DatabaseUtil.query(context, UserRecord.TABLE_NAME,
				new String[] { UserRecord.ID }, UserRecord.FONTLIB_NAME + "=?",
				new String[] { fontLibName }, null, null, null);

		ContentValues values = new ContentValues();

		if (cursor.moveToFirst()) {
			int userRecordId = cursor.getInt(0);
			closedb(cursor);
			values.put(UserRecord.UPDATE_INDEX, index);
			values.put(UserRecord.UPDATE_TIME, DateUtil.getCurrentDateTime());
			DatabaseUtil.update(context, UserRecord.TABLE_NAME, values,
					UserRecord.ID + "=?", new String[] { userRecordId + "" });
		} else {
			closedb(cursor);
			values.put(UserRecord.UPDATE_INDEX, index);
			values.put(UserRecord.FONTLIB_NAME, fontLibName);
			values.put(UserRecord.UPDATE_TIME, DateUtil.getCurrentDateTime());
			DatabaseUtil.insert(context, UserRecord.TABLE_NAME, UserRecord.ID,
					values);
		}
	}
	
	private static void closedb(Cursor cursor) {
		cursor.close();
		DatabaseUtil.closeDatabase();
	}
}
