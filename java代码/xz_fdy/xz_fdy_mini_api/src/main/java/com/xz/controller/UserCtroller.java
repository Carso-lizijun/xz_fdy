package com.xz.controller;

import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MD5Utils;
import com.xz.service.UserService;
import com.zx.pojo.Users;
import com.zx.pojo.UsersReport;
import com.zx.pojo.vo.PublisherVideo;
import com.zx.pojo.vo.UsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Author: huangfurong
 * @Description:
 * @Date: Create in 11:58 2019-07-03
 */
@RestController
@Api(value = "用户相关业务的接口", tags = {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserCtroller extends BasicController {

    @Autowired
    protected UserService userService;

    @ApiOperation(value = "用户上传头像", notes = "用户上传头像的接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
    @PostMapping("/uploadFace")
    public IMoocJSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id不能为空。。");
        }
        //文件保存的命名空间
        String filespace = "F:/xz_fdy_db";
        //保存到数据库中的相对路径
        String uploadPatnDB = "/" + userId + "/face";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if (files.length != 0 || files != null) {
                String filename = files[0].getOriginalFilename();
                if (!StringUtils.isBlank(filename)) {
                    //文件上传的最终保存路径
                    String finalPath = filespace + uploadPatnDB + "/" + filename;
                    //设置数据库保存的路径
                    uploadPatnDB += ("/" + filename);
                    File outfile = new File(finalPath);
                    if (outfile.getParentFile() != null || !outfile.isDirectory()) {
                        //创建父文件夹
                        outfile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(outfile);
                    inputStream = files[0].getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            } else {
                return IMoocJSONResult.errorMsg("上传出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        Users users = new Users();
        users.setId(userId);
        users.setFaceImage(uploadPatnDB);
        userService.updateUserInfo(users);
        return IMoocJSONResult.ok(uploadPatnDB);

    }

    @ApiOperation(value = "查询用户信息", notes = "查询用户信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "fanId", value = "粉丝id", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/query")
    public IMoocJSONResult query(String userId,String fanId) {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id不能为空。。");
        }
        Users usersInfo = userService.queryUserInfo(userId);
        boolean isFollw = userService.quryIsFollw(userId, fanId);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(usersInfo, usersVO);
        usersVO.setFollow(isFollw);
        return IMoocJSONResult.ok(usersVO);

    }

    @ApiOperation(value = "查询视频发布用户信息", notes = "查询视频发布用户信息的接口")
    @PostMapping("/queryPublisher")
    public IMoocJSONResult queryPublisher(String loginUserId, String videoId, String publisherUserId) {
        if (StringUtils.isBlank(loginUserId)) {
            return IMoocJSONResult.errorMsg("");
        }

        // 1.查询视频发布者的信息
        Users users = userService.queryUserInfo(publisherUserId);
        UsersVO publisher = new UsersVO();
        BeanUtils.copyProperties(users, publisher);

        //2.查询登入者，和视频点赞关系
        boolean userLikeVideo = userService.isUserLikeVideo(loginUserId, videoId);

        PublisherVideo publisherVideo = new PublisherVideo();
        publisherVideo.setPublisher(publisher);
        publisherVideo.setUserLikeVideo(userLikeVideo);

        return IMoocJSONResult.ok(publisherVideo);

    }

    @ApiOperation(value = "关注某用户", notes = "关注某用户的接口")
    @PostMapping("/beyourfans")
    public IMoocJSONResult beYourFans(String userId, String fanId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return IMoocJSONResult.errorMsg("");
        }
        userService.saveUserFanRetion(userId, fanId);
        return IMoocJSONResult.ok("已关注");

    }

    @ApiOperation(value = "取消关注某用户", notes = "取消关注某用户的接口")
    @PostMapping("/dontbeyourfans")
    public IMoocJSONResult dontBeyourfans(String userId, String fanId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return IMoocJSONResult.errorMsg("");
        }

        userService.deleteUserFanRetion(userId, fanId);
        return IMoocJSONResult.ok("已取消");
    }

    @PostMapping("/reportUser")
    public IMoocJSONResult reportUser(@RequestBody UsersReport usersReport) throws Exception {

        // 保存举报信息
        userService.reportUser(usersReport);

        return IMoocJSONResult.errorMsg("举报成功...有你平台变得更美好...");
    }
}
