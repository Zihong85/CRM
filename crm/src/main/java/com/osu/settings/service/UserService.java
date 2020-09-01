package com.osu.settings.service;

import com.osu.exception.LoginException;
import com.osu.settings.entity.User;

import java.util.List;

public interface UserService {

    User findUser(User user, String ip) throws LoginException;

    List<User> findUsers();

    User findUserById(String id);
}
