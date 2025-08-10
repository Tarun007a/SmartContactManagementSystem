package com.scm.projectSCM.configPackage;

import com.scm.projectSCM.helper.Message;
import com.scm.projectSCM.helper.MessageType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {
//    @Autowired
//    private EmailService emailService;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof DisabledException){
            HttpSession session = request.getSession();
            session.setAttribute("message", Message.builder()
                            .content("User is disabled, please verify your account verification link is sent on your registered mail")
                            .type(MessageType.red)
                            .build());
//            emailService.sendEmail("karanchourey091@gmail.com", "Testing mail", "testing body");
            response.sendRedirect("/login");
        }
        else{
            response.sendRedirect("/login?error=true");
        }
    }
}
