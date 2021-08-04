package cn.ex.project.scaffold.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类
 *
 * @author: Chen GuoLin
 * @create: 2021-04-24
 **/
@Slf4j
public class ZipUtil {

    /**
     * 将文件压缩后写入流
     * @param sourceFile 文件
     * @param out 流
     * @throws Exception 异常
     */
    public static void zip(File sourceFile, OutputStream out) throws Exception {
        ZipOutputStream zipOut = new ZipOutputStream(out);

        compress(sourceFile, zipOut, sourceFile.getName());

        zipOut.flush();
        zipOut.close();
    }

    /**
     * 将文件压缩
     * @param sourceFile 文件
     * @param out 流
     * @param path 路径
     * @throws Exception 异常
     */
    private static void compress(File sourceFile, ZipOutputStream out, String path) throws Exception {
        if (sourceFile.isDirectory()) {
            File[] files = sourceFile.listFiles();

            if (files == null) {
                return;
            }
            if (files.length == 0) {
                log.info("压缩目录：{}", path);
                out.putNextEntry(new ZipEntry(path + "/"));
            } else {
                for (File file : files) {
                    compress(file, out, path + "/" + file.getName());
                }
            }
        } else {
            out.putNextEntry(new ZipEntry(path));
            FileInputStream fos = new FileInputStream(sourceFile);
            try (BufferedInputStream bis = new BufferedInputStream(fos)) {

                log.debug("压缩文件：{}", path);
                int tag;
                byte[] arr = new byte[1024];
                while ((tag = bis.read(arr)) != -1) {
                    out.write(arr, 0, tag);
                }
            }
            fos.close();
        }
    }
}
