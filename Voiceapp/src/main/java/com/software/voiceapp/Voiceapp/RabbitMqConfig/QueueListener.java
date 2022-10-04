package com.software.voiceapp.Voiceapp.RabbitMqConfig;

import com.software.voiceapp.Voiceapp.Document.Users;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class QueueListener {

    @Autowired JavaMailSender javaMailSender;
    SimpleMailMessage simpleMailMessage=new SimpleMailMessage();

    //Sign up email Trigger
    @RabbitListener(queues = MqConfig.QUEUE1)
    public void TriggerSignUpEmail(Users user){
        simpleMailMessage.setFrom("evento.bookconfirmation@gmail.com");
        simpleMailMessage.setTo(user.get_id());
        simpleMailMessage.setSubject("Welcome to TexttoSpeech - Explore the amazing Features");
        simpleMailMessage.setText("\n Thanks for signing up our application, Explore the below features which are available for free to use."
                +"\n \n 1. Text to Speech with play button ( Just click play button to speech the text you typed or pasted )"
                +"\n \n 2. Highlight text anywhere from window to speech ( Simply highlight text anywhere from window to speech )"
                +"\n \n 3. Speech highlighted or typed text with and without background audio ( Highlight or click play button to speech with/without background track )"
                +"\n \n For any queries or concerns please drop an email to evento.bookconfirmation@gmail.com"
                +"\n \n Thanks & Regards"
                +"\n TexttoSpeech product Team.."
        );
        javaMailSender.send(simpleMailMessage);
        System.out.println("Signup email has been sent to "+user.get_id());
    }

    //Forgot password email trigger process
    @RabbitListener(queues = MqConfig.QUEUE2)
    public void TriggerForgotPinEmail(Users user){
        simpleMailMessage.setFrom("evento.bookconfirmation@gmail.com");
        simpleMailMessage.setTo(user.get_id());
        simpleMailMessage.setSubject("Alert!! TexttoSpeech - PIN Changed");
        simpleMailMessage.setText("\n Your application login password has been changed successfully.. Please find the below temporary password."
            +"\n \n Temporary PIN is "+user.getPassword()+" "
                +"\n \n It is not recommend to keep this password as your login password, Please change password as soon as you login the application."
                +"\n \n If in case of any queries please drop an email to evento.bookconformation@gmail.com"
                +"\n \n Thanks & Regards"
                +"\n TexttoSpeech product Team.."
        );
        javaMailSender.send(simpleMailMessage);
        System.out.println("Temporary password has been sent to "+user.get_id());
    }
}
