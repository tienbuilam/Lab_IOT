import random
import time

from adafruit_mqtt import Adafruit_MQTT

mqtt = Adafruit_MQTT()
time.sleep(5)
client = mqtt.client
sensor_type = 0
while True:
    print("Publishing random data...")
    if sensor_type == 0:
        print("Temperature...")
        temp = random.randint(15, 60)
        client.publish("cambien1", temp)
        sensor_type = 1
    elif sensor_type == 1:
        print("Light...")
        light = random.randint(0, 500)
        client.publish("cambien2", light)
        sensor_type = 2
    elif sensor_type == 2:
        print("Humidity...")
        humi = random.randint(0, 100)
        client.publish("cambien3", humi)
        sensor_type = 0
    time.sleep(10)