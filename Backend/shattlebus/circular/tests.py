from django.test import Client, TestCase

from .models import CircularBus, Location

class RetrieveCircularBusesLocationTest(TestCase):

    def setup(self):
        pass

    def tearDown(self):
        CircularBus.objects.all().delete()
        Location.objects.all().delete()

    def test_get_circular_buses_location(self):
        # Given
        location_1 = Location.objects.create(latitude=11.1111, longitude=11.1111)
        location_1.save()
        circular_bus_1 = CircularBus.objects.create(
            license_plate="11가1111", location_id=location_1.id, is_running=True, is_tracked=True
        )
        circular_bus_1.save()

        location_2 = Location.objects.create(latitude=22.2222, longitude=22.2222)
        location_2.save()
        circular_bus_2 = CircularBus.objects.create(
            license_plate="22가2222", location_id=location_2.id, is_running=True, is_tracked=True
        )
        circular_bus_2.save()

        # When
        client = Client()
        response = client.get('/circular/location', secure=True)
        data = response.json()

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(CircularBus.objects.count(), 2)
        self.assertEqual(Location.objects.count(), 2)
        self.assertEqual(data["num_buses_running"], 2)
        self.assertEqual(len(data["buses"]), 2)
        self.assertEqual(data["buses"][0]["latitude"], 11.1111)
        self.assertEqual(data["buses"][0]["longitude"], 11.1111)
        self.assertEqual(data["buses"][1]["latitude"], 22.2222)
        self.assertEqual(data["buses"][1]["longitude"], 22.2222)

        print("\n- 순환 셔틀 버스 Location 리스트 얻기 success")




