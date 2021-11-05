package cn.ex.project.scaffold.model;

import lombok.Data;

import java.util.Map;

/**
 * 项目模型
 * <pre>
 *     输入示例：
 *     group：cn.ex.pro
 *     artifact：biz-user
 *     packageName：cn.ex.pro.biz.user
 *     port：8080
 *     version：1.0.0-SNAPSHOT
 *     description：用户服务
 * </pre>
 *
 * @author: Chen GuoLin
 * @create: 2021-04-24
 **/
@Data
public class ProjectModel {

    /**
     * 来源变量：group
     * 输出：cn.ex.pro
     */
    private String group;
    /**
     * 来源变量：artifact
     * 输出：biz-user
     */
    private String artifact;
    /**
     * 来源变量：packageName
     * 输出：cn.ex.pro.biz.user
     */
    private String packageName;
    /**
     * 来源变量：port
     * 输出：8080
     */
    private String port;
    /**
     * 来源变量：version
     * 输出：1.0.0-SNAPSHOT
     */
    private String vers;
    /**
     * 来源变量：description
     * 输出：用户服务
     */
    private String desc;
    /**
     * 来源变量：now()
     * 输出：2021-12-12 11:22:33
     */
    private String createTime;
    /**
     * 来源变量：now()
     * 输出：2021-12-12
     */
    private String createDate;
    /**
     * 来源变量：port
     * 输出：8081 port值+1
     */
    private String port4incr;
    /**
     * 来源变量：artifact
     * 输出：biz.user	将artifact转换为"."号分隔
     */
    private String artifact4comma;
    /**
     * 来源变量：artifact
     * 输出：biz_user	将artifact转换为"_"号分隔
     */
    private String artifact4underline;
    /**
     * 来源变量：artifact
     * 输出：biz/user	将artifact转换为"/"号分隔
     */
    private String artifact4path;
    /**
     * 来源变量：artifact
     * 输出：bizUser	   将artifact转换为驼峰形式
     */
    private String artifact4hump;
    /**
     * 来源变量：artifact
     * 输出：BizUser	将artifact转换为首字母大写的驼峰形式
     */
    private String artifact4humpCase;
    /**
     * 来源变量：group
     * 输出：cn/ex/pro	将group转换为"/"号分隔
     */
    private String group4path;
    /**
     * 来源变量：packageName
     * 输出：cn/ex/pro/biz/user	将packageName转换为"/"号分隔
     */
    private String package4path;

    /**
     * 自定义变量
     */
    private Map<String,String> customMap;
}
