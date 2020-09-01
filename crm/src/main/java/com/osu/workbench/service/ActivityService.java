package com.osu.workbench.service;

import com.osu.workbench.entity.Activity;
import com.osu.workbench.entity.ActivityRemark;
import com.osu.vo.PageList;

import java.util.List;

public interface ActivityService {

    Boolean addActivity(Activity activity);

    PageList findActivities(Integer pageNo, Integer pageSize, Activity activity);

    Boolean deleteActivities(String[] id);

    Activity findActivity(String id);

    Boolean updateActivity(Activity activity);

    Boolean addActivityRemark(ActivityRemark activityRemark);

    List<ActivityRemark> findRemarks(String id);

    Boolean deleteRemark(String activityId);

    Boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivitiesByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(String activityName, String clueId);

    List<Activity> getActivityListByNameAndClueId(String activityName, String clueId);

    List<Activity> findActivitiesByName(String activityName);
}
