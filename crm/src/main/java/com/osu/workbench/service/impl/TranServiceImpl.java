package com.osu.workbench.service.impl;

import com.osu.util.DateTimeUtil;
import com.osu.util.UUIDUtil;
import com.osu.workbench.dao.CustomerDao;
import com.osu.workbench.dao.TranDao;
import com.osu.workbench.dao.TranHistoryDao;
import com.osu.workbench.entity.Customer;
import com.osu.workbench.entity.Tran;
import com.osu.workbench.entity.TranHistory;
import com.osu.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {
    @Autowired
    private TranDao tranDao;

    @Autowired
    private TranHistoryDao tranHistoryDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    public boolean save(Tran tran) {
        boolean flag = true;
        Customer customer = customerDao.findByName(tran.getCustomerId());
        if (customer == null) {
            Customer newCustomer = new Customer();
            newCustomer.setId(UUIDUtil.getUUID());
            newCustomer.setNextContactTime(tran.getNextContactTime());
            newCustomer.setContactSummary(tran.getContactSummary());
            newCustomer.setDescription(tran.getDescription());
            newCustomer.setOwner(tran.getOwner());
            newCustomer.setName(tran.getCustomerId());
            newCustomer.setCreateBy(tran.getCreateBy());
            newCustomer.setCreateTime(tran.getCreateTime());
            tran.setCustomerId(newCustomer.getId());
            int count = customerDao.insert(newCustomer);
            if (count != 1) flag = false;
        }
        else {
            tran.setCustomerId(customer.getId());
        }
        int insert = tranDao.insert(tran);
        if (insert != 1) flag = false;

        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        tranHistory.setCreateTime(tran.getCreateTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setExpectedDate(tran.getExpectedDate());

        int insert1 = tranHistoryDao.insert(tranHistory);
        if (insert1 != 1) flag = false;
        return flag;
    }

    @Override
    public Map<String, Object> getTrans(int pageNo, int pageSize) {
        int skipCount = (pageNo - 1) * pageSize;
        List<Tran> trans = tranDao.selectTrans(skipCount,pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("trans",trans);
        int total = tranDao.total();
        map.put("total",total);
        return map;
    }

    @Override
    public Tran getTranById(String id) {
        Tran tran = tranDao.selectTranById(id);
        return tran;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> histories = tranHistoryDao.selectByTranId(tranId);
        return histories;
    }

    @Override
    public boolean changeStage(Tran tran) {
        boolean flag = true;
        int count1 = tranDao.update(tran);
        if (count1 != 1) flag = false;

        TranHistory history = new TranHistory();
        history.setId(UUIDUtil.getUUID());
        history.setExpectedDate(tran.getExpectedDate());
        history.setCreateBy(tran.getEditBy());
        history.setCreateTime(tran.getEditTime());
        history.setTranId(tran.getId());
        history.setMoney(tran.getMoney());
        history.setStage(tran.getStage());
        int count2 = tranHistoryDao.insert(history);
        if (count2 != 1) flag = false;
        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {

        //取得total
        int total = tranDao.getTotal();

        //取得dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();

        //将total和dataList保存到map中
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("dataList", dataList);

        //返回map
        return map;
    }
}
