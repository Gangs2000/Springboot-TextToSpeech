package com.software.voiceapp.Voiceapp.Controller;

import com.software.voiceapp.Voiceapp.Repository.TextCollectionRepository;
import com.software.voiceapp.Voiceapp.Service.TextCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired TextCollectionService textCollectionService;
    @Autowired TextCollectionRepository textCollectionRepository;

    @GetMapping("/text-to-speech/save")
    public String renderSaveTextPage(HttpSession httpSession, ModelMap modelMap){
        modelMap.addAttribute("listData",textCollectionRepository.findByUploadedBy(httpSession.getAttribute("sessionEmailId").toString()));
        return "saveTextData";
    }

    @PostMapping(path="/saveText",consumes = "application/x-www-form-urlencoded")
    public RedirectView saveTextData(HttpServletRequest request, HttpSession httpSession){
        return textCollectionService.saveText(request, httpSession);
    }

    @GetMapping("/text-to-speech/update/{id}")
    public String renderUpdateTextPage(@PathVariable("id") String id, ModelMap modelMap){
        modelMap.addAttribute("data",textCollectionRepository.findById(id).get());
        return "updateTextData";
    }

    @PostMapping(path = "/updateText",consumes = "application/x-www-form-urlencoded")
    public RedirectView updateTextData(HttpServletRequest request, HttpSession httpSession){
        return textCollectionService.updateText(request, httpSession);
    }

    @GetMapping("/text-to-speech/delete/{id}")
    public RedirectView deleteTextData(@PathVariable("id") String id){
        return textCollectionService.deleteText(id);
    }
}
