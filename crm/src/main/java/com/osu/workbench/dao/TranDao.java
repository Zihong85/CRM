package com.osu.workbench.dao;

import com.osu.workbench.entity.Tran;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int insert(Tran tran);

    List<Tran> selectTrans(@Param("skipCount") int skipCount, @Param("pageSize") int pageSize);

    int total();

    Tran selectTranById(String id);

    int update(Tran tran);

    int getTotal();

    List<Map<String, Object>> getCharts();
}
