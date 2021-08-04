package @packageName@.db.common.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import @packageName@.db.common.enums.DbColumnType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author hsf
 * @date 2018/7/4
 */
public class ResourcesCommonGenerator {

    private static Pattern pattern = Pattern.compile("[0-9]*");

    public static void generator(Map<String, String> map, String[] excludedTable) {
        String mainDir = map.get("mainDir");
        String packageDir = map.get("packageDir");
        String outputDir = mainDir + "/java";
        String entityOutputDir = mainDir + packageDir + "model/entity/";
        String mapperOutputDir = mainDir + packageDir + "mapper/";
        String enumOutputDir = mainDir + packageDir + "model/enums/";
        String xmlOutputDir = mainDir + "/resources/mapper/";
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outputDir);
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        gc.setAuthor("hsf");
        gc.setServiceName("%sService");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(map.get("userName"));
        dsc.setPassword(map.get("password"));
        dsc.setUrl(map.get("url"));
        dsc.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String type = fieldType.toLowerCase();
                if (type.contains("datetime") || type.contains("date")) {
                    return DbColumnType.DATE;
                } else if (type.contains("blob") || type.contains("text")) {
                    return DbColumnType.STRING;
                } else {
                    return super.processTypeConvert(globalConfig, fieldType);
                }
            }
        });
        mpg.setDataSource(dsc);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setExclude(excludedTable); // 排除生成的表
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(map.get("parentPackageDir"));
        pc.setEntity("model.entity");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        cfg.setFileCreate((configBuilder, fileType, filePath) -> {
            if (filePath.contains("EmptyEnum")) {
                return false;
            }
            return true;
        });
        //mapper.xml路径
        File xmlDir = new File(xmlOutputDir);
        if (!xmlDir.exists()) {
            xmlDir.mkdirs();
        }
        // 生成的entity路径，不存在时需要先新建
        File entityDir = new File(entityOutputDir);
        if (!entityDir.exists()) {
            entityDir.mkdirs();
        }

        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();

        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                int entityLength = tableInfo.getEntityName().length();
                String endStr = tableInfo.getEntityName().substring(entityLength - 1, entityLength);
                if (pattern.matcher(endStr).matches()) {
                    String secEndStr = tableInfo.getEntityName().substring(entityLength - 2, entityLength - 1);
                    if (pattern.matcher(secEndStr).matches()) {
                        tableInfo.setEntityName(tableInfo.getEntityName().substring(0, entityLength - 2));
                    } else {
                        tableInfo.setEntityName(tableInfo.getEntityName().substring(0, entityLength - 1));
                    }
                }
                return xmlOutputDir + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        focList.add(new FileOutConfig("/templates/entity.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return entityOutputDir + tableInfo.getEntityName() + ".java";
            }
        });
        focList.add(new FileOutConfig("/templates/mapper.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return mapperOutputDir + tableInfo.getEntityName() + "Mapper.java";
            }
        });
        focList.add(new FileOutConfig("/templates/enum.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                List<TableField> fieldList = tableInfo.getFields();
                Boolean enumExist = false;
                for (TableField tableField : fieldList) {
                    if (tableField.getType().equals("tinyint(2)") && !tableField.getComment().contains("CommonEnum")) {
                        enumExist = true;
                    }
                }
                if (enumExist) {
                    return enumOutputDir + tableInfo.getEntityName() + "Enum.java";
                } else {
                    return enumOutputDir + tableInfo.getEntityName() + "EmptyEnum.java";
                }
            }
        });
        // 关闭默认 xml 生成，调整生成至/resources/mapper/根目录
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        tc.setController(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        tc.setEntity(null);
        tc.setMapper(null);
        mpg.setTemplate(tc);
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.execute();
    }


}
