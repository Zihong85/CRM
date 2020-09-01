package com.osu.workbench.service.impl;

import com.osu.workbench.dao.ContactsDao;
import com.osu.workbench.entity.Contacts;
import com.osu.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsDao contactsDao;


    @Override
    public List<Contacts> findContactsByName(String contactsName) {

        List<Contacts> contacts = contactsDao.selectContactsByName(contactsName);
        return contacts;
    }
}
