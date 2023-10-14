from django.urls import path
from . import views

urlpatterns = [
    path('', views.RetrieveTimetableView.as_view(), name='timetable'),
]
