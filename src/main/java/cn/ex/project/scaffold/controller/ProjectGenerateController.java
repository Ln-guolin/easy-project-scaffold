package cn.ex.project.scaffold.controller;

import cn.ex.project.scaffold.common.ApiException;
import cn.ex.project.scaffold.model.ParamDTO;
import cn.ex.project.scaffold.common.R;
import cn.ex.project.scaffold.service.ProjectGenerateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


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
    private ProjectGenerateService projectGenerateService;

    @Value("${template.path.list:}")
    private String templatePathList;
    /**
     * 工程构建
     * @param paramDTO
     * @param response
     * @throws Exception
     */
    @PostMapping("/generate")
    public void generateServiceProject(ParamDTO paramDTO, HttpServletResponse response) throws Exception {
        if (true) throw new ApiException("必填参数不能为空！");
        if(StringUtils.isAnyBlank(paramDTO.getTemplate(),paramDTO.getGroup(),paramDTO.getArtifact(),paramDTO.getPackageName())){
            throw new ApiException("必填参数不能为空！");
        }

        if(paramDTO.getPort() == null){
            throw new ApiException("必填参数不能为空！");
        }

        if(!paramDTO.getPackageName().startsWith(paramDTO.getGroup())){
            throw new ApiException("packageName必须是由group开头");
        }

        response.setContentType("application/x-zip-compressed");
        response.setHeader("Content-Disposition", "attachment;fileName=" + paramDTO.getArtifact() + ".zip");

        projectGenerateService.generate(paramDTO, response.getOutputStream());
    }

    @GetMapping("/query/templates")
    public R getConfTemplates() {
        if(StringUtils.isBlank(templatePathList)){
            return R.success();
        }
        return R.success(templatePathList.split(","));
    }

    /**
     * 渲染错误信息
     * @param errMsg 错误信息
     * @param response 响应
     */
    private void renderErrorResponse(String errMsg, HttpServletResponse response) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(errMsg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
