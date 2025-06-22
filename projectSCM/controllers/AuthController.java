package com.scm.projectSCM.controllers;

import com.scm.projectSCM.entities.User;
import com.scm.projectSCM.helper.Message;
import com.scm.projectSCM.helper.MessageType;
import com.scm.projectSCM.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;

@Controller
@RequestMapping("auth")
public class AuthController {
    UserService userService;
    public AuthController(UserService userService){
        this.userService = userService;
    }
    @RequestMapping("/verify-email")
    public String verifyEmail(@RequestParam("token")String emailToken, HttpSession session){
        User user = userService.findUserByEmailToken(emailToken).orElse(null);
        if(user != null){
            if(user.getEmailToken().equals(emailToken)){
                user.setEmailVerified(true);
                user.setEnabled(true);
                userService.updateUser(user);
                session.setAttribute("message", Message.builder()
                                .content("Your email is verified you can login")
                                .type(MessageType.red)
                                .build());
                return "user/success_page";
            }
        }
        session.setAttribute("message", Message.builder()
                .content("email not verified, please try again try messaging tarun if problem is not solved")
                .type(MessageType.red)
                .build());
        return "user/error_page";
    }
}
