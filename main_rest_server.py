from flask import Flask
import sqlite3
import requests

ip_knx_server = "127.0.0.1"
port_knx_server = 5000

# TODO : PUT CORRECT INFO
ip_zwave_server = "192.168.1.2"
port_zwave_server = 5000

# TODO : COMPLET ZW
actions = {
    "blind" : "blind_open,blind_close,blind_control,blind_read",
    "valve" :  "valve_control,valve_read",
    "light" : "light_level"
}


app = Flask(__name__)

@app.route("/")
def help_page():
    return """OK"""

@app.route("/devices/<int:room>")
def get_devices(room):
    if room == 0 :
        return ""
    else :
        conn = sqlite3.connect('iot.db')
        c = conn.cursor()

        # Try request to db
        try :

            devices_iterator = c.execute("SELECT name FROM devices WHERE room="+str(int(room)))

            if devices_iterator == None :
                return "unknown room"
            else :
                r = next(devices_iterator)[0]
                for row in devices_iterator:
                    r += "," + row[0]
                return r
            
        # If errors in db request
        except :
            return "request error"

@app.route("/actions/<name>")
def get_actions(name):
    conn = sqlite3.connect('iot.db')
    c = conn.cursor()

    # Try request to db
    try :

        device_type = next(c.execute('SELECT type FROM devices WHERE name="'+str(name)+'"'))[0]

        print(device_type)
        if device_type == None :
            return "device not found"
        else :
            return actions[device_type]
        
    # If errors in db request
    except :
        return "request error"

@app.route("/<string:name>/<string:action>")
def action(name,action):
    conn = sqlite3.connect('iot.db')
    c = conn.cursor()

    # Try request to db
    try :

        techno = next(c.execute('SELECT techno FROM devices WHERE name="'+name+'"'))[0]

        if techno == "knx":
            knx_info = next(c.execute('SELECT ip,floor,bloc FROM knx WHERE name="'+name+'"'))

            return "RETURN : " + req.text
        elif techno == "zwave":
            # TODO : COMPLET
            z_wave_info = next(c.execute('SELECT url FROM zwave WHERE name="'+name+'"'))
            req = requests.get("http://" + ip_zwave_server + ":5000" + str(z_wave_info[0]))
            return "Return : " + req.text
        else :
            return "error"
        
    # If errors in db request
    except :
        return "request error"
        

@app.route("/<string:name>/<string:action>/<int:data>")
def action_data(name,action,data):
    if data < 0 or data >255 :
        return "incorrect value"
    
    conn = sqlite3.connect('iot.db')
    c = conn.cursor()

    # Try request to db
    try :

        techno = next(c.execute('SELECT techno FROM devices WHERE name="'+name+'"'))[0]

        if techno == "knx":
            knx_info = next(c.execute('SELECT ip,floor,bloc FROM knx WHERE name="'+name+'"'))

            req = requests.get("http://"+ip_knx_server+":5000/"+knx_info[0]+"/"+str(knx_info[1])+"/"+str(knx_info[2])+"/"+action+"/"+str(data))

            return "RETURN : " + req.text
        elif techno == "zwave":
            # TODO : COMPLET
            z_wave_info = next(c.execute('SELECT adresse FROM zwave WHERE name="' + name + '"'))
            req = requests.get("http://" + ip_zwave_server + ":5000/dimmers" + str(z_wave_info[0]) + "/" + str(data))

            return "Return : " + req.text
        else :
            return "error"
        
    # If errors in db request
    except :
        return "request error"


if __name__ == "__main__" :
    app.run(host="0.0.0.0")
