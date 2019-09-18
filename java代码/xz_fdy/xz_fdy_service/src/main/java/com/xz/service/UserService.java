package com.xz.service;

import com.zx.pojo.Users;
import com.zx.pojo.UsersReport;
import org.springframework.stereotype.Service;

/**
 * @Author: huangfurong
 * @Description:
 * @Date: Create in 12:23 2019-07-03
 */

public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean queryUsernameISExist(String username);

    /**
     * 对注册用户进行保存
     * @param users
     */
    public void saveUser(Users users);

    /**
     * @Description: 用户登录，根据用户名和密码查询用户
     */
    public Users queryUserForLogin(String username, String password);

    /**
     * 修改用户信息方法
     * @param users
     */
    public void updateUserInfo(Users users);

    /**
     * 查找用户信息方法
     * @param userId
     */
    public Users queryUserInfo(String userId);

    /**
     * 查询用户是否喜欢视频
     * @param userId
     */
    public boolean isUserLikeVideo(String userId,String videoId);

    /**
     * 增加粉丝的数量
     * @param userId
     */
    public void addFansCount(String userId);

    /**
     * 增加关注的数量
     * @param userId
     */
    public void addFollersCount(String userId);


    /**
     * 减少粉丝的数量
     * @param userId
     */
    public void reduceFansCount(String userId);

    /**
     * 减少关注的数量
     * @param userId
     */
    public void reduceFollersCount(String userId);

    /**
     * 保存用户和粉丝的关系
     * @param userId
     */
    public void saveUserFanRetion(String userId,String fanId);

    /**
     * 删除用户和粉丝的关系
     * @param userId
     */
    public void deleteUserFanRetion(String userId,String fanId);

    /**
     * 查询用户和粉丝的关注关系
     */
    public boolean quryIsFollw(String userId,String fanId);


    /**
     * 举报用户的接口
     * @param usersReport
     */
    public void reportUser(UsersReport usersReport);
}
