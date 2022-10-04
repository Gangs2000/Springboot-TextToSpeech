package com.software.voiceapp.Voiceapp.Controller;

import com.software.voiceapp.Voiceapp.Repository.UsersRepository;
import com.software.voiceapp.Voiceapp.Service.UsersValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin(origins = "*")
public class SecureLoginController {

    @Autowired UsersValidationService usersValidationService;
    @Autowired UsersRepository usersRepository;

    @GetMapping("/")
    public String loginPage(){
        return "login";
    }

    @PostMapping(path = "/login-request", consumes = "application/x-www-form-urlencoded")
    public RedirectView loginRequest(HttpServletRequest request, HttpSession httpSession){
        return usersValidationService.validateLoginRequest(request, httpSession);
    }

    @PostMapping(path = "/registration-request",consumes = "application/x-www-form-urlencoded")
    public String registrationRequest(HttpServletRequest request){
        return usersValidationService.validateRegistrationRequest(request);
    }

    @GetMapping("/text-to-speech/dashboard")
    public String dashboard(){
        return "home";
    }

    @GetMapping("/login-failure")
    public String loginFailureRedirection(){
        return "loginFailure";
    }

    @GetMapping("/signIn-failure")
    public String signInFailureRedirection(){
        return "signInError";
    }

    @GetMapping("/sign-in")
    public RedirectView signInRequest(HttpSession httpSession){
        return usersValidationService.validateSignInRequest(httpSession);
    }

    @PostMapping(path = "/forgotPin",consumes = "application/x-www-form-urlencoded")
    public RedirectView changePassword(HttpServletRequest request){
        return usersValidationService.changePassword(request);
    }

    @GetMapping("/text-to-speech/updateProfile")
    public String renderUpdateProfilePage(HttpSession httpSession, ModelMap modelMap){
        modelMap.addAttribute("profile",usersRepository.findById(httpSession.getAttribute("sessionEmailId").toString()).get());
        return "updateProfile";
    }

    @PostMapping(path = "/updateProfile", consumes = "application/x-www-form-urlencoded")
    public RedirectView updateProfile(HttpServletRequest request){
        return usersValidationService.updateProfile(request);
    }

    @GetMapping("/pinChangeSuccess")
    public String pinChangeSuccess(){
        return "pinChangeSuccess";
    }

    @GetMapping("/pinChangeError")
    public String pinChangeError(){
        return "pinChangeError";
    }

    @GetMapping("/sign-up")
    public String signUpRequest(){
        return usersValidationService.validateSignUpRequest();
    }

    @GetMapping("/logout-success")
    public String logoutPage(){
        return "logout";
    }
}
