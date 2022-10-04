package com.software.voiceapp.Voiceapp.Repository;

import com.software.voiceapp.Voiceapp.Document.TextCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextCollectionRepository extends MongoRepository<TextCollection, String> {
    List<TextCollection> findByUploadedBy(String emailId);
}
