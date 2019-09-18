package com.xz.service;

import com.zx.pojo.Bgm;
import com.zx.pojo.Users;

import java.util.List;

/**
 * @Author: huangfurong
 * @Description:
 * @Date: Create in 12:23 2019-07-03
 */

public interface BgmService {

    /**
     * 查询bgm列表
     * @param
     * @return
     */
    public List<Bgm> queryBgmList();

    /**
     * 通过id查询背景音乐
     * @param id
     * @return
     */
    public Bgm quryBgmById(String id);



}
