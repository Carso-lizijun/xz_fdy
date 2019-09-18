package com.xz.mapper;


import com.imooc.utils.MyMapper;
import com.zx.pojo.SearchRecords;
import org.apache.ibatis.annotations.Select;

import java.util.List;



public interface SearchRecordsMapper extends MyMapper<SearchRecords> {

	@Select("select content from search_records group by content order by count(content) desc")
	public List<String> getHotwords();
}