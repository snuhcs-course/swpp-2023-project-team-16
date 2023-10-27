from django.contrib import admin
from .models import Location, CircularBus, CircularBusStop

# Register your models here.
admin.site.register(Location)
admin.site.register(CircularBus)