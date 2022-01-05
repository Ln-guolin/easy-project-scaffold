package cn.ex.project.scaffold.controller;

import cn.ex.project.scaffold.common.ApiException;
import cn.ex.project.scaffold.common.R;
import cn.ex.project.scaffold.handler.Template4CustomCreateHandler;
import cn.ex.project.scaffold.model.ConfigInfo;
import cn.ex.project.scaffold.model.ParamDTO;
import cn.ex.project.scaffold.model.ProjectModel;
import cn.ex.project.scaffold.repository.ConfigRepository;
import cn.ex.project.scaffold.util.KeyMapUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.*;


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
    private Template4CustomCreateHandler template4CustomCreateHandler;

    @Resource
    private ConfigRepository configRepository;

    @Value("${template.mode.config:}")
    private String templateMode;
    @Value("${template.folder:}")
    private String templateFolder;

    @GetMapping("/query/templateMode")
    public R getConfTemplate() {
        log.info("[api]接口：/query/templateMode，templateMode=" + templateMode);
        return R.success(templateMode);
    }

    /**
     * 工程构建 - 自定义变量
     * @param response
     * @throws Exception
     */
    @PostMapping(value = "/generate4custom")
    public void generate4custom(HttpServletResponse response,@RequestParam HashMap<String, String> paramMap) {
        log.info("[api]接口：/generate4custom，paramMap=" + paramMap);

        // 验证paramMap参数合法性
        checkParamMap(paramMap);

        // 设置默认变量
        Gson gson = new Gson();
        ParamDTO paramDTO = gson.fromJson(gson.toJson(paramMap),ParamDTO.class);
        // 设置自定义变量
        for (Field field : ProjectModel.class.getDeclaredFields()) {
            field.setAccessible(true);
            paramMap.remove(field.getName());
        }
        paramDTO.setCustomMap(paramMap);

        // 文件response处理
        String fileName = StringUtils.isNotBlank(paramDTO.getArtifact()) ? paramDTO.getArtifact() : UUID.randomUUID().toString();
        response.setContentType("application/x-zip-compressed");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + ".zip");

        try {
            template4CustomCreateHandler.generate(paramDTO, response.getOutputStream());
        } catch (ApiException e){
            throw e;
        } catch (Exception e) {
            log.error("发生异常!",e);
            throw new ApiException("发生异常");
        }
    }

    /**
     * 验证paramMap参数合法性
     * @param paramMap
     */
    private void checkParamMap(HashMap<String, String> paramMap) {

        // 移除空数据
        Iterator<Map.Entry<String, String>> it = paramMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String> entry = it.next();
            if(StringUtils.isBlank(entry.getValue())){
                it.remove();
            }
        }

        if(paramMap == null || paramMap.size() <= 1){
            throw new ApiException("参数为空");
        }
    }

    @GetMapping("/query/templateNames")
    public R getConfTemplates() {
        log.info("[api]接口：/query/templateNames，templateMode=" + templateMode);
        // resources 或 local模式
        if("resources".equals(templateMode) || "local".equals(templateMode)){
            if(StringUtils.isBlank(templateFolder)){
                return R.success();
            }
            return R.success(templateFolder.split(","));
        }

        // git模式
        List<ConfigInfo> configInfos = configRepository.findAll();
        List<String> names = new ArrayList<>();
        for(ConfigInfo config : configInfos){
            names.add(config.getName());
        }
        return R.success(names);
    }

    @PostMapping("/query/template")
    public R getConfTemplate(@RequestBody ParamDTO paramDTO) {
        // git模式
        ConfigInfo configInfo = configRepository.findByName(paramDTO.getTemplate());
        log.info("[api]接口：/query/template，template="+paramDTO.getTemplate()+",configInfo=" + configInfo);
        return R.success(configInfo);
    }
}
