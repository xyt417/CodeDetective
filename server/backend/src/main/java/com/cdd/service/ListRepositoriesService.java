package com.cdd.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdd.mapper.RepositoryMapper;
import com.cdd.pojo.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListRepositoriesService {
    @Autowired
    private RepositoryMapper repositoryMapper;

    public List<Repository> listRepositories(String username) {
        QueryWrapper<Repository> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_name", username);
        if("all".equals(username)) {
            return repositoryMapper.selectList(null);
        }
        // username 未找到或者没有仓库都返回空列表
        return repositoryMapper.selectList(queryWrapper);
    }
}

