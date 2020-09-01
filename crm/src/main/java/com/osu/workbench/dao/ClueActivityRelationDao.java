package com.osu.workbench.dao;

import com.osu.workbench.entity.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    List<ClueActivityRelation> getListByClueId(String clueId);

    int delete(String id);

    int insert(ClueActivityRelation relation);
}
