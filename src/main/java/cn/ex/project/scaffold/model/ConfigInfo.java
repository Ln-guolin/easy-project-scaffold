package cn.ex.project.scaffold.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 模版配置信息
 *
 * @author: Chen GuoLin
 * @create: 2021-09-25 16:41
 **/
@Entity
@Table(name = "config_info")
@Data
public class ConfigInfo implements Serializable {

    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 模版名称
     */
    public String name;

    /**
     * 模版GIT地址，如：http://git.cn/template-test.git
     */
    private String url;

    /**
     * 分支名称
     */
    private String branch;

    /**
     * GIT用户名（可为空）
     */
    private String username;

    /**
     * GIT密码（可为空）
     */
    private String password;

    /**
     * 是否启用默认变量
     * <pre>
     *     1：启用
     *     0：禁用
     * </pre>
     */
    @Column(name = "enable_def_variable")
    private Integer enableDefVariable;

    /**
     * 自定义变量
     * <pre>
     *     格式：多个变量名使用英文逗号分隔
     *     示例：variable1,variable2
     *     注：自定义变量禁止使用通用变量
     * </pre>
     */
    @Column(name = "custom_variable")
    private String customVariable;
}
