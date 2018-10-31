# IOT_LARRIEU_MARTINEZ



Pour utiliser l'application il faut préalablement changer l'IP du serveur principal dans ressources/strings de l'application.

Et changer, si besoin, les IP des serveurs KNX et Z-WAVE dans le serveur principal (main_rest_server.py).

De plus, les nodes de la partie Z-WAVE doivent être de la façon suivante (voir table zwave dans la DB iot.db) :

![zwave_nodes_cap](D:\Documents\HES-SO\cours\iot\git\IOT_LARRIEU_MARTINEZ\zwave_nodes_cap.PNG)



 ## Fonctionnement de l'application :

L'application cherche des beacons à proximité. Si elle en trouve, elle utilise le plus proche. Chaque beacon et liée à une pièce (beacon 3 -> room 1, beacon 13 -> room 2). 

L'application fait ensuite une requête au serveur principal pour avoir la liste des devices dans cette pièce et les affiches.

Lorsqu'on clique sur un device l'application fait une requête pour obtenir la liste des actions possible sur ce device.



## Solution de sécurité :

L'objectif est de garantir qu'une personne soit obligée de se trouver dans une pièce pour contrôler les devices de cette pièce.

Pour éviter la capture des UUIDs des beacons il ne faudrait pas les stocker en clair dans l'application mais dans la DB du serveur principal et d'utiliser HTTPS car un attaquant pourrait les sniffer. **Cependant si un attaquant à un accès physique aux beacons il peut sniffer le trafic Bluetooth qui n'est pas encrypté.** On ne peut donc pas éviter l'hijacking des identifiants des beacons, on ne peut pas se fier uniquement à eux.

Une solution complémentaire serait de créer des **comptes utilisateur** (login,password) avec des accès restreint à certaines pièces (beacons). Ainsi même en détournant les ids des beacons l'attaquant ne pourra pas utiliser les devices. Cette solution requiert d'utiliser HTTPS pour la même raison que précédemment et des tockens afin de créer des sessions utilisateurs.