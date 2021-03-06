package cn.ex.project.scaffold.handler;

import cn.ex.project.scaffold.common.ApiException;
import cn.ex.project.scaffold.common.CommonConstant;
import cn.ex.project.scaffold.model.ConfigInfo;
import cn.ex.project.scaffold.repository.ConfigRepository;
import cn.ex.project.scaffold.util.FileUtils;
import cn.ex.project.scaffold.util.JGitUtils;
import cn.ex.project.scaffold.util.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        String localUrl = PathUtils.getProjectPath() + CommonConstant.PROJECT_INNER_PATH_TEMPLATE;

        // 执行git命令进行模版下载
        if(StringUtils.isAnyBlank(configInfo.getUsername(),configInfo.getPassword())){
            // 无需认证下载
            boolean res = JGitUtils.gitClone(configInfo.getUrl(),configInfo.getBranch(),localUrl);
            if(!res){
                throw new ApiException("代码下载失败-无需认证下载模式");
            }
        }
        else {
            // 需要认证账户密码下载
            boolean res = JGitUtils.gitClone(configInfo.getUrl(),configInfo.getBranch(),localUrl,configInfo.getUsername(),configInfo.getPassword());
            if(!res){
                throw new ApiException("代码下载失败-需要认证账户密码下载模式");
            }
        }

        // 完成下载，返回本地模版地址
        return localUrl;
    }

    @Override
    public void after() {
        // 删除模版目录和文件
        String filePath4template = PathUtils.getProjectPath() + CommonConstant.PROJECT_INNER_PATH_TEMPLATE;
        FileUtils.deleteAll(new File(filePath4template));
        // 删除产品目录和文件
        String filePath4product = PathUtils.getProjectPath() + CommonConstant.PROJECT_INNER_PATH_PRODUCT;
        FileUtils.deleteAll(new File(filePath4product));
    }
}
