package com.scm.projectSCM.services.impl;

import com.scm.projectSCM.entities.Contact;
import com.scm.projectSCM.entities.User;
import com.scm.projectSCM.helper.ResourceNotFoundException;
import com.scm.projectSCM.repositories.ContactRepo;
import com.scm.projectSCM.services.ContactService;
import com.scm.projectSCM.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    ContactRepo contactRepo;
    UserService userService;
    public ContactServiceImpl(ContactRepo contactRepo, UserService userService){
        this.contactRepo = contactRepo;
        this.userService = userService;
    }

    @Override
    public Contact save(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        System.out.println("saving contact "+ contact.getName());
        return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        Contact oldContact = contactRepo.getReferenceById(contact.getId());
        oldContact.setName(contact.getName());
        oldContact.setEmail(contact.getEmail());
        oldContact.setPhoneNumber(contact.getPhoneNumber());
        oldContact.setAddress(contact.getAddress());
        oldContact.setDescription(contact.getDescription());
        oldContact.setFavorite(contact.isFavorite());
        oldContact.setWebsiteLink(contact.getWebsiteLink());
        oldContact.setLinkedInLink(contact.getLinkedInLink());
        oldContact.setPicture(contact.getPicture());

        return contactRepo.save(oldContact);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("contact not fount with id "+id));
    }

    @Override
    public void delete(String id) {
        Contact contact = this.getById(id);
        contactRepo.delete(contact);
    }

    @Override
    public List<Contact> search(String name, String email, String phoneNumber) {
        return null;
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUser(user, pageable);
    }


    @Override
    public Page<Contact> searchByName(User user, String nameKeyword, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndNameContaining(user, nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(User user, String emailKeyword, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(User user, String phoneNumberKeyword, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }

    @Override
    public List<Contact> getByUserId(String id) {
//        by me simply get the user by id and then in Contact we have field as user so easy to define in repo
//        User user = userService.getUserById(id).orElseThrow(() -> new ResourceNotFoundException("user not found with id "+id));
//        return contactRepo.findByUser(user);
        return contactRepo.findByUserId(id);
    }
}
