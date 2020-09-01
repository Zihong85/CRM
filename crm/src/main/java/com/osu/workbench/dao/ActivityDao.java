package com.osu.workbench.dao;

import com.osu.workbench.entity.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityDao {

    Integer insertActivity(Activity activity);

    List<Activity> selectActivities(@Param("skipCount") int skipCount, @Param("pageSize") int pageSize, @Param("activity") Activity activity);

    Integer total();

    Integer delete(String[] id);

    Activity selectActivity(String id);

    Integer update(Activity activity);

    List<Activity> selectActivitiesByNameAndNotByClueId(@Param("activityName") String activityName, @Param("clueId") String clueId);

    List<Activity> selectActivitiesByNameAndClueId(@Param("activityName") String activityName, @Param("clueId") String clueId);

    List<Activity> selectActivitiesByName(String activityName);
}
