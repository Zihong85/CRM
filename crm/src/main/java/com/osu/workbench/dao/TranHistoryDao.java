package com.osu.workbench.dao;

import com.osu.workbench.entity.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int insert(TranHistory tranHistory);

    List<TranHistory> selectByTranId(String tranId);
}
