package com.scm.projectSCM.services;

import com.scm.projectSCM.entities.Contact;
import com.scm.projectSCM.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ContactService {
    Contact save(Contact contact);
    Contact update(Contact contact);
    List<Contact> getAll();
    Contact getById(String id);
    void delete(String id);
    List<Contact> search(String name, String email, String phoneNumber);
    List<Contact> getByUserId(String id);

    Page<Contact> searchByName(User user, String nameKeyword, int page, int size, String sortBy, String direction);

    Page<Contact> searchByEmail(User user, String emailKeyword, int page, int size, String sortBy, String direction);

    Page<Contact> searchByPhoneNumber(User user, String phoneNumberKeyword,  int page, int size, String sortBy, String direction);

    Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction);
}
