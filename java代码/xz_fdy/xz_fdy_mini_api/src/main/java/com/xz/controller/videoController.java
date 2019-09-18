package com.xz.controller;

import com.imooc.enums.VideoStatusEnum;
import com.imooc.utils.*;
import com.xz.service.BgmService;
import com.xz.service.VideoService;
import com.zx.pojo.Bgm;
import com.zx.pojo.Comments;
import com.zx.pojo.Users;
import com.zx.pojo.Videos;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: huangfurong
 * @Description:
 * @Date: Create in 11:58 2019-07-03
 */
@RestController
@Api(value = "上传视频接口", tags = {"上传视频的controller"})
@RequestMapping("/video")
public class videoController extends BasicController {

    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;

    @ApiOperation(value = "上传视频", notes = "用户上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "背景音乐播放长度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频的宽度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频的高度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频的描述", required = false,
                    dataType = "String", paramType = "form"),
    })
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public IMoocJSONResult upload(String userId, String bgmId, double videoSeconds, String videoWidth, String videoHeight, String desc, @ApiParam(value = "短视频", required = true) MultipartFile file) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id不能为空。。");
        }
        //文件保存的命名空间
        String filespace = "F:/xz_fdy_db";
        //保存到数据库中的相对路径
        String uploadPatnDB = "/" + userId + "/video";
        String coverPathDB = "/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        String finalVideoPath = null;
        try {
            if (file != null) {
                String filename = file.getOriginalFilename();
                String filenamePrefix = UUID.randomUUID().toString();
                if (!StringUtils.isBlank(filename)) {
                    //文件上传的最终保存路径
                    finalVideoPath = filespace + uploadPatnDB + "/" + filename;
                    //设置数据库保存的路径
                    uploadPatnDB += ("/" + filename);
                    coverPathDB = coverPathDB + "/" + filenamePrefix + ".jpg";
                    File outfile = new File(finalVideoPath);
                    if (outfile.getParentFile() != null || !outfile.isDirectory()) {
                        //创建父文件夹
                        outfile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(outfile);
                    inputStream = file.getInputStream();
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

            if (StringUtils.isNotBlank(bgmId)) {
                //查询bgm是否为空，如果不为空那就查询bgm的信息，并且合并成新的视频，并且保存新的视频
                Bgm bgm = bgmService.quryBgmById(bgmId);
                String mp3InputPath = FILE_SPACE + bgm.getPath();
                MergeVideoMp3 tool = new MergeVideoMp3(FFMPEG_EXE);
                String videoInputPath = tool.convertor2(finalVideoPath);
                String videoOutputPath = UUID.randomUUID().toString() + ".mp4";
                uploadPatnDB = "/" + userId + "/video" + "/" + videoOutputPath;
                finalVideoPath = FILE_SPACE + uploadPatnDB;
                tool.convertor(videoInputPath, mp3InputPath, videoSeconds, finalVideoPath);
            }
            FetchVideoCover videoInfo = new FetchVideoCover(FFMPEG_EXE);
            videoInfo.getCover(finalVideoPath, FILE_SPACE + "/" + coverPathDB);
        }
        //保存视频到数据库
        Videos videos = new Videos();
        videos.setAudioId(bgmId);
        videos.setUserId(userId);
        videos.setVideoHeight(Integer.parseInt(videoHeight));
        videos.setVideoWidth(Integer.parseInt(videoWidth));
        videos.setVideoDesc(desc);
        videos.setVideoPath(uploadPatnDB);
        videos.setCoverPath(coverPathDB);
        videos.setStatus(VideoStatusEnum.SUCCESS.value);
        videos.setCreateTime(new Date());
        videos.setVideoSeconds((float) videoSeconds);
        String videoId = videoService.saveVideo(videos);

        return IMoocJSONResult.ok(videoId);

    }


    /**
     * 分页和搜索视频
     * @param videos
     * @param isSaveRecord
     *           1- 需要保存
     *           0- 不需要保存
     * @param page
     * @return
     */
    @ApiOperation(value = "分页和搜索视频", notes = "分页和搜索视频的接口")
    @PostMapping(value = "/showAll")
    public IMoocJSONResult showAll(@RequestBody Videos videos,Integer isSaveRecord,Integer page){
        if (page == null){
            page =1;
        }
        PagedResult result = videoService.getAllVideos(videos,isSaveRecord,page, PAGE_SIZE);
        return IMoocJSONResult.ok(result);
    }

    @ApiOperation(value = "查询热搜词", notes = "查询热搜词的接口")
    @PostMapping(value = "/hot")
    public IMoocJSONResult hot(){
        return IMoocJSONResult.ok(videoService.getHotWords());
    }

    @ApiOperation(value = "用户点赞视频", notes = "用户点赞视频的接口")
    @PostMapping(value = "/userLikeVideo")
    public IMoocJSONResult userLike(String userId,String videoId,String videoCreaterId) throws Exception{
        videoService.userLikeVideo(userId,videoId,videoCreaterId);
        return IMoocJSONResult.ok();
    }

    @ApiOperation(value = "用户取消点赞视频", notes = "用户取消点赞视频的接口")
    @PostMapping(value = "/userUnLikeVideo")
    public IMoocJSONResult userUnLike(String userId,String videoId,String videoCreaterId) throws Exception{
        videoService.userUnLikeVideo(userId,videoId,videoCreaterId);
        return IMoocJSONResult.ok();
    }

    /**
     * @Description: 我收藏(点赞)过的视频列表
     */
    @PostMapping("/showMyLike")
    public IMoocJSONResult showMyLike(String userId, Integer page, Integer pageSize) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.ok();
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 6;
        }

        PagedResult videosList = videoService.queryMyLikeVideos(userId, page, pageSize);

        return IMoocJSONResult.ok(videosList);
    }


    /**
     * @Description: 我关注的人发的视频
     */
    @PostMapping("/showMyFollow")
    public IMoocJSONResult showMyFollow(String userId, Integer page) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.ok();
        }

        if (page == null) {
            page = 1;
        }

        int pageSize = 6;

        PagedResult videosList = videoService.queryMyFollowVideos(userId, page, pageSize);

        return IMoocJSONResult.ok(videosList);
    }

    @PostMapping("/saveComment")
    public IMoocJSONResult saveComment(@RequestBody Comments comment,
                                       String fatherCommentId, String toUserId) throws Exception {

        if(StringUtils.isNotBlank(fatherCommentId)&& StringUtils.isNotBlank(toUserId)){
            comment.setFatherCommentId(fatherCommentId);
            comment.setToUserId(toUserId);
        }

        videoService.saveComment(comment);
        return IMoocJSONResult.ok();
    }


    @PostMapping("/getVideoComments")
    public IMoocJSONResult getVideoComments(String videoId, Integer page, Integer pageSize) throws Exception {

        if (StringUtils.isBlank(videoId)) {
            return IMoocJSONResult.ok();
        }

        // 分页查询视频列表，时间顺序倒序排序
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PagedResult list = videoService.getAllComments(videoId, page, pageSize);

        return IMoocJSONResult.ok(list);
    }

}
