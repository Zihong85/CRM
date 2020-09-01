package com.osu.workbench.controller;

import com.osu.settings.entity.User;
import com.osu.settings.service.UserService;
import com.osu.util.DateTimeUtil;
import com.osu.util.UUIDUtil;
import com.osu.vo.PageList;
import com.osu.workbench.entity.*;
import com.osu.workbench.service.CustomerService;
import com.osu.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TranController {

    @Autowired
    private TranService tranService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/workbench/transaction/add.do")
    public ModelAndView add() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/workbench/transaction/save.jsp");
        List<User> users = userService.findUsers();
        mv.addObject("userList",users);
        return mv;
    }

    @RequestMapping(value = "/workbench/transaction/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name) {
        List<String> nameList = customerService.findCustomersByName(name);
        return nameList;
    }

    @RequestMapping(value = "/workbench/transaction/save.do")
    public ModelAndView save(Tran tran, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        tran.setCreateBy(((User) session.getAttribute("user")).getName());
        boolean flag = tranService.save(tran);
        if (flag) {
            mv.setViewName("/workbench/transaction/index.jsp");
        }
        return mv;
    }

    @RequestMapping(value = "/workbench/transaction/pageList.do")
    @ResponseBody
    public Map<String, Object> getTrans(String pageno, String pagesize) {
        int pageNo = Integer.parseInt(pageno);
        int pageSize = Integer.parseInt(pagesize);
        Map<String, Object> result = tranService.getTrans(pageNo,pageSize);
        return result;
    }

    @RequestMapping(value = "/workbench/transaction/detail.do")
    public ModelAndView detail(String id, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/workbench/transaction/detail.jsp");
        Tran tran = tranService.getTranById(id);
        ServletContext application = request.getServletContext();
        Map<String, String> pMap = (Map<String, String>) application.getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);
        mv.addObject("tran",tran);
        return mv;
    }

    @RequestMapping(value = "/workbench/transaction/getHistoryListByTranId.do")
    @ResponseBody
    public List<TranHistory> getHistoryListByTranId(String tranId, HttpServletRequest request) {
        ServletContext application = request.getServletContext();
        Map<String, String> pMap = (Map<String, String>) application.getAttribute("pMap");
        List<TranHistory> histories = tranService.getHistoryListByTranId(tranId);
        for (TranHistory history : histories) {
            String possibility = pMap.get(history.getStage());
            history.setPossibility(possibility);
        }
        return histories;
    }

    @RequestMapping(value = "/workbench/transaction/changeStage.do")
    @ResponseBody
    public Map<String, Object> getTrans(Tran tran, HttpSession session, HttpServletRequest request) {
        tran.setEditBy(((User) session.getAttribute("user")).getName());
        tran.setEditTime(DateTimeUtil.getSysTime());
        boolean flag = tranService.changeStage(tran);
        Map<String, Object> map = new HashMap<>();

        ServletContext application = request.getServletContext();
        Map<String, String> pMap = (Map<String, String>) application.getAttribute("pMap");
        tran.setPossibility(pMap.get(tran.getStage()));

        map.put("success", flag);
        map.put("t",tran);
        return map;
    }

    @RequestMapping(value = "/workbench/transaction/getCharts.do")
    @ResponseBody
    public Map<String, Object> getCharts() {
        Map<String, Object> map = new HashMap<>();

        map = tranService.getCharts();

        return map;
    }
}
