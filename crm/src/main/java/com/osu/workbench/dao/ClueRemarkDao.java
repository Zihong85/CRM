package com.osu.workbench.dao;

import com.osu.workbench.entity.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    int insert(ClueRemark clueRemark);

    List<ClueRemark> selectRemarks(String clueId);

    int update(ClueRemark clueRemark);

    int delete(String id);
}
