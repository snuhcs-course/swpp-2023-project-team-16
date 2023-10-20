from django.db import models

# Create your models here.

# 예시
from django.db import models

# Create your models here.


class LostItem(models.Model):
    name = models.CharField(max_length=20)
    description = models.CharField(max_length=255)
    bus_type = models.PositiveIntegerField()
    is_returned = models.BooleanField()
    is_discarded = models.BooleanField()
    acquired_at = models.DateTimeField()
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
