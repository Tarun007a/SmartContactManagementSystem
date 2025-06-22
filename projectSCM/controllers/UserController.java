package com.scm.projectSCM.controllers;

import com.scm.projectSCM.helper.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @RequestMapping(value = "/dashboard")
    public String userDashboard(){
        return "user/dashboard";
    }
    @RequestMapping(value = "/profile")
    public String userProfile(){
        return "user/profile";
    }
}
