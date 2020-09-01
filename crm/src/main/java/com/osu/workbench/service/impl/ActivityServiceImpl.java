package com.osu.workbench.service.impl;

import com.osu.settings.dao.UserDao;
import com.osu.workbench.dao.ActivityDao;
import com.osu.workbench.dao.ActivityRemarkDao;
import com.osu.workbench.dao.ClueActivityRelationDao;
import com.osu.workbench.entity.Activity;
import com.osu.workbench.entity.ActivityRemark;
import com.osu.workbench.entity.ClueActivityRelation;
import com.osu.workbench.service.ActivityService;
import com.osu.vo.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private ActivityRemarkDao remarkDao;

    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Boolean addActivity(Activity activity) {
        Boolean flag = true;
        Integer count = activityDao.insertActivity(activity);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public PageList findActivities(Integer pageNo, Integer pageSize, Activity activity) {
        int skipCount = (pageNo - 1) * pageSize;
        PageList pageList = new PageList();
        List<Activity> activities = activityDao.selectActivities(skipCount, pageSize, activity);
        Integer total = activityDao.total();
        pageList.setTotal(total);
        pageList.setActivities(activities);
        return pageList;
    }

    @Override
    public Boolean deleteActivities(String[] id) {
        boolean flag = true;

        int count1 = remarkDao.getCountByIds(id);
        int count2 = remarkDao.deleteByIds(id);

        if (count1 != count2) {
            flag = false;
        }

        int count3 = activityDao.delete(id);
        if (count3 != id.length) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity findActivity(String id) {
        Activity activity = activityDao.selectActivity(id);
        return activity;
    }

    @Override
    public Boolean updateActivity(Activity activity) {
        Boolean flag = true;
        Integer count = activityDao.update(activity);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Boolean addActivityRemark(ActivityRemark activityRemark) {
        Boolean flag = false;
        int count = remarkDao.insert(activityRemark);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

    @Override
    public List<ActivityRemark> findRemarks(String id) {
        List<ActivityRemark> remarks = remarkDao.selectRemarks(id);
        return remarks;
    }

    @Override
    public Boolean deleteRemark(String activityId) {
        Boolean flag = false;
        int count = remarkDao.delete(activityId);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean updateRemark(ActivityRemark activityRemark) {
        Boolean flag = false;
        int count = remarkDao.update(activityRemark);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivitiesByClueId(String clueId) {
        List<Activity> activityList = new ArrayList<>();
        List<ClueActivityRelation> relations = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation relation : relations) {
            Activity activity = activityDao.selectActivity(relation.getActivityId());
            activity.setId(relation.getId());
            activity.setOwner(userDao.selectUserById(activity.getOwner()).getName());
            activityList.add(activity);
        }
        return activityList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(String activityName, String clueId) {
        List<Activity> activities = activityDao.selectActivitiesByNameAndNotByClueId(activityName, clueId);
        return activities;
    }

    @Override
    public List<Activity> getActivityListByNameAndClueId(String activityName, String clueId) {
        List<Activity> activities = activityDao.selectActivitiesByNameAndClueId(activityName, clueId);
        return activities;
    }

    @Override
    public List<Activity> findActivitiesByName(String activityName) {
        List<Activity> activities = activityDao.selectActivitiesByName(activityName);
        return activities;
    }
}
