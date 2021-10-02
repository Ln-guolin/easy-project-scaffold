package cn.ex.project.scaffold.model;

import lombok.Data;

/**
 * 入参对象
 *
 * @author: Chen GuoLin
 * @create: 2021-06-10 16:09
 **/
@Data
public class ParamDTO {

    /**
     * 模版：bp/fmis
     */
    private String template;

    /**
     * 英文逗号分隔的域名倒序
     * pom文件中的groupId标签值
     * cn.ex.pro
     */
    private String group;
    /**
     * 制品名称，多词使用中横线链接
     * 制品或业务领域
     * biz-user
     */
    private String artifact;
    /**
     * group + 转换为英文逗号分隔的artifact
     * 包地址
     * cn.ex.pro.biz.user
     */
    private String packageName;
    /**
     * 端口
     * 8080
     */
    private Integer port;

    /**
     * 版本
     * 1.0.0-SNAPSHOT
     */
    private String version;
    /**
     * 工程描述
     * 用户服务
     */
    private String description;

}
