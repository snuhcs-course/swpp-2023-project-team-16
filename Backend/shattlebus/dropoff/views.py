import datetime

from django.views import View
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator
from django.core.exceptions import ValidationError

import json

from .models import CurrentLine

HOUR_SHUTTLE_START = 7
HOUR_SHUTTLE_END = 19
# estimated maximum numbers of people can board on a bus
# this can be revised
MAX_NUM_OF_PEOPLE = 40
FRIDAY = 4


class RetrieveWaitingTimeView(View):

    def get(self, request):
        current_line = CurrentLine.objects.all()[0]
        num_people_waiting = current_line.num_people_waiting
        updated_at = current_line.updated_at
        updated_at_kr = updated_at + datetime.timedelta(hours=9)

        try:
            if not current_line.is_executing:
                raise NoShuttleException

            waiting_data = self.get_waiting_data(num_people_waiting, updated_at_kr)
        except NoShuttleException:
            waiting_data = {
                "num_waiting_people": num_people_waiting,
                "waiting_time": -1,
                "num_needed_bus": -1,
                "updated_at": str(updated_at_kr)
            }

        return HttpResponse(json.dumps(waiting_data, ensure_ascii=False, indent=1), content_type="application/json")

    def get_waiting_data(self, num_people_waiting, updated_at_kr):
        num_needed_bus = (num_people_waiting // MAX_NUM_OF_PEOPLE) + 1
        waiting_time = self.get_waiting_time(num_needed_bus)
        waiting_data = {
            "num_waiting_people": num_people_waiting,
            "waiting_time": waiting_time,
            "num_needed_bus": num_needed_bus,
            "updated_at": str(updated_at_kr)
        }

        return waiting_data

    def get_waiting_time(self, num_needed_bus):
        now_kr = datetime.datetime.now(datetime.timezone(datetime.timedelta(hours=9)))
        hour = now_kr.hour
        minute = now_kr.minute
        day = now_kr.weekday()
        if day > FRIDAY:
            raise NoShuttleException

        waiting_time = 0

        for i in range(num_needed_bus):
            hour_next_bus, minute_next_bus = self.get_time_for_next_bus(hour, minute)
            waiting_time += ((hour_next_bus - hour) * 60 + (minute_next_bus - minute))
            hour, minute = hour_next_bus, minute_next_bus

        return waiting_time

    def get_time_for_next_bus(self, hour, minute):
        if hour >= HOUR_SHUTTLE_END:
            raise NoShuttleException
        elif hour < HOUR_SHUTTLE_START:
            hour_next_bus = HOUR_SHUTTLE_START
            minute_next_bus = 0
        elif 7 <= hour < 8:
            hour_next_bus = 7
            minute_next_bus = ((minute // 15) + 1) * 15
        elif 11 <= hour < 15:
            hour_next_bus = hour
            minute_next_bus = ((minute // 10) + 1) * 10
        else:
            hour_next_bus = hour
            minute_next_bus = ((minute // 5) + 1) * 5

        if minute_next_bus >= 60:
            hour_next_bus += 1
            minute_next_bus -= 60

        return hour_next_bus, minute_next_bus


@method_decorator(csrf_exempt, name='dispatch')
class UpdateWaitingPeopleView(View):

    def put(self, request):
        request = json.loads(request.body)
        waiting_people = request[('waiting_people')]

        try:
            current_line = CurrentLine.objects.all()[0]
            current_line.num_people_waiting = waiting_people
            current_line.is_executing = True
            current_line.save()

            response = {
                "num_people_waiting": current_line.num_people_waiting,
                "is_executing": current_line.is_executing,
                "updated_at": str(current_line.updated_at + datetime.timedelta(hours=9))
            }
        except IndexError:
            current_line = CurrentLine(num_people_waiting=waiting_people, is_executing=True)
            current_line.save()

            response = {
                "num_people_waiting": current_line.num_people_waiting,
                "is_executing": current_line.is_executing,
                "updated_at": str(current_line.updated_at + datetime.timedelta(hours=9))
            }
        except ValueError:
            response = HttpResponse()
            response.status_code = 400
            response.reason_phrase = "Invalid value"

            return response

        return HttpResponse(json.dumps(response, ensure_ascii=False, indent=1), content_type="application/json")


# Exception when there is no available shuttle
class NoShuttleException(Exception):
    pass
