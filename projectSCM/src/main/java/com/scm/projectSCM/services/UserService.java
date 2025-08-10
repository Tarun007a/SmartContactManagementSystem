package com.scm.projectSCM.services;

import com.scm.projectSCM.entities.Contact;
import com.scm.projectSCM.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> updateUser(User user);
    void deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    List<User> getAllUser();

    Optional<User> findUserByEmailToken(String emailToken);

    User getUserByEmail(String email);
}
