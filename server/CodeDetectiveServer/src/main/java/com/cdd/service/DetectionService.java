package com.cdd.service;

import com.cdd.utils.FileUtils;
import com.cdd.utils.JplagRunner;
import com.cdd.utils.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class DetectionService {
    @Autowired
    private OSSUtils ossUtils;
    @Autowired
    private JplagRunner jplagRunner;
    public void detection(String repoName) throws IOException {
        // 下载
        String downloadDirPath = Objects.requireNonNull(getClass().getClassLoader().getResource("download")).getPath();
        String submissionsDirPath = Objects.requireNonNull(getClass().getClassLoader().getResource("submissions")).getPath();
        String outputDirPath = Objects.requireNonNull(getClass().getClassLoader().getResource("output")).getPath();
        FileUtils.deleteFilesInDir(downloadDirPath);
        ossUtils.downloadRepo(ossUtils.getCodeDir() + repoName); // 下载代码库内检测项
        FileUtils.deleteFilesInDir(submissionsDirPath);
        FileUtils.unzipAllZipFiles(downloadDirPath, submissionsDirPath); // 解压

        // 检测
        FileUtils.deleteFilesInDir(outputDirPath);
        jplagRunner.runJPlag(submissionsDirPath, Objects.requireNonNull(getClass().getClassLoader().getResource("output")).getPath() + "/result", "", "");
        FileUtils.unzip(Objects.requireNonNull(getClass().getClassLoader().getResource("output")).getPath() + "/result.zip", Objects.requireNonNull(getClass().getClassLoader().getResource("output")).getPath());

        // 上传
        String resultDir = Objects.requireNonNull(getClass().getClassLoader().getResource("output")).getPath() + "/result";
        String uploadDir = "Results/" + repoName + "/";
        ossUtils.uploadResult(resultDir, uploadDir);
    }
}
