package cn.ex.project.scaffold.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

/**
 * 文件工具类
 *
 * @author: Chen GuoLin
 * @create: 2021-04-24
 **/
@Slf4j
public class FileUtils {

    /**
     * 删除所有目录，包含子目录和文件
     * @param dir 主目录
     */
    public static void deleteAll(File dir) {
        if (dir == null) {
            return;
        }
        if (dir.isDirectory()) {
            if (dir.listFiles() != null) {
                for (File file : dir.listFiles()) {
                    deleteAll(file);
                }
            }
        }
        boolean deleted = dir.delete();
        log.debug("{}删除{}", dir.getPath(), deleted ? "成功" : "失败");
    }
}
