package com.meet.myFirstProject.repository;

import com.meet.myFirstProject.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository //Not Compulsory
public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId>
{

}
