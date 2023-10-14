from django.urls import path
from . import views

urlpatterns = [
    # path('circular/buses/<uuid:circularBus_id>/', views.RetrieveCircularBusView.as_view(), name='circular-bus-detail'),
    path('circular/buses/', views.RetrieveCircularBusView.as_view(), name='circular-bus-detail'),
]
