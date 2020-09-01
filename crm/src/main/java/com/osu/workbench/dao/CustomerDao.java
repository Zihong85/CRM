package com.osu.workbench.dao;

import com.osu.workbench.entity.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerDao {

    Customer findByName(String company);

    int insert(Customer customer);

    List<Customer> selectCustomers(@Param(value = "skipCount") int skipCount, @Param(value = "pageSize") int pageSize, @Param(value = "customer") Customer customer);

    Integer total();

    int delete(String[] id);

    Customer selectCustomer(String id);

    List<String> selectCustomersByName(String name);
}
