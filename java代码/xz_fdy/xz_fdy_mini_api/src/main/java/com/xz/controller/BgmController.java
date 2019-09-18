package com.xz.controller;

import com.imooc.utils.IMoocJSONResult;
import com.xz.service.BgmService;
import com.xz.service.UserService;
import com.zx.pojo.Users;
import com.zx.pojo.vo.UsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @Author: huangfurong
 * @Description:
 * @Date: Create in 11:58 2019-07-03
 */
@RestController
@Api(value = "查询背景音乐接口", tags = {"查询背景音乐的controller"})
@RequestMapping("/bgm")
public class BgmController extends BasicController {

    @Autowired
    protected BgmService bgmService;


    @ApiOperation(value = "查询背景音乐列表", notes = "查询背景音乐的接口")
    @PostMapping("/list")
    public IMoocJSONResult list(){
        return IMoocJSONResult.ok(bgmService.queryBgmList());
    }

}
