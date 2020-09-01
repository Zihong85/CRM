package com.osu.workbench.dao;

import com.osu.workbench.entity.Contacts;

import java.util.List;

public interface ContactsDao {

    int insert(Contacts contacts);

    List<Contacts> selectContactsByName(String contactsName);
}
