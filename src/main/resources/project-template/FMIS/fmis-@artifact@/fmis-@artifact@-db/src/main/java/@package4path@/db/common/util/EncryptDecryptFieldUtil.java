package @packageName@.db.common.util;

import @packageName@.db.common.bean.annotation.DecryptField;
import @packageName@.db.common.bean.annotation.EncryptField;
import io.bigplayers.tangerine.mybatisplus.spring.encrypt.AESUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

public class EncryptDecryptFieldUtil {
    /**
     * 对含注解字段解密
     *
     * @param t
     * @param <T>
     */
    public static <T> void decryptField(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        try {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(DecryptField.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(t);
                        System.out.println("fieldName："+field.getName()+";fieldValue: "+fieldValue);
                        if (StringUtils.isNotEmpty(fieldValue)) {
                            field.set(t, AESUtil.AESDecode(fieldValue));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对含注解字段加密
     *
     * @param t
     * @param <T>
     */
    public static <T> void encryptField(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        try {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(EncryptField.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(t);
                        if (StringUtils.isNotEmpty(fieldValue)) {
                            field.set(t, AESUtil.AESEncode(fieldValue));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 隐藏号码中间4位
     *
     * @param t
     * @param <T>
     */
    public static <T> void hidePhone(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        try {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(DecryptField.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(t);
                        if (StringUtils.isNotEmpty(fieldValue)) {
                            // 暂时与解密注解共用一个注解，该注解隐藏手机号中间四位
                            field.set(t, StringUtils.overlay(fieldValue, "****", 3, 7));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
