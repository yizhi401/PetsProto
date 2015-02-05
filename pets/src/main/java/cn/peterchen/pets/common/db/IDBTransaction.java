package cn.peterchen.pets.common.db;

import java.util.List;

/**
 * 数据库和实体类直接相互转化的接口
 * @author yizhi401
 *
 * @param <T>
 */
public interface IDBTransaction{

	/**
	 * 将一个list<实体类>写入数据库，如果数据库已经有该数据，则替换
	 * @param data  实体类要写入的内容
	 * @param clazz 实体类对应类
	 */
	public <T> boolean addDataToDB(List<T> data, Class<T> clazz);
	
	/**
	 * 将一个list<实体类>从数据库中删除，如果数据库中没有这条数据，不操作,仅根据_ID字段进行删除
	 * @param data  实体类要写入的内容
	 * @param clazz 实体类对应类
	 */
	public <T> boolean deleteDataInDB(List<T> data, Class<T> clazz);
	
	/**
	 * 将一个list<实体类>更新到数据库中,如果数据库中没有，则插入
	 * @param data  实体类要写入的内容
	 * @param clazz 实体类对应类
	 */
	public <T> boolean updateDataInDB(List<T> data, Class<T> clazz);
	
	/**
	 * 按照实体类中不为null的条件来查询数据库，并且返回为list<T>
	 * @param data  实体类要写入的内容
	 * @param clazz 实体类对应类
	 */
	public <T> List<T> queryDataFromDB(T item, Class<T> clazz);
}
