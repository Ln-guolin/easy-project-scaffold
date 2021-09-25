package cn.ex.project.scaffold.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 模版变量替换工具
 *
 * @author: Chen GuoLin
 * @create: 2021-09-26 02:14
 **/
public class TemplateVariableReplaceUtils {

    /**
     * 将字符转换为首字母大写的驼峰形式（支持分隔符：./_-）
     * abc-def -> AbcDef
     * @param name 分隔符：./_-连接的字符串
     * @return 首字母大写的驼峰格式字符串
     */
    public static String parse2humpCase(String name) {
        if (StringUtils.isEmpty(name)) {
            return StringUtils.EMPTY;
        }
        // 统一将字符处理为"-"，作为字符串分隔字符
        name = parse2link(name,"-");
        // 拆分字符
        String[] strArr = name.split("-");

        StringBuilder sb = new StringBuilder();
        for (String str : strArr) {
            String firstChar = new String(new char[] {str.charAt(0)}).toUpperCase();
            String left = str.substring(1);
            sb.append(firstChar).append(left);
        }
        return sb.toString();
    }

    /**
     * 将字符转换为驼峰形式（支持分隔符：./_-）
     * abc-def -> abcDef
     * @param name 分隔符：./_-连接的字符串
     * @return 驼峰格式的字符串
     */
    public static String parse2hump(String name) {
        String humpCase = parse2humpCase(name);
        if (StringUtils.isBlank(humpCase)) {
            return "";
        }
        String firstChar = new String(new char[] {humpCase.charAt(0)}).toLowerCase();
        String left = humpCase.substring(1);
        return firstChar + left;
    }

    /**
     * 将字符转换为指定字符连接的形式（支持分隔符：./_-）
     * abc-def -> abc#link#def（如：adc.def）
     * @param name 分隔符：./_-连接的字符串
     * @param link 指定的连接字符
     * @return 处理后的字符
     */
    public static String parse2link(String name,String link) {
        if (StringUtils.isEmpty(name)) {
            return StringUtils.EMPTY;
        }
        // 统一将字符处理为"-"，作为字符串分隔字符
        name = name.replaceAll("\\.","-");
        name = name.replaceAll("/","-");
        name = name.replaceAll("_","-");
        return name.replace("-", link).toLowerCase();
    }
}
