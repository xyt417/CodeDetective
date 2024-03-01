package com.cdd.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.cdd.pojo.User;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public Map<String, Object> listObjectsAt(String path) {
        OSS ossClient = initOssClient();
        Map<String, Object> res = new HashMap<>();
        try {
            if (!ossClient.doesObjectExist(bucketName, path + "/")) {
                res.put("message", "DirNotExists");
                return res;
            }
            // 构造ListObjectsRequest请求。
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
            // 以 path + "/" 为前缀且第一次出现delimiter字符之间的文件作为一组元素
            listObjectsRequest.setDelimiter("/");
            listObjectsRequest.setPrefix(path + "/");
            ObjectListing listing = ossClient.listObjects(listObjectsRequest);

            // 遍历所有commonPrefix。
            res.put("Directories", listing.getCommonPrefixes());
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            res.put("message", "OSSException");
            return res;
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            res.put("message", "ClientException");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        res.put("message", "success");
        return res;
    }

    public Map<String, String> getOSSPolicy(String repoName){
        OSS ossClient = initOssClient();
        User user = UserUtils.getUserFromContext();
        String username = user.getUsername();

        Map<String, String> respMap = new LinkedHashMap<>();
        try {
            long expireTime = 3600;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            String dir = codeDir + repoName + "/" + username + "/";
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            String host = "https://" + bucketName + "." + endpoint;

            respMap.put("access_id", accessKeyId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }

        return respMap;
    }

}
