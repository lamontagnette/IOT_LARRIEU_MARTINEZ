from flask import Flask
import sqlite3

ip_knx_server = "127.0.0.1"
port_knx_server = 5000

ip_zwave_server = "127.0.0.1"
port_zwave_server = 80


actions = {
    "blind" : "blind_open,blind_close,blind_control,blind_read",
    "valve" :  "valve_control,valve_read"
}

app = Flask(__name__)

@app.route("/")
def help_page():
    return """HELP
USE REST STYLE :
/ip/floor/bloc/action(/data)
"""

@app.route("/devices/<int:room>")
def get_devices(room):
    conn = sqlite3.connect('iot.db')
    c = conn.cursor()

    devices_iterator = c.execute("SELECT name FROM devices WHERE room="+str(int(room)))

    if devices_iterator == None :
        return "unknown room"
    else :
        r = next(devices_iterator)[0]
        for row in devices_iterator:
            r += "," + row[0]
        return r

@app.route("/actions/<name>")
def get_actions(name):
    conn = sqlite3.connect('iot.db')
    c = conn.cursor()

    device_type = next(c.execute('SELECT type FROM devices WHERE name="'+str(name)+'"'))[0]

    print(device_type)
    if device_type == None :
        return "device not found"
    else :
        return actions[device_type]


if __name__ == "__main__":
    app.run(host="0.0.0.0")
