from django.db import models
from .db_connection import db
from bson import ObjectId

# Create your models here.

students_collection = db.students
marks_collection = db.marks
semester_collection = db.semesters
subjects_collection = db.subjects

#Student
class Student:
    @staticmethod
    def create(data):

        return students_collection.insert_one(data)

    @staticmethod
    def get_all():
        return list(students_collection.find())

    @staticmethod
    def get_by_id(student_id):
        return students_collection.find_one({"_id": ObjectId(student_id)})

    @staticmethod
    def update(student_id, update_data):
        return students_collection.update_one(
            {"_id": ObjectId(student_id)},
            {"$set": update_data}
        )

    @staticmethod
    def delete(student_id):
        return students_collection.delete_one({"_id": ObjectId(student_id)})
    
#Subject
class Subject:
    @staticmethod
    def create(data):
        return subjects_collection.insert_one(data)

    @staticmethod
    def get_all():
        return list(subjects_collection.find())

    @staticmethod
    def get_by_id(subject_id):
        return subjects_collection.find_one({"_id": ObjectId(subject_id)})

    @staticmethod
    def update(subject_id, update_data):
        return subjects_collection.update_one(
            {"_id": ObjectId(subject_id)},
            {"$set": update_data}
        )

    @staticmethod
    def delete(subject_id):
        return subjects_collection.delete_one({"_id": ObjectId(subject_id)})
    

#Semester

#Semester
class Semester:
    @staticmethod
    def _subjects_exist(subject_ids):
        # Filter out invalid IDs and check if all exist in subjects collection
        object_ids = [ObjectId(s) if isinstance(s, str) else s for s in subject_ids]
        count = subjects_collection.count_documents({"_id": {"$in": object_ids}})
        return count == len(object_ids)

    @staticmethod
    def create(data):
        subject_ids = data.get("subjects", [])
        if not Semester._subjects_exist(subject_ids):
            raise ValueError("One or more subjects do not exist.")
        
        # Convert subjects to ObjectId
        data["subjects"] = [ObjectId(s) if isinstance(s, str) else s for s in subject_ids]
        return semester_collection.insert_one(data)

    @staticmethod
    def get_all():
        return list(semester_collection.find())

    @staticmethod
    def get_by_id(semester_id):
        return semester_collection.find_one({"_id": ObjectId(semester_id)})

    @staticmethod
    def update(semester_id, update_data):
        if "subjects" in update_data:
            subject_ids = update_data["subjects"]
            if not Semester._subjects_exist(subject_ids):
                raise ValueError("One or more subjects do not exist.")
            update_data["subjects"] = [ObjectId(s) if isinstance(s, str) else s for s in subject_ids]

        return semester_collection.update_one(
            {"_id": ObjectId(semester_id)},
            {"$set": update_data}
        )
    
#marks

class Marks:
    @staticmethod
    def create(data):
        required_fields = ["studentId", "subjectId", "semester", "marks"]
        for field in required_fields:
            if field not in data:
                raise ValueError(f"Missing required field: {field}")

        data["studentId"] = ObjectId(data["studentId"]) if isinstance(data["studentId"], str) else data["studentId"]
        data["subjectId"] = ObjectId(data["subjectId"]) if isinstance(data["subjectId"], str) else data["subjectId"]

        return marks_collection.insert_one(data)

    @staticmethod
    def get_all():
        return list(marks_collection.find())

    @staticmethod
    def get_by_id(student_id):
        return marks_collection.find({"studentId": ObjectId(student_id)})

    @staticmethod
    def update(mark_id, update_data):
        if "studentId" in update_data:
            update_data["studentId"] = ObjectId(update_data["studentId"]) if isinstance(update_data["studentId"], str) else update_data["studentId"]
        if "subjectId" in update_data:
            update_data["subjectId"] = ObjectId(update_data["subjectId"]) if isinstance(update_data["subjectId"], str) else update_data["subjectId"]

        return marks_collection.update_one(
            {"_id": ObjectId(mark_id)},
            {"$set": update_data}
        )

    @staticmethod
    def delete(mark_id):
        return marks_collection.delete_one({"_id": ObjectId(mark_id)})

