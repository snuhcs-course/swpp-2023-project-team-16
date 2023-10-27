from django.urls import path
from . import views

urlpatterns = [
    path('waiting-time/', views.RetrieveWaitingTimeView.as_view(), name='waiting-time'),
]
