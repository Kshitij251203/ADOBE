
package com.adobe.StudentManagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "semesters")
public class Semester {

    @Id
    private String id;

    private int semesterNumber;
    private String name;
    @DBRef
    private List<Subject> subjects;
}
