package cn.peterchen.pets.common.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ORM注解，标注要映射为表的类
 * @author yizhi401
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
	
	/**
	 * 映射后的表名
	 * @return
	 */
	public String tableName() default "table_name";

}
