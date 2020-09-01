package com.osu.workbench.dao;

import com.osu.workbench.entity.Activity;
import com.osu.workbench.entity.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByIds(String[] id);

    int deleteByIds(String[] id);

    int insert(ActivityRemark activityRemark);

    List<ActivityRemark> selectRemarks(String id);

    int delete(String activityId);

    int update(ActivityRemark activityRemark);
}
