from django.urls import path
from . import views

urlpatterns = [
    path('waiting-time', views.RetrieveWaitingTimeView.as_view(), name='waiting-time'),
    path('update-waiting', views.UpdateWaitingPeopleView.as_view(), name='update-waiting'),
]
