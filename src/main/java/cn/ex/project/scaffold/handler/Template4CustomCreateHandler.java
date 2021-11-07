package cn.ex.project.scaffold.handler;

import cn.ex.project.scaffold.common.ApiException;
import cn.ex.project.scaffold.common.CommonConstant;
import cn.ex.project.scaffold.model.ParamDTO;
import cn.ex.project.scaffold.model.ProjectModel;
import cn.ex.project.scaffold.util.DateUtils;
import cn.ex.project.scaffold.util.FileUtils;
import cn.ex.project.scaffold.util.TemplateVariableReplaceUtils;
import cn.ex.project.scaffold.util.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 模版生成工具
 *
 * @author: Chen GuoLin
 * @create: 2021-09-26 02:11
 **/
@Slf4j
@Component
public class Template4CustomCreateHandler {

    @Autowired
    private List<ConfigModeHandlerIfc> configModeHandlerIfcList;

    /**
     * 生成压缩后的项目文件到流
     * @param paramDTO 项目模型
     * @param out 数据流
     * @throws Exception 异常
     */
    public void generate(ParamDTO paramDTO, OutputStream out) throws Exception {

        // 变量验证和转换
        ProjectModel model = variableConvert(paramDTO);

        // 根据模版类型获取代码构建策略
        ConfigModeHandlerIfc ifc = configModeHandlerIfcList.stream().filter(w -> w.mode())
                .findFirst()
                .orElseThrow(() -> new ApiException("配置有误！"));

        // 根据配置策略执行，并获取模版地址
        String templatePath = ifc.execute(paramDTO.getTemplate());

        // 根据模版构建项目
        File targetFile = generate(new File(templatePath), model);
        if (out == null || targetFile == null) {
            throw new ApiException("构建失败！");
        }

        // 将文件压缩后写入流
        ZipUtils.zip(targetFile, out);

        // 删除构建的所有目录，包含子目录和文件
       // FileUtils.deleteAll(targetFile);

        // 执行策略后续处理方法
        ifc.after();
        log.info(paramDTO.getArtifact() + "构建成功！");
    }

    /**
     * 根据模版构建项目
     * @param template 模板文件
     * @param projectModel 项目模型
     * @return 文件
     * @throws IOException 异常
     */
    private File generate(File template, ProjectModel projectModel) {
        if (template == null) {
            return null;
        }

        File newFile = generateFile(template, projectModel);
        if (newFile == null) {
            return null;
        }

        if (template.isDirectory()) {
            File[] templateSubFiles = template.listFiles();
            if (templateSubFiles == null || templateSubFiles.length == 0) {
                return null;
            }

            for (File templateSubFile : templateSubFiles) {
                generate(templateSubFile, projectModel);
            }
        } else {
            if(!template.getName().startsWith(".")){
                try (BufferedReader br = new BufferedReader(new FileReader(template))) {
                    try (PrintWriter pw = new PrintWriter(newFile)) {
                        String line = br.readLine();
                        while (line != null) {
                            String replacedLine = replacePlaceHolder(line, projectModel);
                            pw.println(replacedLine);
                            line = br.readLine();
                        }
                        br.close();
                        pw.flush();
                    }catch (Exception e){
                        log.error("发生异常!#1",e);
                        throw new ApiException("发生异常!#1");
                    }
                }catch (Exception e){
                    log.error("发生异常!#2",e);
                    throw new ApiException("发生异常!#2");
                }
            }else{
                log.info("[filter]跳过文件：" + template.getName());
            }
        }
        return newFile;
    }

    /**
     * 替换占位符
     * @param source 元字符串
     * @param projectModel 项目模型
     * @return 替换后的字符串
     */
    private String replacePlaceHolder(String source, ProjectModel projectModel) {
        String result = source;
        result = result.replace(CommonConstant.PROJECT_INNER_GIT_TEMPLATE, CommonConstant.PROJECT_INNER_GIT_PRODUCT);
        result = result.replace(CommonConstant.PROJECT_INNER_LOCAL_TEMPLATE_PATH, CommonConstant.PROJECT_INNER_LOCAL_PRODUCT_PATH);
        try {
            // 默认变量
            for (Field field : projectModel.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(CommonConstant.PROJECT_TEMPLATE_CUSTOM_MAP.equals(field.getName())){
                    continue;
                }
                Object value = field.get(projectModel);
                if (value == null) {
                    value = "";
                }
                result = result.replace("{" + field.getName() + "}", (String) value);
                result = result.replace("@" + field.getName() + "@", (String) value);
            }

            // 自定义变量
            if(projectModel.getCustomMap() != null && projectModel.getCustomMap().size() > 0){
                for(String variable : projectModel.getCustomMap().keySet()){
                    result = result.replace("{" + variable + "}",projectModel.getCustomMap().get(variable));
                    result = result.replace("@" + variable + "@",projectModel.getCustomMap().get(variable));
                }
            }
        } catch (Exception e) {
            log.error("替换占位符失败", e);
            throw new ApiException("替换占位符失败!");
        }

        return result;
    }

    /**
     * 生成文件或目录
     * @param templateFile 模板文件
     * @param projectModel 项目模型
     * @return 新文件
     */
    private File generateFile(File templateFile, ProjectModel projectModel) {
        String path = replacePlaceHolder(templateFile.getPath(), projectModel);
        if (path == null) {
            return null;
        }

        File newFile = new File(path);
        if (templateFile.isDirectory()) {
            boolean created = newFile.mkdirs();
            log.debug("{}创建{}", path, created ? "成功" : "失败");
        }
        return newFile;
    }

    /**
     * 变量转换
     * @param paramDTO
     * @return
     */
    private ProjectModel variableConvert(ParamDTO paramDTO){
        ProjectModel model = new ProjectModel();
        model.setGroup(paramDTO.getGroup());
        model.setArtifact(paramDTO.getArtifact());
        model.setPackageName(paramDTO.getPackageName());
        model.setPort(parse4port(paramDTO,false));
        model.setVers(StringUtils.isNotBlank(paramDTO.getVersion()) ? paramDTO.getVersion() : "1.0.0.0-SNAPSHOT");
        model.setDesc(parse4desc(paramDTO));
        model.setCreateTime(DateUtils.getTime(System.currentTimeMillis()));
        model.setCreateDate(DateUtils.getTime(System.currentTimeMillis(),DateUtils.FORMAT_YYYY_MM_DD));
        model.setPort4incr(parse4port(paramDTO,true));
        model.setArtifact4comma(TemplateVariableReplaceUtils.parse2link(paramDTO.getArtifact(),"."));
        model.setArtifact4underline(TemplateVariableReplaceUtils.parse2link(paramDTO.getArtifact(),"_"));
        model.setArtifact4path(TemplateVariableReplaceUtils.parse2link(paramDTO.getArtifact(),"/"));
        model.setArtifact4hump(TemplateVariableReplaceUtils.parse2hump(paramDTO.getArtifact()));
        model.setArtifact4humpCase(TemplateVariableReplaceUtils.parse2humpCase(paramDTO.getArtifact()));
        model.setGroup4path(TemplateVariableReplaceUtils.parse2link(paramDTO.getGroup(),"/"));
        model.setPackage4path(TemplateVariableReplaceUtils.parse2link(paramDTO.getPackageName(),"/"));
        model.setCustomMap(paramDTO.getCustomMap());
        return model;
    }


    private String parse4desc(ParamDTO paramDTO) {
        if (StringUtils.isNotBlank(paramDTO.getDescription()) ) {
            return paramDTO.getDescription();
        }
        if (StringUtils.isNotBlank(paramDTO.getArtifact()) ) {
            return paramDTO.getArtifact() + "服务";
        }
        return "";
    }

    private String parse4port(ParamDTO paramDTO,boolean incr) {
        if (paramDTO.getPort() == null) {
            return "";
        }
        if (incr) {
            return (paramDTO.getPort() + 1) + "";
        }
        return paramDTO.getPort() + "";
    }
}
