from django.urls import path
from . import views

urlpatterns = [
    path('location/', views.UpdateCircularBusLocationView.as_view(), name='update-location'),
]
