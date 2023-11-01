from django.views import View
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator

import json

from circular.models import CircularBus
from circular.models import Location


# Create your views here.

class RetrieveCircularBusView(View):

    def get(self, request):
        license_plate = request.GET['license_plate']
        my_bus = CircularBus.objects.filter(license_plate=license_plate).values()[0]

        location_id = my_bus['location_id']
        location = Location.objects.filter(id=location_id).values()[0]

        response = {"id": my_bus['id'],
                    "license_plate": my_bus['license_plate'],
                    "location_id": my_bus['location_id'],
                    "latitude": float(location['latitude']),
                    "longitude": float(location['longitude']),
                    "is_running": my_bus['is_running'],
                    "is_tracked": my_bus['is_tracked'],
                    "updated_at": str(my_bus['updated_at'])
                    }

        return HttpResponse(json.dumps(response))


@method_decorator(csrf_exempt, name='dispatch')
class UpdateCircularBusLocationView(View):

    def put(self, request):
        license_plate = request.GET['license_plate']
        latitude = request.GET['latitude']
        longitude = request.GET['longitude']

        location_data = Location(latitude=latitude, longitude=longitude)
        location_data.save()

        CircularBus.objects\
            .filter(license_plate=license_plate)\
            .update(location=location_data, is_running=True, is_tracked=True)

        return HttpResponse()
