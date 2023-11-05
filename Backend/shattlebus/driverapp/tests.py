from django.test import Client, TestCase

from circular.models import CircularBus
from circular.models import Location
import datetime

class RetrieveCircularBusTest(TestCase):
    def setup(self):
        pass

    def tearDown(self):
        CircularBus.objects.all().delete()
        Location.objects.all().delete()

    def test_get_circular_bus(self):
        # Given
        location = Location.objects.create(latitude=37.1111, longitude=126.1111)
        location.save()
        circular_bus = CircularBus.objects.create(
            license_plate="16가1616", location_id=location.id, is_running=True, is_tracked=True
        )
        circular_bus.save()


        # When
        client = Client()
        response = client.get('/driverapp/circular/buses?license_plate=16가1616', secure=True)
        data = response.json()

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(CircularBus.objects.count(), 1)
        self.assertEqual(Location.objects.count(), 1)
        self.assertEqual(data["license_plate"], "16가1616")
        self.assertEqual(data["latitude"], 37.1111)
        self.assertEqual(data["longitude"], 126.1111)
        self.assertEqual(data["is_running"], True)
        self.assertEqual(data["is_tracked"], True)
        self.assertIsNotNone(data["location_updated_at"])

        print("\n- 내 순환 셔틀 버스 정보 얻기 success")

class UpdateCircularBusLocationTest(TestCase):

    def setup(self):
        pass

    def tearDown(self):
        CircularBus.objects.all().delete()
        Location.objects.all().delete()

    def test_put_circular_bus_location(self):
        # Given
        location = Location.objects.create(latitude=37.1111, longitude=126.1111)
        location.save()
        circular_bus = CircularBus.objects.create(
            license_plate="16가1616", location_id=location.id, is_running=True, is_tracked=True
        )
        circular_bus.save()
        original_updated_at_kr = location.updated_at+datetime.timedelta(hours=9)

        # When
        client = Client()
        response = client.put(
            '/driverapp/update?license_plate=16가1616&latitude=27.1111&longitude=136.1111',
            secure=True
        )
        data = response.json()

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(CircularBus.objects.count(), 1)
        self.assertEqual(Location.objects.count(), 1)
        self.assertEqual(data["license_plate"], "16가1616")
        self.assertEqual(data["latitude"], 27.1111)
        self.assertEqual(data["longitude"], 136.1111)
        self.assertEqual(data["is_running"], True)
        self.assertEqual(data["is_tracked"], True)
        self.assertNotEquals(data["location_updated_at"], str(original_updated_at_kr))

        print("\n- 순환 셔틀 버스 location update success")

