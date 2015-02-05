package cn.peterchen.pets.common.db;

import java.lang.reflect.Field;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 数据库管理类，程序所有数据存于此。要添加一个新表或者删除一个表， 除了更新相应的实体类， 还要记得在这里执行建表或者删除表的功能。
 * <p/>
 * 该类仅提供以下功能： 1. 建立一个数据库（不需要手动调用） 2. 建立表或者删除表（不需要手动调用）
 * <p/>
 * 使用方法： 1. 获取该类的实例，通过getInstance来获取 2.
 * 调用getWritableDatabase、或者getReadableDatabase来获取db实例
 *
 * @author yizhi401
 */
public class CommonDBHelper extends SQLiteOpenHelper {

    public static CommonDBHelper mInstance = null;

    public static final String DATABASE_NAME = "sf_database.db";

    public static final int DATABASE_VERSION = 17;

    // 以下三个变量均为构建表时候sql语句所用
    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT,";

    /**
     * 只提供单例模式，因为全局只需要一个db对象
     *
     * @param context
     * @return
     */
    public static CommonDBHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (CommonDBHelper.class) {
                if (mInstance == null)
                    mInstance = new CommonDBHelper(context);
            }
        }
        return mInstance;
    }

    private CommonDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    protected CommonDBHelper(Context context, String databaseName,
                             int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
    }

    /**
     * 创立数据库时候所调用方法，所有表在此建表。
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 在此建表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 消息表、用户表增加字段,删除重建
    }

    /**
     * 根据所提供的实体类在数据库中建表，如果db中存在此表，直接返回
     *
     * @param <T>
     * @param db    要建表的数据库
     * @param clazz 实体类
     */
    public <T> void createTable(SQLiteDatabase db, Class<T> clazz) {

        StringBuffer createTableSQL = new StringBuffer("CREATE TABLE ");
        Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            createTableSQL.append(table.tableName());
            createTableSQL.append(" (");

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    if (field.getAnnotation(Column.class).isPrimaryKey()) {
                        createTableSQL.append(field.getName());
                        createTableSQL.append(PRIMARY_KEY);

                    } else if (field.getType().getName()
                            .equals(Integer.class.getName())) {
                        createTableSQL.append(field.getName());
                        createTableSQL.append(INTEGER_TYPE);
                        createTableSQL.append(COMMA_SEP);
                    } else {
                        createTableSQL.append(field.getName());
                        createTableSQL.append(TEXT_TYPE);
                        createTableSQL.append(COMMA_SEP);
                    }

                }
            }
            // 删除末尾多加的逗号
            createTableSQL.deleteCharAt(createTableSQL.length() - 1);
            createTableSQL.append(" )");
            db.execSQL(createTableSQL.toString());
        }

    }

    /**
     * 根据所提供的实体类删除相应的表，如果db无此表，不进行操作
     *
     * @param <T>
     * @param db    数据库
     * @param clazz 实体类
     */
    public <T> void deleteTable(SQLiteDatabase db, Class<T> clazz) {
        StringBuffer deleteTableSQL = new StringBuffer("DROP TABLE IF EXISTS ");
        Table table = clazz.getAnnotation(Table.class);

        if (table != null) {
            deleteTableSQL.append(table.tableName());
            db.execSQL(deleteTableSQL.toString());
        }
    }

}
