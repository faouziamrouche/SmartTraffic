from flask import Flask, render_template, request, jsonify
import serial, threading

def sendData(data, node='/dev/tty.usbserial', bauds=9600):
    ser = serial.Serial(node, bauds)
    ser.write(data)

currentSpeed =  0
currentMesg = ""
arduinoMsg = ""
currentTemperature = 0
currentHumidity = 0
currentNumber = 0

app = Flask(__name__)


@app.route('/')
def index():
    return render_template("index.html")


@app.route('/modifysign', methods=["POST"])
def modifySign():
    global currentSpeed
    global currentMesg
    data = request.get_json()
    currentSpeed = data["panel_name"]
    currentMesg = data["panel_msg"]
    print(currentSpeed)
    print(currentMesg)
    res = {'res': "Hello there"}
    return jsonify(res)
    #sendData({"signType":signType, "other":other})


@app.route("/info")
def info():
    global currentSpeed
    global currentMesg
    data = {'vitesse':currentSpeed, 'info':currentMesg}
    return jsonify(data), 200, {'ContentType': "application/json"}


@app.route("/sensing")
def sensing():
    global arduinoMsg
    global currentTemperature
    global currentHumidity
    global currentNumber
    arduinoMsg = request.args.get("cpt")
    if "Temperature" in arduinoMsg:
        currentTemperature = int(arduinoMsg.split("=")[1].strip())
    if "Humidity" in arduinoMsg:
        currentHumidity = int(arduinoMsg.split("=")[1].strip())
    if "Number" in arduinoMsg:
        currentNumber = int(arduinoMsg.split("=")[1].strip())
    return jsonify({"info":"Sensor value received"}), 200, {'ContentType': "application/json"}


@app.route("/monitoring")
def monitoring():
    global arduinoMsg
    global currentHumidity
    global currentNumber
    global currentTemperature
    data = {'temperature': currentTemperature, "humidity": currentHumidity, "number": currentNumber}
    return jsonify(data), 200, {'ContentType': "application/json"}

if __name__=="__main__":
    app.run(debug=True)
