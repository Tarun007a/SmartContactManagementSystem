package com.scm.projectSCM.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
    public static String getEmailOfLoggedInUser(Authentication authentication){
        if(authentication instanceof OAuth2AuthenticationToken){
            var aOAuthAuthentication = (OAuth2AuthenticationToken)authentication;
            var cliendId = aOAuthAuthentication.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User)authentication.getPrincipal();
            String username = "";
            if(cliendId.equalsIgnoreCase("google")){
                System.out.println("Getting email form google");
                username = oauth2User.getAttribute("e" +
                        "mail").toString();
            }
            else if (cliendId.equalsIgnoreCase("github")){
                //here sir got the email using the username but this is not in our case and in the oauth2User there
                //is a gmail feild but it is null System.out.println(oauth2User.getAttributes()); this line print
                //everthing we get so we will be working on username@gmail.com as userId.
                System.out.println("Getting email from github");
                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("gmail").toString() : oauth2User.getAttribute("login").toString()+"@gmail.com";
                //System.out.println(oauth2User.getAttributes());
            }
            return username;
        }
        else{
            System.out.println("Getting data form local database");
            return authentication.getName();
        }
    }

    public static String getLinkForVerification(String emailToken){
        String link = "http://localhost:8081/auth/verify-email?token="+emailToken;
        return link;
    }
}
