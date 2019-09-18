package com.xz.service.com.zx.service.com.xz.service.Impl;


import com.xz.mapper.BgmMapper;
import com.xz.mapper.UsersMapper;
import com.xz.service.BgmService;
import com.xz.service.UserService;
import com.zx.pojo.Bgm;
import com.zx.pojo.Users;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author: huangfurong
 * @Description:
 * @Date: Create in 12:27 2019-07-03
 */
@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Bgm> queryBgmList() {
        return bgmMapper.selectAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Bgm quryBgmById(String id) {
        return bgmMapper.selectByPrimaryKey(id);
    }


}
