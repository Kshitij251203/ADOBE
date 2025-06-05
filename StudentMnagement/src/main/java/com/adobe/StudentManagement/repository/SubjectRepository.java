package com.adobe.StudentManagement.repository;


import com.adobe.StudentManagement.model.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubjectRepository extends MongoRepository<Subject, String> {
    // Custom query methods (if needed) can go here
}
