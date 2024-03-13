package com.cdd.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdd.mapper.RepositoryMapper;
import com.cdd.pojo.User;
import com.cdd.utils.OSSUtils;
import com.cdd.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.cdd.pojo.Repository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreateRepositoryService {
    @Autowired
    private OSSUtils ossUtils;
    @Autowired
    private RepositoryMapper repositoryMapper;

    public Map<String, String> createRepository(String reponame, String description) {
        Map<String, String> res = new HashMap<>();
        if (reponame == null) {
            res.put("message", "reponame is null");
            return res;
        }
        if(description == null) {
            res.put("message", "description is null");
            return res;
        }
        if (reponame.isEmpty()) {
            res.put("message", "仓库名称不能为空");
            return res;
        }
        if (reponame.length() > 50) {
            res.put("message", "仓库名称过长");
            return res;
        }
        if(description.length() > 300) {
            res.put("message", "仓库描述过长");
            return res;
        }
        String result = ossUtils.createDir(reponame);
        if("DirAlreadyExists".equals(result)) {
            res.put("message", "仓库已存在");
            QueryWrapper<Repository> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("repo_name", reponame);
            Repository repo = repositoryMapper.selectOne(queryWrapper);
            if (repo == null) {
                throw new RuntimeException("创建仓库时，数据库与 OSS 不一致");
            }
            return res;
        }
        if (!"success".equals(result)) {
            res.put("message", "创建仓库异常");
            return res;
        }

        // 已在 OSS 创建仓库成功
        QueryWrapper<Repository> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("repo_name", reponame);
        Repository repo = repositoryMapper.selectOne(queryWrapper);
        if (repo != null) {
            throw new RuntimeException("创建仓库时，数据库与 OSS 不一致");
        }

        User user = UserUtils.getUserFromContext();
        int userId = user.getId();
        String username = user.getUsername();
        repo = new Repository(null, userId, username, reponame, description, new Date(), null);
        repositoryMapper.insert(repo);
        res.put("message", "success");
        return res;
    }
}
