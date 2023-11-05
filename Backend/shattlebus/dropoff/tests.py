from django.test import Client, TestCase

from .models import CurrentLine
import datetime

class RetrieveWaitingTimeTest(TestCase):

    def setup(self):
        pass

    def tearDown(self):
        CurrentLine.objects.filter(num_people_waiting=1616).delete()


    def test_get_num_waiting_people_for_shuttle_when_executing(self):
        # Given
        current_line = CurrentLine.objects.create(num_people_waiting=1616, is_executing=True)
        current_line.save()

        # When
        client = Client()
        response = client.get('/dropoff/waiting-time', secure=True)
        data = response.json()

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(CurrentLine.objects.count(), 1)
        self.assertEqual(data["num_waiting_people"], CurrentLine.objects.all()[0].num_people_waiting)
        self.assertEqual(data["num_waiting_people"], 1616)
        print("\n- is_executing=True 일 때 하교 셔틀 대기 시간 얻기 success")

    def test_get_num_waiting_people_for_shuttle_when_not_executing(self):
        # Given
        current_line = CurrentLine.objects.create(num_people_waiting=6161, is_executing=False)
        current_line.save()

        # When
        client = Client()
        response = client.get('/dropoff/waiting-time', secure=True)
        data = response.json()

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(CurrentLine.objects.count(), 1)
        self.assertEqual(data["num_waiting_people"], CurrentLine.objects.all()[0].num_people_waiting)
        self.assertEqual(data["num_waiting_people"], 6161)
        self.assertEqual(data["waiting_time"], -1)
        self.assertEqual(data["num_needed_bus"], -1)
        print("\n- is_executing=False 일 때 하교 셔틀 대기 시간 얻기 success")

    def test_get_waiting_time_for_shuttle_including_no_shuttle_time(self):
        # Given
        current_line = CurrentLine.objects.create(num_people_waiting=1166, is_executing=True)
        current_line.save()

        # When
        client = Client()
        response = client.get('/dropoff/waiting-time', secure=True)
        data = response.json()

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(CurrentLine.objects.count(), 1)
        self.assertEqual(data["num_waiting_people"], CurrentLine.objects.all()[0].num_people_waiting)
        self.assertEqual(data["num_waiting_people"], 1166)

        now_kr = datetime.datetime.now(datetime.timezone(datetime.timedelta(hours=9)))
        day_of_week_today = now_kr.weekday()
        hr_today = now_kr.hour
        is_weekend = day_of_week_today > 4
        is_not_weekend_but_after_19 = day_of_week_today <= 4 and hr_today >= 19

        if is_weekend:
            print("오늘은 주말")
            self.assertEqual(data["waiting_time"], -1)
            self.assertEqual(data["num_needed_bus"], -1)
        elif is_not_weekend_but_after_19:
            print("지금은 평일 19시 이후")
            self.assertEqual(data["waiting_time"], -1)
            self.assertEqual(data["num_needed_bus"], -1)
        else:
            self.assertNotEquals(data["waiting_time"], -1)
            self.assertNotEquals(data["num_needed_bus"], -1)
        print("\n- 셔틀이 없는 시간에 하교 셔틀 대기 시간 얻기 success")
