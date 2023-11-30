from django.contrib import admin
from .models import SingleCurrentLine
from solo.admin import SingletonModelAdmin



# Register your models here.
admin.site.register(SingleCurrentLine, SingletonModelAdmin)