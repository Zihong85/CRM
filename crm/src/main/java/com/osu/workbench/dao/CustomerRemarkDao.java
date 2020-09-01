package com.osu.workbench.dao;

import com.osu.workbench.entity.CustomerRemark;

import java.util.List;

public interface CustomerRemarkDao {

    int insert(CustomerRemark customerRemark);

    int getCountByIds(String[] id);

    int deleteByIds(String[] id);

    List<CustomerRemark> selectByCustomerId(String customerId);
}
