import datetime

from django.shortcuts import get_object_or_404
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

        # 해당하는 CircularBus 없으면 404
        my_bus = get_object_or_404(CircularBus, license_plate=license_plate)
        location_id = my_bus.location_id
        # 해당하는 Location 없으면 404
        location = get_object_or_404(Location, id=location_id)

        response = {
            "id": my_bus.id,
            "license_plate": my_bus.license_plate,
            "location_id": my_bus.location_id,
            "latitude": float(location.latitude),
            "longitude": float(location.longitude),
            "is_running": my_bus.is_running,
            "is_tracked": my_bus.is_tracked,
            "location_updated_at": str(location.updated_at+datetime.timedelta(hours=9))
        }

        return HttpResponse(json.dumps(response, ensure_ascii=False, indent=1))


@method_decorator(csrf_exempt, name='dispatch')
class UpdateCircularBusLocationView(View):

    def put(self, request):
        license_plate = request.GET['license_plate']
        latitude = request.GET['latitude']
        longitude = request.GET['longitude']

        # 해당하는 CircularBus 없으면 404
        my_bus = get_object_or_404(CircularBus, license_plate=license_plate)
        location_id = my_bus.location_id
        # 해당하는 Location 없으면 404
        location = get_object_or_404(Location, id=location_id)

        location.latitude = latitude
        location.longitude = longitude
        location.save()

        response = {
            "id": my_bus.id,
            "license_plate": my_bus.license_plate,
            "location_id": my_bus.location_id,
            "latitude": float(location.latitude),
            "longitude": float(location.longitude),
            "is_running": my_bus.is_running,
            "is_tracked": my_bus.is_tracked,
            "location_updated_at": str(location.updated_at+datetime.timedelta(hours=9))
        }

        return HttpResponse(json.dumps(response, ensure_ascii=False, indent=1))
