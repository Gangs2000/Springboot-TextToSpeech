package com.software.voiceapp.Voiceapp.Interface;

import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface UsersValidationInterface {
    RedirectView validateLoginRequest(HttpServletRequest request, HttpSession httpSession);
    String validateRegistrationRequest(HttpServletRequest request);
    RedirectView validateSignInRequest(HttpSession httpSession);
    RedirectView changePassword(HttpServletRequest request);
    RedirectView updateProfile(HttpServletRequest request);
    String validateSignUpRequest();
}
