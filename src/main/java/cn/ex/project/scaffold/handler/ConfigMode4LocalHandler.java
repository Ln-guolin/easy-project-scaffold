package cn.ex.project.scaffold.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * resources 模式
 *
 * @author: Chen GuoLin
 * @create: 2021-09-26 01:56
 **/
@Slf4j
@Component
public class ConfigMode4LocalHandler implements ConfigModeHandlerIfc{
    /**
     * 模式配置
     */
    @Value("${template.mode.config:}")
    private String templateMode;
    /**
     * 本地磁盘地址
     */
    @Value("${template.local.path:}")
    private String templateLocalPath;

    @Override
    public boolean mode() {
        return "local".equals(templateMode);
    }

    @Override
    public String execute(String templateName) {
        // 直接获取本地磁盘地址
        return templateLocalPath + "/" + templateName;
    }
}
