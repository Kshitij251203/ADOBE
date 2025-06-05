package com.adobe.StudentManagement.repository;


import com.adobe.StudentManagement.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {
}
