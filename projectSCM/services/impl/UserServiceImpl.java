package com.scm.projectSCM.services.impl;

import com.scm.projectSCM.entities.Contact;
import com.scm.projectSCM.entities.User;
import com.scm.projectSCM.helper.AppConstants;
import com.scm.projectSCM.helper.Helper;
import com.scm.projectSCM.helper.ResourceNotFoundException;
import com.scm.projectSCM.repositories.UserRepo;
import com.scm.projectSCM.services.EmailService;
import com.scm.projectSCM.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, EmailService emailService){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());



    @Override
    public User saveUser(User user) {
        //by me check before saving as if we try so signup again with a same mail we get error
        User user1 = userRepo.findByEmail(user.getEmail()).orElse(null);

        if(user1 != null)return null;
        //Id
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        String emailLink = Helper.getLinkForVerification(emailToken);

        User savedUser = userRepo.save(user);
        emailService.sendEmail(savedUser.getEmail(), "Verify account, scm", emailLink);
        return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepo.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        //update user2 form user

        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderId(user.getProviderId());

        //save user in database
        User save = userRepo.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        userRepo.delete(user);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user = userRepo.findById(userId).orElse(null);
        return user != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null ? true : false;
    }

    @Override
    public Optional<User> findUserByEmailToken(String emailToken) {
        return userRepo.findByEmailToken(emailToken);
    }

    @Override
    public List<User> getAllUser() {
        return (List<User>) userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }
}
