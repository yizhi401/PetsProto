package cn.peterchen.pets.common.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 数据库基础类，提供将实体类的List与数据库之间相互转化功能，支持增删改查
 *
 * @author yizhi401
 */
public class EntityDBTransaction implements IDBTransaction {

    /**
     * 不管是转换哪一张表，dbHelper都是一定的
     */
    private static CommonDBHelper dbHelper = null;

    private static EntityDBTransaction mInstance = null;

    public static EntityDBTransaction getInstance(Context context) {
        if (mInstance == null) {
            synchronized (EntityDBTransaction.class) {
                if (mInstance == null)
                    mInstance = new EntityDBTransaction(context);
            }
        }
        return mInstance;
    }

    /**
     * 单例模式私有构造方法
     *
     * @param context
     */
    private EntityDBTransaction(Context context) {
        if (dbHelper == null) {
            dbHelper = CommonDBHelper.getInstance(context);
        }
    }

    /**
     * 参加相关接口说明IDBTransaction
     *
     * @param data
     * @param clazz
     */
    @Override
    public <T> boolean addDataToDB(List<T> data, Class<T> clazz) {

        if (data == null || data.size() == 0) {
            return false;
        }

        // 如果给的clazz不是一张表，也return
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // 将data中的数据插入db
            for (int i = 0; i < data.size(); i++) {
                T item = data.get(i);

                ContentValues value = getValueFromEntity(item, clazz);
                db.insert(table.tableName(), null, value);
            }

            db.setTransactionSuccessful();
            return true;

        } catch (Exception e) {
            Log.i("mInfo", "插入数据失败");

        } finally {
            db.endTransaction();
        }
        return false;
    }

    /**
     * 不重复的插入一条数据，如果表中有完全一样的数据就不插入，并且返回true
     *
     * @param data
     * @param clazz
     * @return 表中有这条数据返回true, 否则返回false
     */
    public <T> boolean addItemWithoutRepetition(T data, Class<T> clazz) {
        if (data == null)
            return false;
        // 如果给的clazz不是一张表，也return
        List<T> result = queryDataFromDB(data, clazz);
        if (result != null && result.size() > 0) {
            return true;
        }
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues value = getValueFromEntity(data, clazz);
            db.insert(table.tableName(), null, value);

            db.setTransactionSuccessful();
            return true;

        } catch (Exception e) {
            Log.i("mInfo", "插入数据失败");

        } finally {
            db.endTransaction();
        }
        return false;
    }

    /**
     * 从数据库中删除数据，仅支持根据ID删除对应的条目，其他删除请自行操作 比如，如果要删除某一类数据，可以先调用查询来查询出这些数据，再传过来删除。
     * <p/>
     * 注意：经测验，删除一条数据后，这个_ID就没有了，数据的_ID不会自动变更排序
     *
     * @param data  要删除数据的集合，在此集合中的数据均会被删除
     * @param clazz 表
     */
    @Override
    public <T> boolean deleteDataInDB(List<T> data, Class<T> clazz) {

        Table table = clazz.getAnnotation(Table.class);

        if (table == null || data == null || data.size() == 0) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            for (int i = 0; i < data.size(); i++) {
                T item = data.get(i);

                Field field = clazz.getDeclaredField("_ID");
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                String selection = field.getName() + " =?";
                String[] selectionArgs = {String.valueOf(field.get(item))};

                db.delete(table.tableName(), selection, selectionArgs);

            }

            db.setTransactionSuccessful();
            return true;
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return false;
    }

    /**
     * 更新表中数据， 按照ID来进行更新，更新全部column
     *
     * @param data  要更新的条目集合
     * @param clazz
     */
    @Override
    public <T> boolean updateDataInDB(List<T> data, Class<T> clazz) throws IllegalArgumentException {

        Table table = clazz.getAnnotation(Table.class);

        if (table == null || data == null || data.size() == 0) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            for (int i = 0; i < data.size(); i++) {
                T item = data.get(i);

                Field field = clazz.getDeclaredField("_ID");
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                String selection = field.getName() + " =?";
                String[] selectionArgs = {String.valueOf(field.get(item))};

                ContentValues value = this.getValueFromEntity(item, clazz);

                db.update(table.tableName(), value, selection, selectionArgs);
            }

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return false;

    }

    /**
     * 从数据库中查询，只能提供一个查询条件，将item中不为null的column作为查询条件
     * 注意：目前不为null的语句均作为and连接，如果要使用or来连接，请采取多次查询
     * 策略，或者自己写sql语句,结果将会返回所有的column，如果要指定的column请自行查询, 查询结果按照ID升序排列
     * <p/>
     * 注意：查询的表中必须含有_ID这一列,因为是按照_ID来排序的
     *
     * @param item  查询条件 为null表示查询所有项
     * @param clazz 查询的实体类
     * @return List<T> 返回结果
     */
    @Override
    public <T> List<T> queryDataFromDB(T item, Class<T> clazz) {

        Table table = clazz.getAnnotation(Table.class);

        if (table == null) {
            return null;
        }

        final String CONDITION = "=?";
        final String AND = " AND ";

        StringBuffer selection = null;
        List<String> selectionArgs = new ArrayList<String>();

        if (item != null) {

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    // item 转化为查询条件selection 和 selectionArgs
                    try {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        // item的这个属性不为null，说明是查询条件
                        if (field.get(item) != null) {
                            if (selection == null) {
                                selection = new StringBuffer();
                                selection.append(field.getName());
                                selection.append(CONDITION);
                            } else {
                                selection.append(AND);
                                selection.append(field.getName());
                                selection.append(CONDITION);
                            }

                            // 如果是string类型直接添加
                            if (field.getType().equals(String.class)) {
                                selectionArgs.add((String) field.get(item));
                            }

                            // 如果是Integer类型，转化为string
                            if (field.getType().equals(Integer.class)) {
                                selectionArgs.add((field.get(item)).toString());
                            }

                            if (field.getType().equals(Long.class)) {
                                selectionArgs.add((field.get(item)).toString());
                            }
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        // 得到查询结果cursor
        String sortOrder = "_ID ASC";
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        if (selection == null) {

            cursor = db.query(table.tableName(), null, null, null, null, null,
                    sortOrder);
        } else {
            cursor = db.query(table.tableName(), null, selection.toString(),
                    selectionArgs.toArray(new String[selectionArgs.size()]),
                    null, null, sortOrder);
        }
        // 将cursor转化为实体类list<T>并返回
        List<T> data = new ArrayList<T>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            T newItem = cursorToEntity(cursor, clazz);
            data.add(newItem);
            cursor.moveToNext();
        }

        cursor.close();
        cursor = null;

        return data;
    }

    private <T> T cursorToEntity(Cursor cursor, Class<T> clazz) {

        try {
            T item = clazz.newInstance();

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }

                    int columnIndex = cursor.getColumnIndex(field.getName());

                    if (field.getType().equals(String.class)) {
                        field.set(item, cursor.getString(columnIndex));
                    }

                    if (field.getType().equals(Integer.class)) {
                        field.set(item, cursor.getInt(columnIndex));
                    }

                    if (field.getType().equals(Long.class)) {
                        field.set(item, cursor.getLong(columnIndex));
                    }
                }
            }

            return item;
        } catch (InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据所传入的实体类，将其转化为ContentValue以供数据库读写使用
     *
     * @param item  实体类
     * @param clazz 实体类的clazz
     * @return
     */
    private <T> ContentValues getValueFromEntity(T item, Class<T> clazz) {

        ContentValues value = new ContentValues();
        Field[] fields = clazz.getDeclaredFields();

        // 将所有的column放入value，准备写入数据库
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {

                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }

                    if (field.getType().equals(String.class)) {
                        value.put(field.getName(), (String) field.get(item));
                    }
                    if (field.getType().equals(Integer.class)) {
                        value.put(field.getName(), (Integer) field.get(item));
                    }
                    if (field.getType().equals(Long.class)) {
                        value.put(field.getName(), (Long) field.get(item));
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}
