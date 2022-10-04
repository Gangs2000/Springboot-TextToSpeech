package com.software.voiceapp.Voiceapp.Document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
@Document
public class Users {
    @Id
    private String _id;
    private String password;
    private String userName;
}
