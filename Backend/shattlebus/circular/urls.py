from django.urls import path
from . import views

urlpatterns = [
    path('location/', views.RetrieveCircularBusesLocationView.as_view(), name='retrieve-buses-location'),
]
