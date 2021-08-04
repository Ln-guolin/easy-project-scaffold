package @packageName@.db.util;

import io.bigplayers.tangerine.mybatisplus.generator.DataSource;
import io.bigplayers.tangerine.mybatisplus.generator.MybatisGeneratorResource;

import java.io.File;

/**
 * @author hsf
 * @date 2018/7/3
 */
public class ResourcesGenerator {


    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        System.out.println(userDir);
        String serviceName = "pay";
        String basePathName = "fmis-pay";
        String parentPackage = "group.bigplayers.fmis";
        DataSource dataSource = DataSource.builder()
                .databasesName("dev")
                .url("jdbc:mysql://dev-fmis-pay-nrk2jo.mysql.svc.square.bigplayers.io/fmis_pay?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false")
                .username("root")
                .password("Zyq1234560")
                .build();

        String baseDir = userDir;// + File.separator + "pay";
        MybatisGeneratorResource resource1 =
                new MybatisGeneratorResource.Builder()
                        .setBaseDir(baseDir)
                        .setBasePathName(basePathName)
                        .setServiceName(serviceName)
                        .setParentPackage(parentPackage)
                        .setDatasource(dataSource)
                        .setFileOverride(true)
                        .build();
//        String[] excludedTable = new String[]{"nextserialnr"};
        resource1.generate(null, null);

    }
}
