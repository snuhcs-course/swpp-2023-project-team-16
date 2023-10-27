from django.contrib import admin
from .models import CurrentLine, Congestion

# Register your models here.
admin.site.register(CurrentLine)
admin.site.register(Congestion)