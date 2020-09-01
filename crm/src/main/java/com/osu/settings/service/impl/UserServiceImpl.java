package com.osu.settings.service.impl;

import com.osu.exception.LoginException;
import com.osu.settings.dao.UserDao;
import com.osu.settings.entity.User;
import com.osu.settings.service.UserService;
import com.osu.util.DateTimeUtil;
import com.osu.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findUser(User user, String ip) throws LoginException {
        user.setLoginPwd(MD5Util.getMD5(user.getLoginPwd()));
        User u = userDao.selectUser(user);
        if (u == null) {
            throw new LoginException("账号或密码错误");
        }
        //if (!u.getAllowIps().contains(ip)) {
        //    throw new LoginException("IP地址不合法");
        //}
        if (u.getExpireTime().compareTo(DateTimeUtil.getSysTime()) < 0) {
            throw new LoginException("用户已失效");
        }
        if ("0".equals(u.getLockState())) {
            throw new LoginException("用户已锁定");
        }
        return u;
    }

    @Override
    public List<User> findUsers() {
        List<User> users = userDao.selectUsers();
        return users;
    }

    @Override
    public User findUserById(String id) {
        User user = userDao.selectUserById(id);
        return user;
    }
}
