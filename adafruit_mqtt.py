import sys
from Adafruit_IO import MQTTClient

class Adafruit_MQTT:
    AIO_FEED_IDs = ["nutnhan1", "nutnhan2"]
    AIO_USERNAME = "tienbuilam"
    AIO_KEY = "aio_zwuh99aYlzEsc3y4VTdHtSMTUPAE"

    def connected(self, client):
        print("Connected ...")
        for feed in self.AIO_FEED_IDs:
            client.subscribe(feed)

    def subscribe(self, client , userdata , mid , granted_qos):
        print("Subscribed...")

    def disconnected(self, client):
        print("Disconnected...")
        sys.exit (1)

    def message(self, client, feed_id, payload):
        print("Received: " + payload + " from " + feed_id)

    def __init__(self):
        client = MQTTClient(self.AIO_USERNAME , self.AIO_KEY)
        client.on_connect = self.connected
        client.on_disconnect = self.disconnected
        client.on_message = self.message
        client.on_subscribe = self.subscribe
        client.connect()
        client.loop_background()
        self.client = client

