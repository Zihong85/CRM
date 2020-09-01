package com.osu.workbench.controller;

import com.osu.settings.entity.User;
import com.osu.settings.service.UserService;
import com.osu.util.DateTimeUtil;
import com.osu.util.UUIDUtil;
import com.osu.vo.PageList;
import com.osu.workbench.entity.*;
import com.osu.workbench.service.ActivityService;
import com.osu.workbench.service.ClueService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {

    @Autowired
    private ClueService clueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/workbench/clue/getUsers.do")
    @ResponseBody
    public List<User> findUsers() {
        List<User> users = clueService.getUsers();
        return users;
    }

    @RequestMapping(value = "/workbench/clue/save.do")
    @ResponseBody
    public Map<String,Object> save(Clue clue, HttpSession session) {
        Map<String,Object> result = new HashMap<>();
        Boolean flag = false;

        clue.setId(UUIDUtil.getUUID());
        clue.setCreateBy(((User) session.getAttribute("user")).getName());
        clue.setCreateTime(DateTimeUtil.getSysTime());

        if (clueService.add(clue)) {
            flag = true;
        }
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/clue/pageList.do")
    @ResponseBody
    public PageList getActivities(PageList pageList) {
        int pageNo = Integer.parseInt(pageList.getPageNo());
        int pageSize = Integer.parseInt(pageList.getPageSize());
        Clue clue  = new Clue();

        clue.setFullname(pageList.getFullname());
        clue.setCompany(pageList.getCompany());
        clue.setOwner(pageList.getOwner());
        clue.setPhone(pageList.getPhone());
        clue.setMphone(pageList.getMphone());
        clue.setSource(pageList.getSource());
        clue.setState(pageList.getState());

        PageList page = clueService.getPageList(pageNo,pageSize,clue);
        return page;
    }

    @RequestMapping(value = "/workbench/clue/edit.do")
    @ResponseBody
    public Map<String,Object> edit(String id) {
        Map<String,Object> result = new HashMap<>();

        List<User> users = clueService.getUsers();
        result.put("list",users);

        Clue clue = clueService.findClue(id);
        result.put("c",clue);

        return result;
    }

    @RequestMapping(value = "/workbench/clue/update.do")
    @ResponseBody
    public Map<String,Object> update(Clue clue, HttpSession session) {
        Map<String,Object> result = new HashMap<>();
        clue.setEditBy(((User) session.getAttribute("user")).getName());
        clue.setEditTime(DateTimeUtil.getSysTime());
        Boolean flag = clueService.updateClue(clue);
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/clue/delete.do")
    @ResponseBody
    public Map<String,Object> deleteActivities(String[] id) {
        Boolean flag = clueService.delete(id);
        Map<String,Object> result = new HashMap<>();
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/clue/detail.do")
    public ModelAndView detail(String id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/workbench/clue/detail.jsp");
        Clue clue = clueService.findClue(id);
        User user = userService.findUserById(clue.getOwner());
        clue.setOwner(user.getName());
        mv.addObject("clue",clue);
        return mv;
    }

    @RequestMapping(value = "/workbench/clue/getActivitiesByClueId.do")
    @ResponseBody
    public List<Activity> getActivitiesByClueId(String clueId) {
        List<Activity> activities = activityService.getActivitiesByClueId(clueId);
        return activities;
    }

    @RequestMapping(value = "/workbench/clue/unbundle.do")
    @ResponseBody
    public Map<String,Object> unbundle(String id) {
        Boolean flag = clueService.deleteRelation(id);
        Map<String,Object> result = new HashMap<>();
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/clue/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByNameAndNotByClueId(String activityName, String clueId) {
        List<Activity> activities = activityService.getActivityListByNameAndNotByClueId(activityName,clueId);
        return activities;
    }

    @RequestMapping(value = "/workbench/clue/bundle.do")
    @ResponseBody
    public Map<String,Object> bundle(String[] activityId, String clueId) {
        Boolean flag = clueService.addRelation(activityId, clueId);
        Map<String,Object> result = new HashMap<>();
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/clueRemark/addRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(String noteContent, String clueId, HttpSession session) {
        ClueRemark clueRemark = new ClueRemark();
        Map<String, Object> result = new HashMap<>();
        clueRemark.setId(UUIDUtil.getUUID());
        clueRemark.setNoteContent(noteContent);
        clueRemark.setCreateBy(((User) session.getAttribute("user")).getName());
        clueRemark.setCreateTime(DateTimeUtil.getSysTime());
        clueRemark.setClueId(clueId);
        clueRemark.setEditFlag(false);
        Boolean flag = clueService.addRemark(clueRemark);
        result.put("success",flag);
        result.put("cr",clueRemark);
        return result;
    }

    @RequestMapping(value = "/workbench/clueRemark/getRemarks.do")
    @ResponseBody
    public List<ClueRemark> getRemarks(String clueId) {
        List<ClueRemark> remarks = clueService.getRemarks(clueId);
        return remarks;
    }

    @RequestMapping(value = "/workbench/clueRemark/updateRemark.do")
    @ResponseBody
    public Map<String,Object> updateRemark(String noteContent, String id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        ClueRemark clueRemark = new ClueRemark();
        clueRemark.setId(id);
        clueRemark.setNoteContent(noteContent);
        clueRemark.setEditTime(DateTimeUtil.getSysTime());
        clueRemark.setEditBy(((User) session.getAttribute("user")).getName());
        clueRemark.setEditFlag(true);
        Boolean flag = clueService.updateRemark(clueRemark);
        result.put("success",flag);
        result.put("cr",clueRemark);
        return result;
    }

    @RequestMapping(value = "/workbench/clueRemark/deleteRemark.do")
    @ResponseBody
    public Map<String,Object> deleteRemark(String id) {
        Boolean flag = clueService.deleteRemark(id);
        Map<String,Object> result = new HashMap<>();
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/clue/convertPage.do")
    public ModelAndView convertPage(String clueId) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/workbench/clue/convert.jsp");
        Clue clue = clueService.findClue(clueId);
        User user = userService.findUserById(clue.getOwner());
        clue.setOwner(user.getName());
        mv.addObject("clue",clue);
        return mv;
    }

    @RequestMapping(value = "/workbench/clue/getActivityListByNameAndClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String activityName, String clueId) {
        List<Activity> activities = activityService.getActivityListByNameAndClueId(activityName,clueId);
        return activities;
    }

    @RequestMapping(value = "/workbench/clue/convert.do")
    public ModelAndView convert(HttpServletRequest request, HttpSession session) {
        Tran tran = null;
        ModelAndView mv = new ModelAndView();

        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");
        String createBy = ((User) session.getAttribute("user")).getName();

        if ("true".equals(flag)) {
            tran = new Tran();

            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectDate = request.getParameter("expectDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String type = request.getParameter("type");

            tran.setMoney(money);
            tran.setName(name);
            tran.setExpectedDate(expectDate);
            tran.setStage(stage);
            tran.setActivityId(activityId);
            tran.setId(UUIDUtil.getUUID());
            tran.setCreateBy(createBy);
            tran.setType(type);
            tran.setCreateTime(DateTimeUtil.getSysTime());
        }

        Boolean f = clueService.convert(clueId, tran, createBy);
        if (f) {
            mv.setViewName("/workbench/clue/index.jsp");
        }
        return mv;
    }
}
