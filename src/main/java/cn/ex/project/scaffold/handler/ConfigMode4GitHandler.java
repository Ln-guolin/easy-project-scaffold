package cn.ex.project.scaffold.handler;

import cn.ex.project.scaffold.common.ApiException;
import cn.ex.project.scaffold.model.ConfigInfo;
import cn.ex.project.scaffold.repository.ConfigRepository;
import cn.ex.project.scaffold.util.FileUtils;
import cn.ex.project.scaffold.util.JGitUtils;
import cn.ex.project.scaffold.util.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * git 模式
 *
 * @author: Chen GuoLin
 * @create: 2021-09-26 01:56
 **/
@Slf4j
@Component
public class ConfigMode4GitHandler implements ConfigModeHandlerIfc{
    /**
     * 模式配置
     */
    @Value("${template.mode.config:}")
    private String templateMode;

    @Resource
    private ConfigRepository configRepository;

    @Override
    public boolean mode() {
        return "git".equals(templateMode);
    }

    @Override
    public String execute(String templateName) {

        // 查询git模版配置信息
        ConfigInfo configInfo = configRepository.findByName(templateName);
        if(configInfo == null){
            throw new ApiException("未找到GIT模版配置！");
        }

        // 指定克隆到本地的路径
        String localUrl = PathUtils.getProjectPath() + "template/";

        // 先清空本地模版
        FileUtils.deleteAll(new File(localUrl));

        // 执行git命令进行模版下载
        JGitUtils.gitClone(configInfo.getUrl(),configInfo.getBranch(),localUrl,configInfo.getUsername(),configInfo.getPassword());

        // 完成下载，返回本地模版地址
        return localUrl;
    }
}
