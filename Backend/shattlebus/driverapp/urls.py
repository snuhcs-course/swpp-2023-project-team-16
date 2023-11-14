from django.urls import path
from . import views

urlpatterns = [
    path('bus', views.RetrieveCircularBusView.as_view(), name='circular-bus-detail'),
    path('bus/location', views.UpdateCircularBusLocationView.as_view(), name='update-bus-location'),
    path('bus/is_running', views.UpdateCircularBusIsRunningView.as_view(), name='update-bus-is-running'),
]
