package com.osu.settings.controller;

import com.osu.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DicController {

    @Autowired
    private DicService dicService;
}
