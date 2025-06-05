package com.adobe.StudentManagement.repository;

import com.adobe.StudentManagement.model.Semester;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SemesterRepository extends MongoRepository<Semester, String> {
}
