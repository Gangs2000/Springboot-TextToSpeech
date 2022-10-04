package com.software.voiceapp.Voiceapp.Interface;

import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface TextCollectionInterface {
    RedirectView saveText(HttpServletRequest request, HttpSession httpSession);
    RedirectView updateText(HttpServletRequest request, HttpSession httpSession);
    RedirectView deleteText(String id);
}
