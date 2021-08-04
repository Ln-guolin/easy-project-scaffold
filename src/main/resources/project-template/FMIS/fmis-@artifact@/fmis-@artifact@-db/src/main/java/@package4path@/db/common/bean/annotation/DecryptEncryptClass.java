package @packageName@.db.common.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: hsf
 * @Date: 2018/8/6 18:30
 * @Description:类是否含有加解密字段注解
 */
@Target({ElementType.TYPE})//作用域是类或者接口
@Retention(RetentionPolicy.RUNTIME)//注解类型：运行时注解
public @interface DecryptEncryptClass {
    String value() default "";
}
