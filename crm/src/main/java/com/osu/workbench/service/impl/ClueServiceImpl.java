package com.osu.workbench.service.impl;

import com.osu.settings.entity.User;
import com.osu.util.DateTimeUtil;
import com.osu.util.UUIDUtil;
import com.osu.vo.PageList;
import com.osu.workbench.dao.*;
import com.osu.workbench.entity.*;
import com.osu.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueDao clueDao;

    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;

    @Autowired
    private ClueRemarkDao clueRemarkDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerRemarkDao customerRemarkDao;

    @Autowired
    private ContactsDao contactsDao;

    @Autowired
    private ContactsRemarkDao contactsRemarkDao;

    @Autowired
    private ContactsActivityRelationDao contactsActivityRelationDao;

    @Autowired
    private TranDao tranDao;

    @Autowired
    private TranHistoryDao tranHistoryDao;


    @Override
    public List<User> getUsers() {
        List<User> users = clueDao.selectUsers();
        return users;
    }

    @Override
    public Boolean add(Clue clue) {
        Boolean flag = false;
        int count = clueDao.insert(clue);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

    @Override
    public PageList getPageList(int pageNo, int pageSize, Clue clue) {
        int skipCount = (pageNo - 1) * pageSize;
        PageList pageList = new PageList();
        List<Clue> clues = new ArrayList<>();
        try {
            clues = clueDao.findClues(skipCount,pageSize,clue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Integer total = clueDao.total();
        pageList.setTotal(total);
        pageList.setClues(clues);
        return pageList;
    }

    @Override
    public Clue findClue(String id) {

        Clue clue = clueDao.findClue(id);
        return clue;
    }

    @Override
    public Boolean updateClue(Clue clue) {
        Boolean flag = true;
        Integer count = clueDao.update(clue);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Boolean delete(String[] id) {

        boolean flag = true;
        int count = clueDao.delete(id);
        if (count != id.length) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Boolean deleteRelation(String id) {
        Boolean flag = false;
        int count = clueActivityRelationDao.delete(id);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean addRelation(String[] activityId, String clueId) {
        int count = 0;
        Boolean flag = false;
        for (int i = 0; i < activityId.length; i++) {
            ClueActivityRelation relation = new ClueActivityRelation();
            relation.setActivityId(activityId[i]);
            relation.setClueId(clueId);
            relation.setId(UUIDUtil.getUUID());
            count += clueActivityRelationDao.insert(relation);
        }
        if (count == activityId.length) {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean addRemark(ClueRemark clueRemark) {
        Boolean flag = false;
        int count = clueRemarkDao.insert(clueRemark);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

    @Override
    public List<ClueRemark> getRemarks(String clueId) {
        List<ClueRemark> remarks = clueRemarkDao.selectRemarks(clueId);
        return remarks;
    }

    @Override
    public Boolean updateRemark(ClueRemark clueRemark) {
        Boolean flag = false;
        int count = clueRemarkDao.update(clueRemark);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean deleteRemark(String id) {
        Boolean flag = false;
        int count = clueRemarkDao.delete(id);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean convert(String clueId, Tran tran, String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;
        Clue clue = clueDao.findClue(clueId);
        String company = clue.getCompany();
        Customer customer = customerDao.findByName(company);
        if (customer == null) {
            // 数据库中没有客户，需新建客户
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setOwner(clue.getOwner());
            customer.setName(clue.getCompany());
            customer.setDescription(clue.getDescription());
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setPhone(clue.getPhone());

            int count = customerDao.insert(customer);
            if (count != 1) flag = false;
        }

        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setCreateBy(createBy);
        contacts.setCustomerId(customer.getId());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());

        int count = contactsDao.insert(contacts);
        if (count != 1) flag = false;

        List<ClueRemark> clueRemarks = clueRemarkDao.selectRemarks(clueId);
        for (ClueRemark clueRemark : clueRemarks) {
            String noteContent = clueRemark.getNoteContent();
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCustomerId(customer.getId());
            int count1 = customerRemarkDao.insert(customerRemark);
            if (count1 != 1) flag = false;

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setContactsId(contacts.getId());
            int count2 = contactsRemarkDao.insert(contactsRemark);
            if (count2 != 1) flag = false;
        }

        List<ClueActivityRelation> relations = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation relation : relations) {
            String activityId = relation.getActivityId();
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            int count3 = contactsActivityRelationDao.insert(contactsActivityRelation);
            if (count3 != 1) flag = false;
        }

        if (tran != null) {
            tran.setSource(clue.getSource());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setOwner(clue.getOwner());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(contacts.getId());
            tran.setCustomerId(customer.getId());
            int count4 = tranDao.insert(tran);
            if (count4 != 1) flag = false;

            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setStage(tran.getStage());
            tranHistory.setTranId(tran.getId());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            int count5 = tranHistoryDao.insert(tranHistory);
            if (count5 != 1) flag = false;
        }

        for (ClueRemark clueRemark : clueRemarks) {
            int count6 = clueRemarkDao.delete(clueRemark.getId());
            if (count6 != 1) flag = false;
        }
        for (ClueActivityRelation relation : relations) {
            int count7 = clueActivityRelationDao.delete(relation.getId());
            if (count7 != 1) flag = false;
        }

        String[] clueIds = new String[1];
        clueIds[0] = clueId;
        int count8 = clueDao.delete(clueIds);
        if (count8 != 1) flag = false;
        return flag;
    }
}
