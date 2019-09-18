package com.xz.admin.mapper;


import java.util.List;

import com.xz.admin.pojo.Videos;
import com.xz.admin.pojo.VideosExample;
import org.apache.ibatis.annotations.Param;

public interface VideosMapper {
    int countByExample(VideosExample example);

    int deleteByExample(VideosExample example);

    int deleteByPrimaryKey(String id);

    int insert(Videos record);

    int insertSelective(Videos record);

    List<Videos> selectByExample(VideosExample example);

    Videos selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Videos record, @Param("example") VideosExample example);

    int updateByExample(@Param("record") Videos record, @Param("example") VideosExample example);

    int updateByPrimaryKeySelective(Videos record);

    int updateByPrimaryKey(Videos record);
}