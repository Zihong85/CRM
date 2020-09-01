package com.osu.workbench.service;

import com.osu.settings.entity.User;
import com.osu.vo.PageList;
import com.osu.workbench.entity.Clue;
import com.osu.workbench.entity.ClueRemark;
import com.osu.workbench.entity.Tran;

import java.util.List;

public interface ClueService {

    List<User> getUsers();

    Boolean add(Clue clue);

    PageList getPageList(int pageNo, int pageSize, Clue clue);

    Clue findClue(String id);

    Boolean updateClue(Clue clue);

    Boolean delete(String[] id);

    Boolean deleteRelation(String id);

    Boolean addRelation(String[] activityId, String clueId);

    Boolean addRemark(ClueRemark clueRemark);

    List<ClueRemark> getRemarks(String clueId);

    Boolean updateRemark(ClueRemark clueRemark);

    Boolean deleteRemark(String id);

    Boolean convert(String clueId, Tran tran, String createBy);

}
