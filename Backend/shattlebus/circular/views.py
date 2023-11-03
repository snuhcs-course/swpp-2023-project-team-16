from django.views import View
from django.http import HttpResponse

import json

from .models import Location
from .models import CircularBus

# Create your views here.


class UpdateCircularBusLocationView(View):

    def get(self, request):
        running_bus_list = CircularBus.objects.filter(is_running=True, is_tracked=True).values()
        response = {}

        num_buses = len(running_bus_list)
        response['num_buses_running'] = num_buses
        bus_lists = []

        for i in range(num_buses):
            bus = running_bus_list[i]
            bus_location = Location.objects.filter(id=bus['location_id']).values()[0]
            bus_data = {"id": bus['id'],
                        "license_plate": bus['license_plate'],
                        "latitude": float(bus_location['latitude']),
                        "longitude": float(bus_location['longitude']),
                        "is_running": bus['is_running'],
                        "is_tracked": bus['is_tracked']
                        }

            bus_lists.append(bus_data)

        response['buses'] = bus_lists

        return HttpResponse(json.dumps(response))
