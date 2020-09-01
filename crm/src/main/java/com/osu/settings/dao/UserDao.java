package com.osu.settings.dao;

import com.osu.settings.entity.User;

import java.util.List;

public interface UserDao {

    User selectUser(User user);

    List<User> selectUsers();

    User selectUserById(String id);
}
