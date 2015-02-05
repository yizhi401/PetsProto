package cn.peterchen.pets.common.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * 	汇率查询历史表
  * @ClassName: ExchangeRateResolver
 */
public class ExchangeRateResolver {

	private static ExchangeRateResolver resolver;
	private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static ExchangeRateResolver getInstance(Context ctx) {
		if (resolver == null) {
			resolver = new ExchangeRateResolver(ctx);
		}
		return resolver;
	}

	private SQLiteHelper helper;

	private ExchangeRateResolver(Context ctx) {
		helper = SQLiteHelper.getInstance(ctx);
	}

	public synchronized void insertRecord(String src, String dest,
			String quantity, String date ,String srcName ,String destName , String total) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.CLM_SRC, src);
		values.put(SQLiteHelper.CLM_DEST, dest);
		values.put(SQLiteHelper.CLM_QUANTITY, quantity);
		values.put(SQLiteHelper.CLM_DATE, date);
		values.put(SQLiteHelper.CLM_TIME, sdf.format(new Date()));
		values.put(SQLiteHelper.CLM_SRC_NAME, srcName);
		values.put(SQLiteHelper.CLM_DEST_NAME, destName);
		values.put(SQLiteHelper.CLM_TOTAL, total);
		
		try {
			db.insert(SQLiteHelper.TBL_EXCHANGE_RATE, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public synchronized List<ContentValues> fetchRecords(String limit) {
		List<ContentValues> all = new ArrayList<ContentValues>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = null;
		try {
			c = db.query(SQLiteHelper.TBL_EXCHANGE_RATE, null, null, null,
					null, null, SQLiteHelper.CLM_TIME + " DESC ", limit);
			if (c != null && c.moveToFirst()) {
				while (!c.isAfterLast()) {
					ContentValues cv = new ContentValues();
					DatabaseUtils.cursorRowToContentValues(c, cv);
					all.add(cv);
					c.moveToNext();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
			db.close();
		}
		return all;
	}

	public synchronized boolean deletRecords() {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			db.delete(SQLiteHelper.TBL_EXCHANGE_RATE, null, null);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	
	@SuppressWarnings("unused")
	public synchronized boolean deletRecords(int i) {

		String str = "" + i;
		if (str == null)
			return false;
		SQLiteDatabase db = helper.getWritableDatabase();
		String whereClause = BaseColumns._ID + " = '" + str + "'";
		try {
			db.delete(SQLiteHelper.TBL_EXCHANGE_RATE, whereClause, null);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		} finally {
			db.close();
		}

		return true;
	}
}
