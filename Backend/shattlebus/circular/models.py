from django.db import models

# Create your models here.


class Location(models.Model):
    type = models.PositiveIntegerField()
    latitude = models.DecimalField(max_digits=9, decimal_places=6)
    longitude = models.DecimalField(max_digits=9, decimal_places=6)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)


class CircularBus(models.Model):
    license_plate = models.CharField(max_length=20)
    location = models.ForeignKey(Location, on_delete=models.CASCADE)
    is_running = models.BooleanField()
    is_tracked = models.BooleanField()
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)


class CircularBusStop(models.Model):
    name = models.CharField(max_length=255)
    location = models.ForeignKey(Location, on_delete=models.CASCADE)




