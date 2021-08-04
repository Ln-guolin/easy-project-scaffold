package @packageName@.db.common.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: hsf
 * @Date: 2018/8/3 18:30
 * @Description:字段加密注解
 */
@Target({ElementType.FIELD})//作用域是字段
@Retention(RetentionPolicy.RUNTIME)//注解类型：运行时注解
public @interface EncryptField {
    String value() default "";
}
