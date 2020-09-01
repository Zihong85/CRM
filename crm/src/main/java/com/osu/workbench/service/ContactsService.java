package com.osu.workbench.service;

import com.osu.workbench.entity.Contacts;

import java.util.List;

public interface ContactsService {
    List<Contacts> findContactsByName(String contactsName);
}
