package com.osu.workbench.controller;

import com.osu.workbench.entity.Activity;
import com.osu.workbench.entity.Contacts;
import com.osu.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContactsController {

    @Autowired
    private ContactsService contactsService;

    @RequestMapping(value = "/workbench/contacts/getContactsListByName.do")
    @ResponseBody
    public List<Contacts> getContactsListByName(String contactsName) {
        List<Contacts> contacts = contactsService.findContactsByName(contactsName);
        return contacts;
    }
}
