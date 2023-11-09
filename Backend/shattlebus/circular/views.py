from django.shortcuts import get_object_or_404
from django.views import View
from django.http import HttpResponse

import json
import datetime

from .models import Location
from .models import CircularBus

# Create your views here.


class RetrieveCircularBusesLocationView(View):

    def get(self, request):
        running_bus_list = CircularBus.objects.filter(is_running=True, is_tracked=True)
        response = {}

        num_buses = len(running_bus_list)
        response['num_buses_running'] = num_buses
        bus_lists = []
        location_updated_at_lists = []

        for i in range(num_buses):
            bus = running_bus_list[i]
            bus_location = get_object_or_404(Location, id=bus.location_id)
            location_updated_at_lists.append(str(bus_location.updated_at + datetime.timedelta(hours=9)))
            bus_data = {
                "id": bus.id,
                "license_plate": bus.license_plate,
                "latitude": float(bus_location.latitude),
                "longitude": float(bus_location.longitude),
                "is_running": bus.is_running,
                "is_tracked": bus.is_tracked,
                "location_updated_at": str(bus_location.updated_at + datetime.timedelta(hours=9))
            }

            bus_lists.append(bus_data)
            location_updated_at_lists.sort()

        response['buses'] = bus_lists
        response['latest_location_updated_at'] = location_updated_at_lists[-1]

        return HttpResponse(json.dumps(response, ensure_ascii=False, indent=1), content_type="application/json")
