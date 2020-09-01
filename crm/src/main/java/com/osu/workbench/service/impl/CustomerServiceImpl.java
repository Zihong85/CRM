package com.osu.workbench.service.impl;

import com.osu.vo.PageList;
import com.osu.workbench.dao.CustomerDao;
import com.osu.workbench.dao.CustomerRemarkDao;
import com.osu.workbench.entity.Activity;
import com.osu.workbench.entity.Customer;
import com.osu.workbench.entity.CustomerRemark;
import com.osu.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerRemarkDao customerRemarkDao;

    @Override
    public PageList findCustomers(int pageNo, int pageSize, Customer customer) {
        int skipCount = (pageNo - 1) * pageSize;
        PageList pageList = new PageList();
        List<Customer> customers = customerDao.selectCustomers(skipCount, pageSize, customer);
        Integer total = customerDao.total();
        pageList.setTotal(total);
        pageList.setCustomers(customers);
        return pageList;
    }

    @Override
    public Boolean addCustomer(Customer customer) {
        Boolean flag = true;
        Integer count = customerDao.insert(customer);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Boolean deleteCustomers(String[] id) {
        boolean flag = true;

        int count1 = customerRemarkDao.getCountByIds(id);
        int count2 = customerRemarkDao.deleteByIds(id);

        if (count1 != count2) {
            flag = false;
        }

        int count3 = customerDao.delete(id);
        if (count3 != id.length) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Customer findCustomer(String id) {
        Customer customer = customerDao.selectCustomer(id);
        return customer;
    }

    @Override
    public List<CustomerRemark> findRemarks(String customerId) {
        List<CustomerRemark> customerRemarks = customerRemarkDao.selectByCustomerId(customerId);
        return customerRemarks;
    }

    @Override
    public List<String> findCustomersByName(String name) {
        List<String> nameList = customerDao.selectCustomersByName(name);
        return nameList;
    }
}
