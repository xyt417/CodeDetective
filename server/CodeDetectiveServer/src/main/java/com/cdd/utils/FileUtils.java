package com.cdd.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUtils {
    public static void unzip(String zipFilePath, String destDir) throws ZipException {
        // 获取压缩文件名（不包含扩展名）
        String fileNameWithoutExtension = new File(zipFilePath).getName().replaceFirst("[.][^.]+$", "");
        // 创建目标路径，包含了以压缩文件名命名的子目录
        String targetDir = Paths.get(destDir, fileNameWithoutExtension).toString();

        // 确保目标目录存在
        new File(targetDir).mkdirs();

        // 解压到创建的目标目录
        new ZipFile(zipFilePath).extractAll(targetDir);
    }

    public static void unzipAllZipFiles(String sourceDir, String destDir) throws IOException {
        Files.walk(Paths.get(sourceDir))
                .filter(path -> path.toString().endsWith(".zip"))
                .forEach(path -> {
                    try {
                        unzip(path.toString(), destDir);
                    } catch (ZipException e) {
                        System.out.println("Error unzipping file: " + path.toString());
                    }
                });
    }

    public static void deleteFilesInDir(String dirPath) throws IOException {
        Path directory = Paths.get(dirPath);

        // 使用Files.walk遍历目录，但不包括顶级目录本身
        Files.walk(directory, 1) // 1表示只访问直接子文件和子目录
                .filter(path -> !path.equals(directory)) // 排除顶级目录本身
                .forEach(path -> {
                    try {
                        // 如果是目录，则递归删除里面的内容
                        if (Files.isDirectory(path)) {
                            deleteFilesInDir(path.toString());
                            Files.delete(path); // 删除空目录
                        } else {
                            Files.delete(path); // 删除文件
                        }
                        System.out.println("Deleted: " + path);
                    } catch (IOException e) {
                        System.err.println("Exception when deleting: " + path);
                        e.printStackTrace();
                    }
                });
    }
}
