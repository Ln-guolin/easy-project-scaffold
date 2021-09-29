package cn.ex.project.scaffold.controller;

import cn.ex.project.scaffold.common.ApiException;
import cn.ex.project.scaffold.common.R;
import cn.ex.project.scaffold.handler.TemplateCreateHandler;
import cn.ex.project.scaffold.model.ConfigInfo;
import cn.ex.project.scaffold.model.ParamDTO;
import cn.ex.project.scaffold.repository.ConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * 项目生成控制器
 *
 * @author: Chen GuoLin
 * @create: 2021-04-24
 **/
@RestController
@Slf4j
public class ProjectGenerateController {

    @Resource
    private TemplateCreateHandler templateCreateHandler;

    @Resource
    private ConfigRepository configRepository;

    @Value("${template.mode.config:}")
    private String templateMode;
    @Value("${template.resources.path.list:}")
    private String templatePathList;

    /**
     * 工程构建
     * @param paramDTO
     * @param response
     * @throws Exception
     */
    @PostMapping("/generate")
    public void generateServiceProject(ParamDTO paramDTO, HttpServletResponse response) throws Exception {
        if(StringUtils.isAnyBlank(paramDTO.getTemplate(),paramDTO.getGroup(),paramDTO.getArtifact(),paramDTO.getPackageName())){
            throw new ApiException("必填参数不能为空！");
        }

        if(paramDTO.getPort() == null){
            throw new ApiException("必填参数不能为空！");
        }

        if(!paramDTO.getPackageName().startsWith(paramDTO.getGroup())){
            throw new ApiException("packageName必须是由" + paramDTO.getGroup() + "开头");
        }

        response.setContentType("application/x-zip-compressed");
        response.setHeader("Content-Disposition", "attachment;fileName=" + paramDTO.getArtifact() + ".zip");

        try {
            templateCreateHandler.generate(paramDTO, response.getOutputStream());
        } catch (Exception e) {
            log.error("发生异常!",e);
            throw new ApiException("发生异常");
        }
    }

    @GetMapping("/query/templates")
    public R getConfTemplates() {
        // resources模式
        if("resources".equals(templateMode)){
            if(StringUtils.isBlank(templatePathList)){
                return R.success();
            }
            return R.success(templatePathList.split(","));
        }

        // git模式
        List<ConfigInfo> configInfos = configRepository.findAll();
        List<String> names = new ArrayList<>();
        for(ConfigInfo config : configInfos){
            names.add(config.getName());
        }
        return R.success(names);
    }
}
