package @packageName@.db.common.bean.interceptor;

import @packageName@.db.common.bean.annotation.DecryptEncryptClass;
import @packageName@.db.common.bean.annotation.DecryptField;
import @packageName@.db.common.util.EncryptDecryptFieldUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Auther: hsf
 * @Date: 2018/8/3 18:30
 * @Description:字段加解密功能
 */
@Intercepts({
        @Signature(type=Executor.class,method="update",args={MappedStatement.class,Object.class}),
        @Signature(type=Executor.class,method="query",args={MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class})
})
public class DBInterceptor implements Interceptor {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    public DBInterceptor() {
    }
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        Object parameter = invocation.getArgs()[1];

        Object returnValue = null;
        try {
            if (StringUtils.equalsIgnoreCase("update", methodName)
                    || StringUtils.equalsIgnoreCase("insert", methodName)) {
                if(parameter instanceof HashMap) {
                    Map map = (HashMap)parameter;
                    Object obj = map.get("param1");
                    if(obj==null){
                        if(map.get("param2") != null &&
                                map.get("param2").getClass().isAnnotationPresent(DecryptEncryptClass.class)){
                            EncryptDecryptFieldUtil.encryptField(map.get("param2"));
                        }
                    }else{
                        if(obj.getClass().isAnnotationPresent(DecryptEncryptClass.class)){
                            EncryptDecryptFieldUtil.encryptField(map.get("param1"));
                        }
                    }
                }else{
                    if(parameter.getClass().isAnnotationPresent(DecryptEncryptClass.class)){
                        EncryptDecryptFieldUtil.encryptField(parameter);
                    }
                }
            }
            returnValue = invocation.proceed();
            if (StringUtils.equalsIgnoreCase("query", methodName) && returnValue instanceof ArrayList<?>) {
                List<?> list = (ArrayList<?>) returnValue;
                if (null == list || 1 > list.size()){
                    return returnValue;
                }
                Object obj = list.get(0);
                if (null == obj || ! obj.getClass().isAnnotationPresent(DecryptEncryptClass.class)) {
                    return returnValue;
                }
                // 判断第一个对象是否有DecryptField注解
                Field[] fields = obj.getClass().getDeclaredFields();
                int len;
                if (null != fields && 0 < (len = fields.length)) {
                    // 标记是否有解密注解
                    boolean isD = false;
                    for (int i = 0; i < len; i++) {
                        /**
                         * 由于返回的是同一种类型列表，因此这里判断出来之后可以保存field的名称
                         * 之后处理所有对象直接按照field名称查找Field从而改之即可
                         * 有可能该类存在多个注解字段，所以需要保存到数组（项目中目前最多是2个）
                         * */
                        if (fields[i].isAnnotationPresent(DecryptField.class)) {
                            isD = true;
                            break;
                        }
                    }
                    // 将含有DecryptField注解的字段解密
                    if (isD) {
                        list.forEach(l -> EncryptDecryptFieldUtil.decryptField(l));
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            // 直接返回原结果即可
            return returnValue;
        }
        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
