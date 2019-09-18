package com.xz.mapper;


import com.imooc.utils.MyMapper;
import com.zx.pojo.Comments;
import com.zx.pojo.vo.CommentsVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentsMapperCustom extends MyMapper<Comments> {


    @Select("select c.*,u.face_image as face_image,u.nickname,tu.nickname as toNickname from comments c left join users u " +
            "on c.from_user_id = u.id left join users tu on c.to_user_id = tu.id where c.video_id = #{videoId} order by c.create_time desc")
    public List<CommentsVO> queryComments(String videoId);
}