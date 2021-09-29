package cn.ex.project.scaffold.handler;

import cn.ex.project.scaffold.common.ApiException;
import cn.ex.project.scaffold.constant.Constant;
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
public class TemplateCreateHandler {

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

        // 根据配置策略执行，获取模版地址
        String templatePath = configModeHandlerIfcList.stream().filter(w -> w.mode())
                .findFirst()
                .orElseThrow(() -> new ApiException("配置有误！"))
                .execute(paramDTO.getTemplate());

        // 开始进行项目模版处理
        File template = new File(templatePath);
        File[] templateFiles = new File(templatePath).listFiles();
        for (File templateFile : templateFiles) {
            if(!templateFile.getName().startsWith(".")){
                template = templateFile;
                break;
            }
        }

        // 替换变量
        File file = generate(template, model);
        if (file == null) {
            throw new ApiException("构建失败！#1");
        }
        File targetFile = null;
        for (File f : file.listFiles()) {
            if (f.getName().contains(model.getArtifact())) {
                targetFile = f;
                break;
            }
        }
        if (out == null || targetFile == null) {
            throw new ApiException("构建失败！#2");
        }

        ZipUtils.zip(targetFile, out);
        FileUtils.deleteAll(targetFile);
        log.info(paramDTO.getArtifact() + "构建成功！");
    }

    /**
     * 生成文件
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
            log.debug("generateFile(template, projectModel) result newFile == null");
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

            try (BufferedReader br = new BufferedReader(new FileReader(template))) {
                try (PrintWriter pw = new PrintWriter(newFile)) {

                    String line = br.readLine();
                    while (line != null) {
                        String replacedLine = replacePlaceholder(line, projectModel);

                        if (!replacedLine.contains(Constant.TOBE_REMOVED_MARK)) {
                            pw.println(replacedLine);
                        }
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
        }
        return newFile;
    }

    /**
     * 替换占位符
     * @param source 元字符串
     * @param projectModel 项目模型
     * @return 替换后的字符串
     */
    private String replacePlaceholder(String source, ProjectModel projectModel) {
        return replacePlaceHolder(source, projectModel, ProjectModel.class);
    }

    /**
     * 替换占位符
     * @param source 元字符串
     * @param projectModel 项目模型
     * @param clazz 类
     * @return 替换后的字符串
     */
    private String replacePlaceHolder(String source, ProjectModel projectModel, Class<? extends ProjectModel> clazz) {
        String result = source;

        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(projectModel);
                if (value == null) {
                    value = "";
                }

                result = result.replace("@" + field.getName() + "@", (String) value);
                result = result.replace("{" + field.getName() + "}", (String) value);
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
        String path = replacePlaceholder(templateFile.getPath(), projectModel);
        if (path == null) {
            return null;
        }

        if (path.contains(Constant.TOBE_REMOVED_MARK)) {
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
        model.setPort(String.valueOf(paramDTO.getPort()));
        model.setVers(StringUtils.isNotBlank(paramDTO.getVersion()) ? paramDTO.getVersion() : "1.0.0.0-SNAPSHOT");
        model.setDesc(StringUtils.isNotBlank(paramDTO.getDescription()) ? paramDTO.getDescription() : paramDTO.getArtifact() + "服务");
        model.setCreateTime(DateUtils.getTime(System.currentTimeMillis()));
        model.setCreateDate(DateUtils.getTime(System.currentTimeMillis(),DateUtils.FORMAT_YYYY_MM_DD));
        model.setPort4incr(String.valueOf(paramDTO.getPort() + 1));
        model.setArtifact4comma(TemplateVariableReplaceUtils.parse2link(paramDTO.getArtifact(),"."));
        model.setArtifact4underline(TemplateVariableReplaceUtils.parse2link(paramDTO.getArtifact(),"_"));
        model.setArtifact4path(TemplateVariableReplaceUtils.parse2link(paramDTO.getArtifact(),"/"));
        model.setArtifact4hump(TemplateVariableReplaceUtils.parse2hump(paramDTO.getArtifact()));
        model.setArtifact4humpCase(TemplateVariableReplaceUtils.parse2humpCase(paramDTO.getArtifact()));
        model.setGroup4path(TemplateVariableReplaceUtils.parse2link(paramDTO.getGroup(),"/"));
        model.setPackage4path(TemplateVariableReplaceUtils.parse2link(paramDTO.getPackageName(),"/"));
        return model;
    }


}
