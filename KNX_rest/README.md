# IOT
KNX Commands

optional arguments:
  -h, --help            show this help message and exit
  
  -ip IP [IP ...]       ip
  
  -pcli PCLI [PCLI ...] port client
  
  -pgate PGATE [PGATE ...] port destination
  
  
  -floor FLOOR [FLOOR ...] floor
  
  -bloc BLOC [BLOC ...] bloc
  
  -a {blind_open,blind_close,blind_control,valve_control,blind_read,valve_read} [{blind_open,blind_close,blind_control,valve_control,blind_read,valve_read} ...]
                        action
  
  -data 

EXEMPLE :

python3 main.py -ip "127.1.0.3" -pcli 3672 -pgate 3671 -floor 4 -bloc 2 -data 128 -a blind_control 

python3 main.py -ip "127.1.0.0" -pcli 3672 -pgate 3671 -floor 4 -bloc 1 -a blind_open

python3 main.py -ip "127.1.0.0" -pcli 3672 -pgate 3671 -floor 4 -bloc 1 -a blind_close

python3 main.py -ip "127.1.0.3" -pcli 3672 -pgate 3671 -floor 4 -bloc 2 -data 128 -a valve_control

python3 main.py -ip "127.1.0.0" -pcli 3672 -pgate 3671 -floor 4 -bloc 1 -a blind_read

