package com.scm.projectSCM.configPackage;

import com.scm.projectSCM.entities.Provider;
import com.scm.projectSCM.entities.User;
import com.scm.projectSCM.helper.AppConstants;
import com.scm.projectSCM.repositories.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("OAuthAuthenticationSuccessHandler");

        var oauth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
        logger.info(authorizedClientRegistrationId);

        var authUser = (DefaultOAuth2User)authentication.getPrincipal();
//        authUser.getAttributes().forEach((key, value) -> {
//            logger.info(key + " : " + value);
//        });

        User user1 = new User();
        user1.setUserId(UUID.randomUUID().toString());
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        user1.setPassword("dummy");
        user1.setEmailVerified(true);
        user1.setEnabled(true);

        if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
            String name = authUser.getAttribute("name").toString();
            String email = authUser.getAttribute("email").toString();
            String picture = authUser.getAttribute("picture").toString();

            user1.setName(name);
            user1.setEmail(email);
            user1.setProfilePic(picture);
            user1.setProviderId(authUser.getName());
            user1.setProvider(Provider.GOOGLE);
            user1.setAbout("This user is created using google");
        }
        else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
            String name = authUser.getAttribute("login").toString();
            String email = authUser.getAttribute("email") != null ? authUser.getAttribute("email").toString() :
                    name+"@gmail.com";
            String picture = authUser.getAttribute("avatar_url").toString();

            user1.setName(name);
            user1.setEmail(email);
            user1.setProfilePic(picture);
            user1.setProviderId(authUser.getName());
            user1.setProvider(Provider.GITHUB);
            user1.setAbout("This is user crated using github");
        }
        else{
            logger.info("unknown provider"+authorizedClientRegistrationId);
        }

        User user2 = userRepo.findByEmail(user1.getEmail()).orElse(null);
        if (user2 == null){
            userRepo.save(user1);
            logger.info("user saved "+user1.getEmail());
        }
        else logger.info("user already exists");
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}





























