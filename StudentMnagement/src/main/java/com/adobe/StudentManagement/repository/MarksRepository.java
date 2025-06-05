package com.adobe.StudentManagement.repository;

import com.adobe.StudentManagement.model.Marks;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MarksRepository extends MongoRepository<Marks, String> {
    List<Marks> findByStudentId(ObjectId studentObjId);

}
