package com.cdd.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cdd.mapper.RepositoryMapper;
import com.cdd.pojo.Repository;
import com.cdd.utils.FileUtils;
import com.cdd.utils.JplagRunner;
import com.cdd.utils.OSSUtils;
import com.cdd.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
public class DetectionService {
    @Autowired
    private OSSUtils ossUtils;
    @Autowired
    private JplagRunner jplagRunner;
    @Autowired
    private RestUtils restUtils;
    @Autowired
    private RepositoryMapper repositoryMapper;

    public void detection(String repoName, int userId) throws IOException {
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

        // 更新仓库状态
        Date detectionDate = new Date();
        UpdateWrapper<Repository> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("repo_name", repoName).set("detection_time", detectionDate);
        repositoryMapper.update(updateWrapper);

        // 告知后端业务服务器
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("result", "success");
        multiValueMap.add("user_id", String.valueOf(userId));
        multiValueMap.add("detection_time", String.valueOf(detectionDate));
        restUtils.postStrs(RestUtils.BackendGetResultUrl, multiValueMap);
    }
}
