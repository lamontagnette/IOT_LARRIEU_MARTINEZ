from knxnet import knxnet
import sys, argparse
import utils_knx


#exemple : python3 main.py -ip "127.1.0.0" -pcli 3672 -pgate 3671 -floor 4 -bloc 1 -a blind_open
#exemple : python3 main.py -ip "127.1.0.0" -pcli 3672 -pgate 3671 -floor 4 -bloc 1 -a blind_close
#exemple : python3 main.py -ip "127.1.0.0" -pcli 3672 -pgate 3671 -floor 4 -bloc 1 -a blind_read



# parser for command line exec
parser = argparse.ArgumentParser(description='KNX Commands')
parser.add_argument('-ip', type=str, nargs='+', required=True,help='ip')
parser.add_argument('-pcli', type=int, nargs='+', required=True,help='port client')
parser.add_argument('-pgate', type=int, nargs='+', required=True,help='port destination')
parser.add_argument('-floor', type=int, nargs='+', required=True,help='floor')
parser.add_argument('-bloc', type=int,nargs='+', required=True,help='bloc')
parser.add_argument('-a', type=str, nargs='+', required=True, choices=("blind_open","blind_close","blind_control","valve_control","blind_read","valve_read"),help='action')
parser.add_argument('-data', type=int, nargs='+', required=False,default=[0],choices=range(0, 256),help='optional data')

args = vars(parser.parse_args())

gateway_ip = args['ip'][0]#"127.1.0.0"
gateway_port = args['pgate'][0]#3671
client_port = args['pcli'][0]#3672
floor = args['floor'][0]
bloc = args['bloc'][0]
action = args['a'][0]
data = args['data'][0]

# command_name : (x,data,data_size)
dict_write = {
"blind_open":(1,1,1),
"blind_close":(1,0,1),
"blind_control":(3,data,2),
"valve_control":(0,data,2)
}

dict_read = {
"blind_read":(4,1,1),
"valve_read":(0,1,1)
}

try :
    com = dict_write[action]
    data = com[1]
    data_size = com[2]

    dest_addr_group = knxnet.GroupAddress.from_str(str(com[0])+"/"+str(floor)+"/"+str(bloc))

    utils_knx.write("127.0.0.1",gateway_ip,client_port,gateway_port,dest_addr_group,data,data_size)

except:
    com = dict_read[action]
    data = com[1]
    data_size = com[2]

    dest_addr_group = knxnet.GroupAddress.from_str(str(com[0])+"/"+str(floor)+"/"+str(bloc))
    utils_knx.read("127.0.0.1",gateway_ip,client_port,gateway_port,dest_addr_group,data,data_size)

