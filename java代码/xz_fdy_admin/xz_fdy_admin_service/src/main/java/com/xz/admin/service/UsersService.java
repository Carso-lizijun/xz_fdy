package com.xz.admin.service;


import com.imooc.utils.PagedResult;
import com.xz.admin.pojo.Users;

public interface UsersService {

	public PagedResult queryUsers(Users user, Integer page, Integer pageSize);
	
}
