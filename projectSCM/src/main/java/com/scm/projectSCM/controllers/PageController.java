package com.scm.projectSCM.controllers;

import com.scm.projectSCM.entities.User;
import com.scm.projectSCM.forms.UserForm;
import com.scm.projectSCM.helper.Message;
import com.scm.projectSCM.helper.MessageType;
import com.scm.projectSCM.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {
    private UserService userService;
    public PageController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping("/")
    public String index(){
        return "redirect:/home";
    }
    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("home page");
        model.addAttribute("name","Substring Technology");
        model.addAttribute("youtubeChannel", "PirroKhiladaIsLive");
        model.addAttribute("gitRepo", "https://github.com/tarun007");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("isLogin", false);
        System.out.println("about page loading");
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage(){
        System.out.println("services page loading");
        return "services";
    }

    @RequestMapping("/contact")
    public String contact(){
        return "contact";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/register")
    public String register(Model model){
        UserForm userForm = new UserForm();
        //default data
        model.addAttribute("userForm", userForm);
        return "register";
    }

    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session){
        System.out.println("processing form");
        System.out.println(userForm);

        //validate the data
        if(rBindingResult.hasErrors()){
            return "register";
        }


//          Dont use builder as it dont take those default values
//        User user = User.builder()
//                .name(userForm.getName())
//                .email(userForm.getEmail())
//                .password(userForm.getPassword())
//                .about(userForm.getAbout())
//                .phoneNumber(userForm.getPhoneNumber())
//                .profilePic("") //pic url here
//                .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("");

        User savedUser = userService.saveUser(user);
        System.out.println("user saved");

        //by me is user already exist I returned null there
        if(savedUser == null){
            Message message = Message.builder()
                    .content("Email already registered")
                    .type(MessageType.red)
                    .build();
            session.setAttribute("message", message);
            return "redirect:/register";
        }

        //add message
        Message message = Message.builder()
                .content("Registration Successful")
                .type(MessageType.green)
                .build();
        session.setAttribute("message", message);

        return "redirect:/register";
    }
}
