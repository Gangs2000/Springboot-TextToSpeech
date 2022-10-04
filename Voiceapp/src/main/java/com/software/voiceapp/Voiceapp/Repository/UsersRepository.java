package com.software.voiceapp.Voiceapp.Repository;

import com.software.voiceapp.Voiceapp.Document.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends MongoRepository<Users, String> {

}
