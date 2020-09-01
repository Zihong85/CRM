package com.osu.settings.controller;

import com.osu.exception.LoginException;
import com.osu.settings.entity.User;
import com.osu.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/settings/user/login.do")
    @ResponseBody
    public Map<String,Object> login(User user, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        Map<String,Object> result = new HashMap<>();
        User u = null;
        result.put("success",1);
        try {
            u = userService.findUser(user, request.getRemoteAddr());
        } catch (LoginException e) {
            String msg = e.getMessage();
            result.put("msg",msg);
            result.put("success",0);
            return result;
        }
        session.setAttribute("user",u);
        return result;
    }

    @RequestMapping(value = "/workbench/activity/getUsers.do")
    @ResponseBody
    public List<User> getUsers() {
        List<User> users = userService.findUsers();
        return users;
    }
}
