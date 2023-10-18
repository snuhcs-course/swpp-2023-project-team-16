from django.urls import path
from . import views

urlpatterns = [
    path('', views.RetrieveTimeTableView.as_view(), name='timetable'),
]
