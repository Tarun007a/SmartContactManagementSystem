package com.scm.projectSCM.controllers;

import com.scm.projectSCM.entities.Contact;
import com.scm.projectSCM.entities.User;
import com.scm.projectSCM.forms.ContactForm;
import com.scm.projectSCM.forms.ContactSearchForm;
import com.scm.projectSCM.helper.AppConstants;
import com.scm.projectSCM.helper.Helper;
import com.scm.projectSCM.helper.Message;
import com.scm.projectSCM.helper.MessageType;
import com.scm.projectSCM.services.ContactService;
import com.scm.projectSCM.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {
    ContactService contactService;
    UserService userService;
    Logger logger = LoggerFactory.getLogger(ContactController.class);
    public ContactController(ContactService contactService, UserService userService){
        this.contactService = contactService;
        this.userService = userService;
    }
    @RequestMapping("/add")
    public String addContactView(Model model){
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult rBindingResult, HttpSession session, Authentication authentication){

        if(rBindingResult.hasErrors()){
            Message message = Message.builder()
                    .content("Contact information not saved fill all attributes")
                    .type(MessageType.red)
                    .build();
            session.setAttribute("message", message);
            return "user/add_contact";
        }

        System.out.println(contactForm);

        Contact contact = new Contact();

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setFavorite(contactForm.isFavorite());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setUser(user);

        contactService.save(contact);

        Message message = Message.builder()
                .content("contact saved")
                .type(MessageType.green)
                .build();
        session.setAttribute("message", message);
        return "redirect:/user/contacts/add";
    }

    @RequestMapping
    private String viewContacts(
            @RequestParam(value = "page", defaultValue = AppConstants.PAGE)int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE)int size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY)String sortBy,
            @RequestParam(value = "direction", defaultValue = AppConstants.DIRECTION)String direction,
            Model model,
            Authentication authentication){
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        System.out.println(username);

        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageSize", size);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/contacts";
    }

    @RequestMapping(value = "/search")
    public String SearchHandler(
            @RequestParam(value = "page", defaultValue = AppConstants.PAGE)int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE)int size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY)String sortBy,
            @RequestParam(value = "direction", defaultValue = AppConstants.DIRECTION)String direction,
            @ModelAttribute ContactSearchForm contactSearchForm,
            Authentication authentication,
            Model model) {

        Page<Contact> pageContact = null;

        String userName = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userName);


        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContact = contactService.searchByName(user, contactSearchForm.getValue(), page, size, sortBy, direction);
        }
        else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            pageContact = contactService.searchByEmail(user, contactSearchForm.getValue(), page, size, sortBy, direction);
        }
        else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
            pageContact = contactService.searchByPhoneNumber(user, contactSearchForm.getValue(), page, size, sortBy, direction);
        }

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", size);
        return "user/search";
    }

    @RequestMapping("/delete/{contactId}")
    public String deleteContactHandler(@PathVariable String contactId,
                                        HttpSession session){
        contactService.delete(contactId);
        session.setAttribute("message",
                Message.builder()
                        .content("Contact deleted successfully")
                        .type(MessageType.green)
                        .build());
        return "redirect:/user/contacts";
    }

    @RequestMapping("/view/{contactId}")
    public String updateAndViewContactHandler(@PathVariable String contactId,
            Model model){
         Contact contact = contactService.getById(contactId);
         ContactForm contactForm = new ContactForm();

         contactForm.setName(contact.getName());
         contactForm.setEmail(contact.getEmail());
         contactForm.setPhoneNumber(contact.getPhoneNumber());
         contactForm.setAddress(contact.getAddress());
         contactForm.setDescription(contact.getDescription());
         contactForm.setFavorite(contact.isFavorite());
         contactForm.setWebsiteLink(contact.getWebsiteLink());
         contactForm.setLinkedInLink(contact.getLinkedInLink());
         contactForm.setPicture(contact.getPicture());

         model.addAttribute("contactForm", contactForm);
         model.addAttribute("contactId", contactId);

        return "user/update_contact_view";
    }

    @RequestMapping(value="/update/{contactId}", method = RequestMethod.POST)
    public String updateContactHandler(@Valid @ModelAttribute ContactForm contactForm,
                                       BindingResult bindingResult,
                                       @PathVariable String contactId,
                                       HttpSession session
                                       ){
        if(bindingResult.hasErrors()){
            Message message = Message.builder()
                .content("contact not updated please fill valid information")
                .type(MessageType.red)
                .build();
            session.setAttribute("message", message);
            return "/user/update_contact_view";
        }

        //to update
        var con = contactService.getById(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setFavorite(contactForm.isFavorite());
        con.setWebsiteLink(contactForm.getWebsiteLink());
        con.setLinkedInLink(contactForm.getLinkedInLink());

        Contact updatedContact = contactService.update(con);

        Message message = Message.builder()
                .content("contact updated successfully")
                .type(MessageType.green)
                .build();
        session.setAttribute("message", message);

        logger.info("contact updated successfully");
        return "redirect:/user/contacts";
    }
}
