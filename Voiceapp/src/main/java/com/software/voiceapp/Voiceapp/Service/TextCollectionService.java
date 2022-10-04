package com.software.voiceapp.Voiceapp.Service;

import com.software.voiceapp.Voiceapp.Document.TextCollection;
import com.software.voiceapp.Voiceapp.Interface.TextCollectionInterface;
import com.software.voiceapp.Voiceapp.Repository.TextCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class TextCollectionService implements TextCollectionInterface {

    @Autowired TextCollection textCollection;
    @Autowired TextCollectionRepository textCollectionRepository;

    RedirectView redirectView=new RedirectView();

    @Override
    public RedirectView saveText(HttpServletRequest request, HttpSession httpSession) {
        textCollection.set_id(UUID.randomUUID().toString());                            //Random String for file ID
        textCollection.setUploadedBy(httpSession.getAttribute("sessionEmailId").toString());
        textCollection.setTextTitle(request.getParameter("textTitle"));
        textCollection.setTextContent(request.getParameter("textContent"));
        textCollection.setLastModified(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")));
        textCollectionRepository.save(textCollection);
        redirectView.setUrl("/text-to-speech/save");
        return redirectView;
    }

    @Override
    public RedirectView updateText(HttpServletRequest request, HttpSession httpSession) {
        textCollection=textCollectionRepository.findById(request.getParameter("id")).get();
        textCollection.setTextTitle(request.getParameter("textTitle"));
        textCollection.setTextContent(request.getParameter("textContent"));
        textCollection.setLastModified(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")));
        textCollectionRepository.save(textCollection);
        redirectView.setUrl("/text-to-speech/save");
        return redirectView;
    }

    @Override
    public RedirectView deleteText(String id) {
        redirectView.setUrl("/text-to-speech/save");
        textCollectionRepository.deleteById(id);
        return redirectView;
    }
}
