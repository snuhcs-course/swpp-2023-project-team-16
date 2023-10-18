from django.db import models

# Create your models here.


class CurrentLine(models.Model):
    num_people_waiting = models.PositiveIntegerField()
    is_executing = models.BooleanField()
    updated_at = models.DateTimeField(auto_now=True)


class Congestion(models.Model):
    day = models.PositiveIntegerField()
    time_slot_start = models.TimeField()
    time_slot_end = models.TimeField()
    average_people_waiting = models.PositiveIntegerField()
    updated_at = models.DateTimeField(auto_now=True)
