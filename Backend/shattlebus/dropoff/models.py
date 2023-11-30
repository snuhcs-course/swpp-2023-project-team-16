from django.db import models
from solo.models import SingletonModel

# Create your models here.


class SingleCurrentLine(SingletonModel):
    num_people_waiting = models.PositiveIntegerField()
    is_executing = models.BooleanField()
    updated_at = models.DateTimeField(auto_now=True)

    class Meta:
        verbose_name = "Single Current Line"
