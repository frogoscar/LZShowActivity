package com.xhhlz.lzshow.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xhhlz.lzshow.constant.Record;
import com.xhhlz.lzshow.util.DateUtil;

public class RecordUtil {

	public static boolean addWriteSumToDb(Context context, int writeSum) {
		String currentDate = DateUtil.getCurrentDate();
		Cursor cursor = DatabaseUtil.query(context, Record.TABLE_NAME,
				new String[] { Record.ID, Record.FONTNUM },
				Record.DATE + "=?", new String[] { currentDate }, null,
				null, null);

		ContentValues contentValues = new ContentValues();

		if (cursor.moveToFirst()) {
			int recordSum = cursor.getInt(1);
			int recordId = cursor.getInt(0);
			closedb(cursor);

			recordSum += writeSum;
			contentValues.put(Record.FONTNUM, recordSum);

			// ÐÞ¸ÄÊý¾Ý¿â
			int updateRows = DatabaseUtil.update(context,
					Record.TABLE_NAME, contentValues, Record.ID + "=?",
					new String[] { recordId + "" });
			if (updateRows > 0)
				return true;
		} else {
			closedb(cursor);
			contentValues.put(Record.FONTNUM, writeSum);
			contentValues.put(Record.DATE, currentDate);
			long insertRows = DatabaseUtil.insert(context,
					Record.TABLE_NAME, Record.ID, contentValues);
			if (insertRows > 0)
				return true;
		}

		return false;
	}

	public static int[] queryWriteSumByDate(int year, int month, Context context) {
		int daySum = DateUtil.getDaySum(year, month);
		int query[] = new int[daySum];

		for (int index = 0; index < daySum; index++)
			query[index] = 0;

		Cursor cursor = DatabaseUtil.query(context, Record.TABLE_NAME,
				new String[] { Record.FONTNUM, Record.DATE },
				Record.DATE + " LIKE ?", new String[] { year + "-"
						+ (month < 10 ? "0" + month : month) + "%" }, null,
				null, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String date = cursor.getString(1);
			int fontnum = cursor.getInt(0);
			int day = Integer.parseInt(date.split("-")[2]);
			query[day - 1] = fontnum;
		}
		closedb(cursor);

		return query;
	}

	public static int queryFontSum(Context context) {
		int fontSum = 0;

		Cursor cursor = DatabaseUtil.query(context, Record.TABLE_NAME,
				new String[] { "SUM(" + Record.FONTNUM + ") AS total" }, null, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			fontSum = cursor.getInt(0);
		}
		closedb(cursor);

		return fontSum;
	}

	public static int queryTodayFontSum(Context context) {
		int todayFontSum = 0;

		Cursor cursor = DatabaseUtil.query(context, Record.TABLE_NAME,
				new String[] { Record.FONTNUM }, Record.DATE + "=?",
				new String[] { DateUtil.getCurrentDate() }, null, null, null);
		
		if (cursor.moveToFirst()) {
			todayFontSum = cursor.getInt(0);
		}
		closedb(cursor);

		return todayFontSum;
	}
	
	public static int queryRecordRows(Context context) {
		int recordRows = 0;

		Cursor cursor = DatabaseUtil.query(context, Record.TABLE_NAME,
				new String[] { "COUNT(" + Record.FONTNUM + ") AS total" }, null, null,
				null, null, null);
		
		if (cursor.moveToFirst()) {
			recordRows = cursor.getInt(0);
		}
		closedb(cursor);

		return recordRows;
	}

	private static void closedb(Cursor cursor) {
		cursor.close();
		DatabaseUtil.closeDatabase();
	}

}
