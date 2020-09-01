package com.osu.workbench.service;

import com.osu.workbench.entity.Tran;
import com.osu.workbench.entity.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran tran);

    Map<String, Object> getTrans(int pageNo, int pageSize);

    Tran getTranById(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    boolean changeStage(Tran tran);

    Map<String, Object> getCharts();
}
