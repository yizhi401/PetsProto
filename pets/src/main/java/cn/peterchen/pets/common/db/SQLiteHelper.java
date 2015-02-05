package cn.peterchen.pets.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "suyuntong.db";
	public static final int DATABASE_VERSION = 7;

	/**
	 * 数据的时间
	 */
	public static final String CLM_TIME = "time";

	public static final String CLM_SIGNTIME = "sign_time";


	/**
	 * 运单查询历史记录
	 */
	public static final String TBL_DELIVERY = "delivery";
	/**
	 * 运单号
	 */
	public static final String CLM_ID = "id";
	/**
	 * 当前状态
	 */
	public static final String CLM_STATUS = "status";
	/**
	 * 状态信息
	 */
	public static final String CLM_MESSAGE = "message";
	/**
	 * 运单别名，用户自定义
	 */
	public static final String CLM_ALIAS = "alias";


	/**
	 * 寄件人数据，json数据
	 */
	public static final String CLM_SENDER = "sender";
	/**
	 * 收件人列表，json 数组
	 */
	public static final String CLM_RECEIVER = "receivers";
	
	/**
	 * 汇率查询历史记录表
	 */
	public static final String TBL_EXCHANGE_RATE = "exchange_rate";
	public static final String CLM_SRC = "src";
	public static final String CLM_DEST = "dest";
	public static final String CLM_QUANTITY = "quantity";
	public static final String CLM_DATE = "date";
	public static final String CLM_SRC_NAME = "src_name";
	public static final String CLM_DEST_NAME = "dest_name";
	public static final String CLM_TOTAL = "total";
	
	/**
	 * 运费查询
	 */
	public static final String TBL_FREIGHT = "freight";
	public static final String CLM_SENDER_REGION = "sender_region";
	public static final String CLM_RECEIVER_REGION = "receiver_region";
	public static final String CLM_UNIT = "unit";
	public static final String CLM_WEIGHT = "weight";
	public static final String CLM_FEIGHT_TYPE = "type";
	public static final String CLM_RESULT = "result";

	/**
	 * 地址簿缓存表
	 *//*
	
	/**
	 * 电子运单表
	 */
    public static final String E_CLM_PROVINCE_ID="province_id";
    
    public static final String E_CLM_PROVINCE_NAME="province_name";
    
    public static final String E_CLM_CITY_ID="city_id";
    
    public static final String E_CLM_CITY_NAME="city_name";
    
    public static final String  E_CLM_COUNTY_ID="county_id";
    
    public static final String  E_CLM_COUNTY_NAME="county_name";
    
    public static final String  E_CLM_SENDER="sender";

	private static SQLiteHelper usHelper;

	public static SQLiteHelper getInstance(Context ctx) {
		if (usHelper == null) {
			usHelper = new SQLiteHelper(ctx, SQLiteHelper.DATABASE_NAME, null,
					SQLiteHelper.DATABASE_VERSION);
		}
		return usHelper;
	}

	private SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_DELIVERY + " ("
				+ BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY," + CLM_ID
				+ " TEXT NOT NULL," + CLM_STATUS + " TEXT," + CLM_MESSAGE
				+ " TEXT," + CLM_SIGNTIME + " TEXT," + CLM_ALIAS + " TEXT,"
				+ CLM_TIME + " TEXT NOT NULL" + ");");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_EXCHANGE_RATE + " ("
				+ BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY," + CLM_SRC
				+ " TEXT NOT NULL," + CLM_DEST + " TEXT NOT NULL,"
				+ CLM_SRC_NAME + " TEXT NOT NULL," + CLM_DEST_NAME + " TEXT NOT NULL,"
				+ CLM_TOTAL + " TEXT NOT NULL,"
				+ CLM_QUANTITY + " TEXT NOT NULL," + CLM_DATE
				+ " TEXT NOT NULL," + CLM_TIME + " TEXT NOT NULL" + ");");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_FREIGHT + " ("
				+ BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
				+ CLM_SENDER_REGION + " TEXT NOT NULL," + CLM_RECEIVER_REGION
				+ " TEXT NOT NULL," + CLM_UNIT + " TEXT NULL," + CLM_WEIGHT
				+ " TEXT NOT NULL," + CLM_RESULT+ " TEXT NOT NULL," + CLM_FEIGHT_TYPE + " TEXT," + CLM_TIME
				+ " TEXT NOT NULL" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TBL_DELIVERY);
		db.execSQL("DROP TABLE IF EXISTS " + TBL_EXCHANGE_RATE);
		db.execSQL("DROP TABLE IF EXISTS " + TBL_FREIGHT);
		onCreate(db);
	}
}
