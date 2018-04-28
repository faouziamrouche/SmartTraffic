import threading, requests, time, os, random

class ArduinoListener(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self, name="arduino_receiver")
    
    def run(self):
        os.environ['NO_PROXY'] = '127.0.0.1'
        cpt = 0
        while True:
            time.sleep(5)
            params = {'cpt': random.choice(["Temperature", "Humidity", "Number"]) + " = " + str(cpt)}
            cpt = cpt + 1
            res = requests.get(url="http://127.0.0.1:5000/sensing", params=params)
            print(res.content)
            

arduino = ArduinoListener()
threads = [arduino.start()]