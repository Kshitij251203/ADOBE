from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from bson import ObjectId, json_util
import json

from .models import Student
from .models import Subject
from .models import Semester
from .models import Marks

# Helper to serialize ObjectId
def serialize(obj):
    return json.loads(json_util.dumps(obj))


@csrf_exempt
def student_list(request):
    if request.method == "GET":
        students = Student.get_all()
        return JsonResponse(serialize(students), safe=False)
    
    elif request.method == "POST":
        try:
            data = json.loads(request.body)
            result = Student.create(data)
            return JsonResponse({"message": "Student created", "id": str(result.inserted_id)}, status=201)
        except Exception as e:
            return JsonResponse({"error": str(e)}, status=400)


@csrf_exempt
def student_detail(request, student_id):
    if request.method == "GET":
        student = Student.get_by_id(student_id)
        if student:
            return JsonResponse(serialize(student), safe=False)
        return JsonResponse({"error": "Student not found"}, status=404)

    elif request.method == "PUT":
        try:
            data = json.loads(request.body)
            result = Student.update(student_id, data)
            if result.modified_count > 0:
                return JsonResponse({"message": "Student updated"})
            return JsonResponse({"message": "No changes made"}, status=200)
        except Exception as e:
            return JsonResponse({"error": str(e)}, status=400)


@csrf_exempt
def create_subject(request):
    if request.method == 'POST':
        data = json.loads(request.body)
        result = Subject.create(data)
        return JsonResponse({'inserted_id': str(result.inserted_id)}, status=201)

@csrf_exempt
def get_all_subjects(request):
    if request.method == 'GET':
        subjects = Subject.get_all()
        for subject in subjects:
            subject['_id'] = str(subject['_id'])
        return JsonResponse(subjects, safe=False)

@csrf_exempt
def get_subject_by_id(request, subject_id):
    if request.method == 'GET':
        subject = Subject.get_by_id(subject_id)
        if subject:
            subject['_id'] = str(subject['_id'])
            return JsonResponse(subject, safe=False)
        return JsonResponse({'error': 'Subject not found'}, status=404)

@csrf_exempt
def update_subject(request, subject_id):
    if request.method == 'PUT':
        data = json.loads(request.body)
        result = Subject.update(subject_id, data)
        if result.modified_count > 0:
            return JsonResponse({'message': 'Subject updated'})
        return JsonResponse({'message': 'No changes made'}, status=200)
from django.http import JsonResponse, HttpResponseNotAllowed

@csrf_exempt
def create_semester(request):
    if request.method == 'POST':
        try:
            data = json.loads(request.body)
            result = Semester.create(data)
            return JsonResponse({'inserted_id': str(result.inserted_id)}, status=201)
        except ValueError as ve:
            return JsonResponse({'error': str(ve)}, status=400)
        except Exception as e:
            return JsonResponse({'error': 'Invalid data or server error'}, status=400)
    else:
        return HttpResponseNotAllowed(['POST'])


@csrf_exempt
def get_all_semesters(request):
    if request.method == 'GET':
        semesters = Semester.get_all()
        for sem in semesters:
            sem['_id'] = str(sem['_id'])
            sem['subjects'] = [str(s) for s in sem.get('subjects', [])]
        return JsonResponse(semesters, safe=False)
    else:
        return HttpResponseNotAllowed(['GET'])


@csrf_exempt
def get_semester_by_id(request, semester_id):
    if request.method == 'GET':
        try:
            semester = Semester.get_by_id(semester_id)
            if semester:
                semester['_id'] = str(semester['_id'])
                semester['subjects'] = [str(s) for s in semester.get('subjects', [])]
                return JsonResponse(semester, safe=False)
            return JsonResponse({'error': 'Semester not found'}, status=404)
        except Exception as e:
            return JsonResponse({'error': 'Invalid semester ID'}, status=400)
    else:
        return HttpResponseNotAllowed(['GET'])


@csrf_exempt
def update_semester(request, semester_id):
    if request.method == 'PUT':
        try:
            data = json.loads(request.body)
            result = Semester.update(semester_id, data)
            if result.modified_count > 0:
                return JsonResponse({'message': 'Semester updated'})
            return JsonResponse({'message': 'No changes made'}, status=200)
        except ValueError as ve:
            return JsonResponse({'error': str(ve)}, status=400)
        except Exception as e:
            return JsonResponse({'error': 'Invalid semester ID'}, status=400)
    else:
        return HttpResponseNotAllowed(['PUT'])

@csrf_exempt
def marks_list(request):
    if request.method == 'GET':
        marks = Marks.get_all()
        # Convert ObjectIds to strings for JSON
        for mark in marks:
            mark['_id'] = str(mark['_id'])
            mark['studentId'] = str(mark['studentId'])
            mark['subjectId'] = str(mark['subjectId'])
        return JsonResponse(marks, safe=False)

    elif request.method == 'POST':
        try:
            data = json.loads(request.body)
            result = Marks.create(data)
            return JsonResponse({'inserted_id': str(result.inserted_id)}, status=201)
        except ValueError as ve:
            return JsonResponse({'error': str(ve)}, status=400)
        except Exception as e:
            return JsonResponse({'error': 'Invalid data or server error'}, status=400)

    else:
        return HttpResponseNotAllowed(['GET', 'POST'])

@csrf_exempt
def marks_detail(request, student_id):
    if request.method == 'GET':
        try:
            # Find all marks with the given studentId
            marks = Marks.get_by_id(student_id)
            marks_list = []
            for mark in marks:
                mark['_id'] = str(mark['_id'])
                mark['studentId'] = str(mark['studentId'])
                mark['subjectId'] = str(mark['subjectId'])
                marks_list.append(mark)
            
            if marks_list:
                return JsonResponse(marks_list, safe=False)
            else:
                return JsonResponse({'error': 'No marks found for this student'}, status=404)
        except Exception:
            return JsonResponse({'error': 'Invalid student ID'}, status=400)
    else:
        return HttpResponseNotAllowed(['GET'])


from django.http import JsonResponse
from bson import ObjectId
from .models import Marks, Subject

def calculate_cgpa(request, student_id):
    if request.method == 'GET':
        try:
            # Fetch all marks for the student
            marks_entries = Marks.get_by_id(student_id)
            if not marks_entries:
                return JsonResponse({'error': 'No marks found for this student'}, status=404)

            total_weighted_points = 0
            total_credits = 0

            for entry in marks_entries:
                subject_id = entry['subjectId']
                subject = Subject.get_by_id(str(subject_id))
                if subject:
                    grade_points = convert_marks_to_grade_points(entry['marks'])
                    credits = subject.get('credits', 0)
                    total_weighted_points += grade_points * credits
                    total_credits += credits

            if total_credits == 0:
                return JsonResponse({'error': 'Total credits cannot be zero'}, status=400)

            cgpa = total_weighted_points / total_credits
            return JsonResponse({'cgpa': round(cgpa, 2)})

        except Exception as e:
            return JsonResponse({'error': str(e)}, status=500)

    else:
        return JsonResponse({'error': 'Invalid HTTP method'}, status=405)

def convert_marks_to_grade_points(marks):
    if marks >= 90:
        return 10
    elif marks >= 80:
        return 9
    elif marks >= 70:
        return 8
    elif marks >= 60:
        return 7
    elif marks >= 50:
        return 6
    elif marks >= 40:
        return 5
    else:
        return 0
