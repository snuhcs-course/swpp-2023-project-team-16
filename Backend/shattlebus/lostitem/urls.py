from django.urls import path
from . import views

urlpatterns = [
    path('list/', views.LostItemView.as_view(), name='lostitem-list'),
    path('', views.LostItemView.as_view(), name='lostitem-search'),
]
