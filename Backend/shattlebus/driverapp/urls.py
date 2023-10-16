from django.urls import path
from . import views

urlpatterns = [
    path('circular/buses/', views.RetrieveCircularBusView.as_view(), name='circular-bus-detail'),
]
