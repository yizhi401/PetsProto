package cn.peterchen.pets.common.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ORM映射注解，标注要设为外键的属性
 * @author yizhi401
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ForeignKey {
	
	public Class<?> foreignKey();
	
}
