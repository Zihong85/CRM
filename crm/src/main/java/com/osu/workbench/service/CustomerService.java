package com.osu.workbench.service;

import com.osu.vo.PageList;
import com.osu.workbench.entity.Customer;
import com.osu.workbench.entity.CustomerRemark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CustomerService {

    PageList findCustomers(int pageNo, int pageSize, Customer customer);

    Boolean addCustomer(Customer customer);

    Boolean deleteCustomers(String[] id);

    Customer findCustomer(String id);

    List<CustomerRemark> findRemarks(String customerId);

    List<String> findCustomersByName(String name);

}
