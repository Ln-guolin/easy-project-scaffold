package cn.ex.project.scaffold.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
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
}
