from flask import Flask
import utils_knx
from knxnet import knxnet
import sys

app = Flask(__name__)

#gateway_ip = "127.1.0.1"
gateway_port = 3671
client_port = 3672

choices_action = ["blind_open","blind_close","blind_control","valve_control","blind_read","valve_read"]

# command_name : (x,data,data_size)
dict_write = {
"blind_open":(1,1,1),
"blind_close":(1,0,1),
"blind_control":(3,0,2),
"valve_control":(0,0,2)
}

dict_read = {
"blind_read":(4,1,1),
"valve_read":(0,1,1)
}


@app.route("/")
def help_page():
    return "USE REST STYLE : /ip/floor/bloc/action(/data)</br>ip : ?.?.?.?</br>floor : Int</br>bloc : Int</br>action : [blind_open,blind_close,blind_control,valve_control,blind_read,valve_read]"

@app.route("/<string:gateway_ip>/<floor>/<bloc>/<action>")
def req(gateway_ip,floor,bloc,action):
    print(gateway_ip,floor,bloc,action)
    if "control" in action :
        return "data needed"
    elif action in dict_write.keys() :
        com = dict_write[action]
        data = com[1]
        data_size = com[2]

        dest_addr_group = knxnet.GroupAddress.from_str(str(com[0])+"/"+str(floor)+"/"+str(bloc))
        return utils_knx.write("127.0.0.1",gateway_ip,client_port,gateway_port,dest_addr_group,data,data_size)
    elif action in dict_read.keys() :
        com = dict_read[action]
        data = com[1]
        data_size = com[2]

        dest_addr_group = knxnet.GroupAddress.from_str(str(com[0])+"/"+str(floor)+"/"+str(bloc))
        return utils_knx.read("127.0.0.1",gateway_ip,client_port,gateway_port,dest_addr_group,data,data_size)
    else : 
        return "unknown command"

@app.route("/<string:gateway_ip>/<floor>/<bloc>/<action>/<int:data>")
def req_command(gateway_ip,floor,bloc,action,data):
    if "control" not in action :
        req(floor,bloc,action)
        return "data need useless for that command"
    elif action in dict_write.keys() :
        com = dict_write[action]
        data_size = com[2]

        dest_addr_group = knxnet.GroupAddress.from_str(str(com[0])+"/"+str(floor)+"/"+str(bloc))
        return utils_knx.write("127.0.0.1",gateway_ip,client_port,gateway_port,dest_addr_group,data,data_size)
    else : 
        return "unknown command"


if __name__ == "__main__":
    app.run()
