package cn.ex.project.scaffold.service;

import cn.ex.project.scaffold.constant.Constant;
import cn.ex.project.scaffold.model.ParamDTO;
import cn.ex.project.scaffold.model.ProjectModel;
import cn.ex.project.scaffold.util.DateUtils;
import cn.ex.project.scaffold.util.FileUtil;
import cn.ex.project.scaffold.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;

/**
 * 项目生成服务
 *
 * @author: Chen GuoLin
 * @create: 2021-04-24
 **/
@Service
@Slf4j
public class ProjectGenerateService {

    /**
     * 项目模板路径
     */
    @Value("${project.template.path:}")
    private String projectTemplatePath;

    /**
     * 生成压缩后的项目文件到流
     * @param paramDTO 项目模型
     * @param out 数据流
     * @throws Exception 异常
     */
    public void generate(ParamDTO paramDTO, OutputStream out) throws Exception {

        // 变量转换
        ProjectModel model = variableConvert(paramDTO);

        String templatePath = null;
        // 未配置模版地址，走项目默认模版，否则使用工程内部模版
        if (StringUtils.isNotBlank(projectTemplatePath)) {
            templatePath = projectTemplatePath + "/" + paramDTO.getTemplate();
            log.info("[模版路径]走配置指定模版：" + templatePath);
        }else{
            templatePath = ProjectGenerateService.class.getResource("/project-template/" + paramDTO.getTemplate()).getFile();
            log.info("[模版路径]走项目默认模版:" + templatePath);
        }

        File template = new File(templatePath);
        // 替换变量
        File file = generate(template, model);
        if (file == null) {
            return;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        File targetFile = null;
        for (File f : files) {
            if (f.getName().contains(model.getArtifact())) {
                targetFile = f;
                break;
            }
        }
        if (out != null && targetFile != null) {
            ZipUtil.zip(targetFile, out);
            FileUtil.deleteAll(targetFile);
        }
    }

    /**
     * 生成文件
     * @param template 模板文件
     * @param projectModel 项目模型
     * @return 文件
     * @throws IOException 异常
     */
    private File generate(File template, ProjectModel projectModel) throws IOException {
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
                }
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

                result = result.replace("<!---->", "");
            }
        } catch (Exception e) {
            log.error("替换占位符失败", e);
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
        model.setArtifact4comma(Utils.parse2link(paramDTO.getArtifact(),"."));
        model.setArtifact4underline(Utils.parse2link(paramDTO.getArtifact(),"_"));
        model.setArtifact4path(Utils.parse2link(paramDTO.getArtifact(),"/"));
        model.setArtifact4hump(Utils.parse2hump(paramDTO.getArtifact()));
        model.setArtifact4humpCase(Utils.parse2humpCase(paramDTO.getArtifact()));
        model.setGroup4path(Utils.parse2link(paramDTO.getGroup(),"/"));
        model.setPackage4path(Utils.parse2link(paramDTO.getPackageName(),"/"));
        return model;
    }

    /**
     * 变量处理工具
     */
    static class Utils{

        /**
         * 将字符转换为首字母大写的驼峰形式（支持分隔符：./_-）
         * abc-def -> AbcDef
         * @param name 分隔符：./_-连接的字符串
         * @return 首字母大写的驼峰格式字符串
         */
        public static String parse2humpCase(String name) {
            if (StringUtils.isEmpty(name)) {
                return StringUtils.EMPTY;
            }
            // 统一将字符处理为"-"，作为字符串分隔字符
            name = parse2link(name,"-");
            // 拆分字符
            String[] strArr = name.split("-");

            StringBuilder sb = new StringBuilder();
            for (String str : strArr) {
                String firstChar = new String(new char[] {str.charAt(0)}).toUpperCase();
                String left = str.substring(1);
                sb.append(firstChar).append(left);
            }
            return sb.toString();
        }

        /**
         * 将字符转换为驼峰形式（支持分隔符：./_-）
         * abc-def -> abcDef
         * @param name 分隔符：./_-连接的字符串
         * @return 驼峰格式的字符串
         */
        public static String parse2hump(String name) {
            String humpCase = parse2humpCase(name);
            if (StringUtils.isBlank(humpCase)) {
                return "";
            }
            String firstChar = new String(new char[] {humpCase.charAt(0)}).toLowerCase();
            String left = humpCase.substring(1);
            return firstChar + left;
        }

        /**
         * 将字符转换为指定字符连接的形式（支持分隔符：./_-）
         * abc-def -> abc#link#def（如：adc.def）
         * @param name 分隔符：./_-连接的字符串
         * @param link 指定的连接字符
         * @return 处理后的字符
         */
        public static String parse2link(String name,String link) {
            if (StringUtils.isEmpty(name)) {
                return StringUtils.EMPTY;
            }
            // 统一将字符处理为"-"，作为字符串分隔字符
            name = name.replaceAll("\\.","-");
            name = name.replaceAll("/","-");
            name = name.replaceAll("_","-");
            return name.replace("-", link).toLowerCase();
        }
    }

}
