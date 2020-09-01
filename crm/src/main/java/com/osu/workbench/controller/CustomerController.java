package com.osu.workbench.controller;

import com.osu.settings.entity.User;
import com.osu.settings.service.UserService;
import com.osu.util.DateTimeUtil;
import com.osu.util.UUIDUtil;
import com.osu.vo.PageList;
import com.osu.workbench.entity.Activity;
import com.osu.workbench.entity.ActivityRemark;
import com.osu.workbench.entity.Customer;
import com.osu.workbench.entity.CustomerRemark;
import com.osu.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/workbench/customer/pageList.do")
    @ResponseBody
    public PageList getActivities(PageList pageList) {
        int pageNo = Integer.parseInt(pageList.getPageNo());
        int pageSize = Integer.parseInt(pageList.getPageSize());
        Customer customer = new Customer();

        customer.setOwner(pageList.getOwner());
        customer.setName(pageList.getName());
        customer.setPhone(pageList.getPhone());
        customer.setWebsite(pageList.getWebsite());

        PageList page = customerService.findCustomers(pageNo, pageSize, customer);
        return page;
    }

    @RequestMapping(value = "/workbench/customer/getUsers.do")
    @ResponseBody
    public List<User> findUsers() {
        List<User> users = userService.findUsers();
        return users;
    }


    @RequestMapping(value = "/workbench/customer/save.do")
    @ResponseBody
    public Map<String,Object> save(Customer customer, HttpSession session) {
        Map<String,Object> result = new HashMap<>();
        customer.setId(UUIDUtil.getUUID());
        customer.setCreateBy(((User) session.getAttribute("user")).getName());
        customer.setCreateTime(DateTimeUtil.getSysTime());
        Boolean flag = customerService.addCustomer(customer);
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/customer/delete.do")
    @ResponseBody
    public Map<String,Object> deleteCustomer(String[] id) {
        Boolean flag = customerService.deleteCustomers(id);
        Map<String,Object> result = new HashMap<>();
        result.put("success",flag);
        return result;
    }

    @RequestMapping(value = "/workbench/customer/detail.do")
    public ModelAndView detail(String id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/workbench/customer/detail.jsp");
        Customer customer = customerService.findCustomer(id);
        User user = userService.findUserById(customer.getOwner());
        customer.setOwner(user.getName());
        mv.addObject("customer",customer);
        return mv;
    }

    @RequestMapping(value = "/workbench/customerRemark/getRemarks.do")
    @ResponseBody
    public List<CustomerRemark> getRemarks(String customerId) {
        List<CustomerRemark> remarks = customerService.findRemarks(customerId);
        return remarks;
    }
}
