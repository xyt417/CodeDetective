package com.cdd.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@Component
@ConfigurationProperties(prefix="oss")
public class OSSUtils {
    private String endpoint;
    private String region;
    private String accessUsername;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String codeDir;

    public void printConfig() {
        System.out.println("endpoint: " + endpoint);
        System.out.println("region: " + region);
        System.out.println("accessUsername: " + accessUsername);
        System.out.println("accessKeyId: " + accessKeyId);
        System.out.println("accessKeySecret: " + accessKeySecret);
        System.out.println("bucketName: " + bucketName);
        System.out.println("codeDir: " + codeDir);
    }

    public OSS initOssClient() {
        // 使用代码嵌入的RAM用户的访问密钥配置访问凭证。
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);
        // 创建OSSClient实例。
        return new OSSClientBuilder().build(endpoint, credentialsProvider);
    }

    public String createDir(String dirName) {
        OSS ossClient = initOssClient();
        try {
            dirName = codeDir + dirName + "/";
            if (ossClient.doesObjectExist(bucketName, dirName)) {
                return "DirAlreadyExists";
            }
            String content = "";
            // 创建PutObjectRequest对象。
            // dirName 需以正斜线结尾
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, dirName, new ByteArrayInputStream(content.getBytes()));
            // 上传字符串。
            ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return "OSSException";
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            return "ClientException";
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return "success";
    }

    public List<String> listObjectsAt(String path, boolean findDir) {
        OSS ossClient = initOssClient();
        List<String> objects = new LinkedList<>();
        try {
            // 构造ListObjectsRequest请求。
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
            listObjectsRequest.setPrefix(path + "/");
            if(findDir)
                // 以 path + "/" 为前缀且第一次出现delimiter字符之间的文件作为一组元素
                listObjectsRequest.setDelimiter("/");
            ObjectListing listing = ossClient.listObjects(listObjectsRequest);

            if(findDir) {
                return listing.getCommonPrefixes();
            } else {
                for(OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
                    String filePath = objectSummary.getKey();
                    if(filePath.endsWith("/")) continue; // skip directory name
                    Date lastModified = objectSummary.getLastModified();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    objects.add(filePath + "[" + formatter.format(lastModified) + "]");
                }
                // return listing.getObjectSummaries().stream().map(OSSObjectSummary::getKey).toList();
                return objects;
            }
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return new LinkedList<>();
    }

    public void downloadRepo(String dirPath) {
        OSS ossClient = initOssClient();
        try {
            // 构造ListObjectsRequest请求。
            String downloadDirPath = Objects.requireNonNull(getClass().getClassLoader().getResource("download")).getPath();
            System.out.println(downloadDirPath);
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
            listObjectsRequest.setPrefix(dirPath + "/");
            ObjectListing listing = ossClient.listObjects(listObjectsRequest);
            for(OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
                String filePath = objectSummary.getKey();
                if(filePath.endsWith("/")) continue; // skip directory name
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf(".zip")) +
                        "[" + filePath.substring(filePath.substring(0, filePath.lastIndexOf("/")).lastIndexOf("/") + 1, filePath.lastIndexOf("/")) + "]" +
                        ".zip";
                // 下载文件。
                ossClient.getObject(new GetObjectRequest(bucketName, filePath), new java.io.File(downloadDirPath + "/" + fileName));
                System.out.println("download file: " + downloadDirPath + "/" + fileName);
            }
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public void uploadResult(String resultDir, String uploadDir) {
        OSS ossClient = initOssClient();
        try {
            Path resultDirPath = Paths.get(resultDir);
            Files.walk(resultDirPath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        String key = uploadDir + resultDirPath.relativize(file).toString().replace(File.separatorChar, '/');
                        ossClient.putObject(bucketName, key, file.toFile());
                        System.out.println(file + " 已上传至 " + key);
                    });
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

}
