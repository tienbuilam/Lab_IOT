import random
import time

from adafruit_mqtt import Adafruit_MQTT
from ai import maskDetection
from uart import readSerial

mqtt = Adafruit_MQTT()
time.sleep(5)

client = mqtt.client
sensor_type = 0
prev_result = ""

while True:
    readSerial(client)
    # ai model
    ai_result = maskDetection()
    if ai_result != prev_result:
        client.publish("ai", ai_result)
        prev_result = ai_result
    time.sleep(1)