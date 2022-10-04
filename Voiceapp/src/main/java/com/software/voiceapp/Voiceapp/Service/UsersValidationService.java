package com.software.voiceapp.Voiceapp.Service;

import com.software.voiceapp.Voiceapp.Document.Users;
import com.software.voiceapp.Voiceapp.Interface.UsersValidationInterface;
import com.software.voiceapp.Voiceapp.RabbitMqConfig.MqConfig;
import com.software.voiceapp.Voiceapp.Repository.UsersRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Service
public class UsersValidationService implements UsersValidationInterface {

    @Autowired Users users;
    @Autowired UsersRepository usersRepository;
    BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(15);
    @Autowired AuthenticationProvider authenticationProvider;
    RedirectView redirectView=new RedirectView();
    @Autowired RabbitTemplate rabbitTemplate;

    @Override
    public RedirectView validateLoginRequest(HttpServletRequest request, HttpSession httpSession) {
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(request.getParameter("emailId"),request.getParameter("password"));
        if(usersRepository.existsById(request.getParameter("emailId"))){
            if(encoder.matches(request.getParameter("password"),usersRepository.findById(request.getParameter("emailId")).get().getPassword())) {
                Authentication authentication= authenticationProvider.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                httpSession.setAttribute("sessionEmailId",request.getParameter("emailId"));
                redirectView.setUrl("/text-to-speech/dashboard");
            }
            else
                redirectView.setUrl("/login-failure");
        }
        else
            redirectView.setUrl("/login-failure");
        return redirectView;
    }

    @Override
    public String validateRegistrationRequest(HttpServletRequest request) {
        if(usersRepository.existsById(request.getParameter("emailId")))
            return "registrationFailure";
        else {
            users.set_id(request.getParameter("emailId"));
            users.setPassword(encoder.encode(request.getParameter("password")));
            users.setUserName(request.getParameter("userName"));
            usersRepository.save(users);
            return "registrationSuccess";
        }
    }

    @Override
    public RedirectView validateSignInRequest(HttpSession httpSession) {
        OAuth2AuthenticationToken oauth2=(OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if(usersRepository.existsById(oauth2.getPrincipal().getAttribute("email").toString())) {
            httpSession.setAttribute("sessionEmailId",oauth2.getPrincipal().getAttribute("email"));
            redirectView.setUrl("/text-to-speech/dashboard");
        }
        else
            redirectView.setUrl("/signIn-failure");
        return redirectView;
    }

    @Override
    public RedirectView changePassword(HttpServletRequest request) {
        Random random=new Random();
        if(usersRepository.existsById(request.getParameter("emailId"))) {
            users=usersRepository.findById(request.getParameter("emailId")).get();
            String plainTextPassword= String.valueOf(String.format("%08d",random.nextInt(999999999)));
            users.setPassword(encoder.encode(plainTextPassword));                                          //Setting some random characters as a password
            users=usersRepository.save(users);
            //Produce data into Rabbit MQ for resetting password..
            users.setPassword(plainTextPassword);                                                         //Setting plain text as a password since password is bcrypted while storing into DB
            rabbitTemplate.convertAndSend(MqConfig.EXCHANGE,MqConfig.ROUTING_KEY2,users);
            redirectView.setUrl("/pinChangeSuccess");
        }
        else
            redirectView.setUrl("/pinChangeError");
        return redirectView;
    }

    @Override
    public RedirectView updateProfile(HttpServletRequest request) {
        users=usersRepository.findById(request.getParameter("emailId")).get();
        if(request.getParameter("password").equals("")) {
            users.setUserName(request.getParameter("userName"));
            usersRepository.save(users);
            redirectView.setUrl("/text-to-speech/updateProfile");
        }
        else {
            users.setUserName(request.getParameter("userName"));
            users.setPassword(encoder.encode(request.getParameter("password")));
            usersRepository.save(users);
            redirectView.setUrl("/logout");
        }
        return redirectView;
    }

    @Override
    public String validateSignUpRequest() {
        OAuth2AuthenticationToken oauth2=(OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if(usersRepository.existsById(oauth2.getPrincipal().getAttribute("email").toString()))
            return "signUpError";
        else {
            users.set_id(oauth2.getPrincipal().getAttribute("email").toString());
            users.setUserName(oauth2.getPrincipal().getAttribute("name").toString());
            users=usersRepository.save(users);
            //Produce data into Rabbit MQ for successful signup process..
            rabbitTemplate.convertAndSend(MqConfig.EXCHANGE,MqConfig.ROUTING_KEY1,users);
            return "signUpSuccess";
        }
    }
}
