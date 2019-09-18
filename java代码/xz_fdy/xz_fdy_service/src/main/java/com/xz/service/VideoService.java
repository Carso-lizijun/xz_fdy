package com.xz.service;

import com.imooc.utils.PagedResult;
import com.zx.pojo.Bgm;
import com.zx.pojo.Comments;
import com.zx.pojo.Videos;

import java.util.List;

/**
 * @Author: huangfurong
 * @Description:
 * @Date: Create in 12:23 2019-07-03
 */

public interface VideoService {

    /**
     * 储存视频
     * @param
     * @return
     */
    public String saveVideo(Videos videos);

    /**
     * 储存视频
     * @param
     * @return
     */
    public void updateVideo(String videoId,String coverPath);

    /**
     * 分页查询视频
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult getAllVideos(Videos videos,Integer isSaveRecord,Integer page,Integer pageSize);


    /**
     * 获得热搜词列表
     * @return
     */
    public List<String> getHotWords();


    /**
     * 用户喜欢视频
     * @param userId
     * @param videoId
     * @param videoCreateId
     */
    public void userLikeVideo(String userId,String videoId,String videoCreateId);

    /**
     * 用户不喜欢视频
     * @param userId
     * @param videoId
     * @param videoCreateId
     */
    public void userUnLikeVideo(String userId,String videoId,String videoCreateId);

    public  PagedResult queryMyLikeVideos(String userId,Integer page,Integer pageSize);

    public  PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize);

    public void saveComment(Comments comment);

    public PagedResult getAllComments(String videoId, Integer page, Integer pageSize);
}
