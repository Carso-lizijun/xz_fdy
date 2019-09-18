package com.xz.service.com.zx.service.com.xz.service.Impl;


import com.imooc.utils.IMoocJSONResult;
import com.xz.mapper.UsersFansMapper;
import com.xz.mapper.UsersLikeVideosMapper;
import com.xz.mapper.UsersMapper;
import com.xz.mapper.UsersReportMapper;
import com.xz.service.UserService;
import com.zx.pojo.Users;
import com.zx.pojo.UsersFans;
import com.zx.pojo.UsersLikeVideos;
import com.zx.pojo.UsersReport;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @Author: huangfurong
 * @Description:
 * @Date: Create in 12:27 2019-07-03
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper userMapper;

    @Autowired
    private UsersFansMapper usersFansMapper;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersReportMapper usersReportMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameISExist(String username) {
        Users users = new Users();
        users.setUsername(username);
        Users user = userMapper.selectOne(users);

        return user == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users users) {
        String userid = sid.nextShort();
        users.setId(userid);
        userMapper.insert(users);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);
        Users result = userMapper.selectOneByExample(userExample);

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserInfo(Users users) {
        Example userexample = new Example(Users.class);
        Example.Criteria criteria = userexample.createCriteria();
        criteria.andEqualTo("id", users.getId());
        userMapper.updateByExampleSelective(users, userexample);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserInfo(String userId) {
        Example userexample = new Example(Users.class);
        Example.Criteria criteria = userexample.createCriteria();
        criteria.andEqualTo("id", userId);
        Users users = userMapper.selectOneByExample(userexample);
        return users;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserLikeVideo(String userId, String videoId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)) {
            return false;
        }

        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("videoId", videoId);
        List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);

        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void addFansCount(String userId) {

    }

    @Override
    public void addFollersCount(String userId) {

    }

    @Override
    public void reduceFansCount(String userId) {

    }

    @Override
    public void reduceFollersCount(String userId) {

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUserFanRetion(String userId, String fanId) {

        String id = sid.nextShort();

        UsersFans usersFans = new UsersFans();
        usersFans.setId(id);
        usersFans.setFanId(fanId);
        usersFans.setUserId(userId);

        usersFansMapper.insert(usersFans);
        userMapper.addFansCount(userId);
        userMapper.addFollersCount(fanId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserFanRetion(String userId, String fanId) {
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("fanId", fanId);
        usersFansMapper.deleteByExample(example);
        userMapper.reduceFansCount(userId);
        userMapper.reduceFollersCount(fanId);
    }

    @Override
    public boolean quryIsFollw(String userId, String fanId) {
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("fanId", fanId);
        List<UsersFans> list = usersFansMapper.selectByExample(example);

        if (list != null && !list.isEmpty() && list.size() > 0) {
            return true;
        }

        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reportUser(UsersReport userReport) {

        String urId = sid.nextShort();
        userReport.setId(urId);
        userReport.setCreateDate(new Date());

        usersReportMapper.insert(userReport);
    }

}
