# urls.py

from django.urls import path
from . import views

urlpatterns = [
    path("students/", views.student_list, name="student_list"),
    path("students/create", views.student_list, name="student_list"),
    path("students/<str:student_id>/", views.student_detail, name="student_detail"),

    path('subjects/', views.get_all_subjects, name='get_all_subjects'),
    path('subjects/create/', views.create_subject, name='create_subject'),
    path('subjects/<str:subject_id>/', views.get_subject_by_id, name='get_subject_by_id'),
    path('subjects/<str:subject_id>/update/', views.update_subject, name='update_subject'),

    # Semester routes
    path('semester/', views.get_all_semesters, name='get_all_semesters'),
    path('semester/create/', views.create_semester, name='create_semester'),
    path('semester/<str:semester_id>/', views.get_semester_by_id, name='get_semester_by_id'),
    path('semester/<str:semester_id>/update/', views.update_semester, name='update_semester'),

    # marks routes
    path('marks/', views.marks_list, name='marks_list'),
    path('marks/<str:student_id>/', views.marks_detail, name='marks_detail'),

    #cgpa calculation
    path('cgpa/<str:student_id>/', views.calculate_cgpa, name='calculate_cgpa'),
]
