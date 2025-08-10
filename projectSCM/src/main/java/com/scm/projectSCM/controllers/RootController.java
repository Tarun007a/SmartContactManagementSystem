package com.scm.projectSCM.controllers;


import com.scm.projectSCM.entities.User;
import com.scm.projectSCM.helper.Helper;
import com.scm.projectSCM.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {
    UserService userService;
    public RootController(UserService userService){
        this.userService = userService;
    }

    Logger logger = LoggerFactory.getLogger(RootController.class);
    @ModelAttribute
    public void addLoggedInUserInfo(Model model, Authentication authentication){
        if(authentication == null)return;
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("user logged in {}", username);

        User user = userService.getUserByEmail(username);
        model.addAttribute("loggedInUser", user);
    }
}
