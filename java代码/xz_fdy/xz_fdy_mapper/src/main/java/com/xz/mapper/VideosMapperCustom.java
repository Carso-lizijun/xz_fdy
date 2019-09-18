package com.xz.mapper;

import java.util.List;


import com.imooc.utils.MyMapper;
import com.zx.pojo.Videos;
import com.zx.pojo.vo.VideosVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PathVariable;


public interface VideosMapperCustom extends MyMapper<Videos> {

    /**
     * @Description: 条件查询所有视频列表@Param("videoDesc") String videoDesc,
     * @Param("userId") String userId)
     */
    @Select("<script>" +
            "select v.*,u.face_image as face_image,u.nickname as nickname from videos v left join users u " +
            "on u.id = v.user_id  where 1=1" +
            " <if test=\"videoDesc != null and videoDesc != ''\"> and v.video_desc like '%${videoDesc}%'</if> " +
            "<if test=\" userId != null and userId != '' \"> and v.user_id = #{userId}</if>" +
            " and v.status order by v.create_time desc" +
            "</script>")
    public List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc,@Param("userId") String userId);

    /**
     * 对视频喜欢的数量进行累加
     *
     * @param videId
     */
    @Update("update videos set like_counts = like_counts + 1 where id = #{videoId}")
    public void addVideoLikeCount(String videId);

    /**
     * 对视频喜欢的数量进行累减
     *
     * @param videId
     */
    @Update("update videos set like_counts = like_counts - 1 where id = #{videoId}")
    public void reduceVideoLikeCount(String videId);

    /**
     * 查找用户喜欢的视频
     *
     * @param userId
     * @return
     */
    @Select("select v.*,u.face_image as face_image,u.nickname as nickname from videos v left join users u on v.user_id = u.id where " +
            "v.id in (select ulv.video_id from users_like_videos ulv where ulv.user_id = #{userId}) and v.status = 1 order by v.create_time desc")
    public List<VideosVO> queryMyLikeVideos(String userId);

    /**
     * 查询用户关注的人的所用视频
     * @param userId
     * @return
     */
    @Select("select v.*,u.face_image as face_image,u.nickname as nickname from videos v left join users u on v.user_id = u.id where " +
            "v.user_id in (select uf.user_id from users_fans uf where uf.fan_id = #{userId}) and v.status = 1 order by v.create_time desc")
    public List<VideosVO> queryMyFollowVideos(String userId);
}