package com.osu.workbench.controller;

import com.osu.settings.entity.User;
import com.osu.settings.service.UserService;
import com.osu.util.DateTimeUtil;
import com.osu.util.UUIDUtil;
import com.osu.workbench.entity.Activity;
import com.osu.workbench.entity.ActivityRemark;
import com.osu.workbench.service.ActivityService;
import com.osu.vo.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/workbench/activity/save.do")
    @ResponseBody
    public Map<String,Object> save(Activity activity, HttpSession session) {
        Map<String,Object> result = new HashMap<>();
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateBy(((User) session.getAttribute("user")).getName());
        activity.setCreateTime(DateTimeUtil.getSysTime());
        Boolean flag = activityService.addActivity(activity);
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/activity/pageList.do")
    @ResponseBody
    public PageList getActivities(PageList pageList) {
        int pageNo = Integer.parseInt(pageList.getPageNo());
        int pageSize = Integer.parseInt(pageList.getPageSize());
        Activity activity = new Activity();

        activity.setOwner(pageList.getOwner());
        activity.setName(pageList.getName());
        activity.setStartDate(pageList.getStartDate());
        activity.setEndDate(pageList.getEndDate());

        PageList page = activityService.findActivities(pageNo, pageSize, activity);
        return page;
    }

    @RequestMapping(value = "/workbench/activity/delete.do")
    @ResponseBody
    public Map<String,Object> deleteActivities(String[] id) {
        Boolean flag = activityService.deleteActivities(id);
        Map<String,Object> result = new HashMap<>();
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/activity/edit.do")
    @ResponseBody
    public Map<String,Object> editActivity(String id) {
        Map<String,Object> result = new HashMap<>();

        List<User> users = userService.findUsers();
        result.put("list",users);

        Activity activity = activityService.findActivity(id);
        result.put("a",activity);

        return result;
    }

    @RequestMapping(value = "/workbench/activity/update.do")
    @ResponseBody
    public Map<String,Object> update(Activity activity, HttpSession session) {
        Map<String,Object> result = new HashMap<>();
        activity.setEditBy(((User) session.getAttribute("user")).getName());
        activity.setEditTime(DateTimeUtil.getSysTime());
        Boolean flag = activityService.updateActivity(activity);
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/activity/updateActivity.do")
    public void update(Activity activity, HttpSession session, HttpServletResponse response) throws IOException {
        activity.setEditBy(((User) session.getAttribute("user")).getName());
        activity.setEditTime(DateTimeUtil.getSysTime());
        response.sendRedirect("workbench/activity/detail.do?id=" + activity.getId());
    }

    @RequestMapping(value = "/workbench/activity/detail.do")
    public ModelAndView detail(String id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/workbench/activity/detail.jsp");
        Activity activity = activityService.findActivity(id);
        User user = userService.findUserById(activity.getOwner());
        activity.setOwner(user.getName());
        mv.addObject("activity",activity);
        return mv;
    }

    @RequestMapping(value = "/workbench/activityRemark/save.do")
    @ResponseBody
    public Map<String,Object> saveRemark(String noteContent, String activityId, HttpSession session) {
        ActivityRemark activityRemark = new ActivityRemark();
        Map<String, Object> result = new HashMap<>();
        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateBy(((User) session.getAttribute("user")).getName());
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setActivityId(activityId);
        activityRemark.setEditFlag(false);
        Boolean flag = activityService.addActivityRemark(activityRemark);
        result.put("success",flag);
        result.put("ar",activityRemark);
        return result;
    }

    @RequestMapping(value = "/workbench/activityRemark/getRemarks.do")
    @ResponseBody
    public List<ActivityRemark> getRemarks(String activityId) {
        List<ActivityRemark> remarks = activityService.findRemarks(activityId);
        return remarks;
    }

    @RequestMapping(value = "/workbench/activityRemark/deleteRemark.do")
    @ResponseBody
    public Map<String,Object> deleteRemark(String id) {
        Boolean flag = activityService.deleteRemark(id);
        Map<String,Object> result = new HashMap<>();
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/activityRemark/updateRemark.do")
    @ResponseBody
    public Map<String,Object> updateRemark(String noteContent, String id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditTime(DateTimeUtil.getSysTime());
        activityRemark.setEditBy(((User) session.getAttribute("user")).getName());
        activityRemark.setEditFlag(true);
        Boolean flag = activityService.updateRemark(activityRemark);
        result.put("success",flag);
        result.put("ar",activityRemark);
        return result;
    }

    @RequestMapping(value = "/workbench/activity/getActivityListByName.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String activityName) {
        List<Activity> activityList = activityService.findActivitiesByName(activityName);
        return activityList;
    }
}
