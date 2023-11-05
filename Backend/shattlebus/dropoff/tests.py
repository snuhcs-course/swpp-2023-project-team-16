from django.test import Client, TestCase

from .models import CurrentLine

class RetrieveWaitingTimeTest(TestCase):

    def setup(self):
        pass

    def tearDown(self):
        CurrentLine.objects.filter(num_people_waiting=1616).delete()


    def test_get_waiting_time_for_shuttle_when_executing(self):
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





