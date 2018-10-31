# IOT_LARRIEU_MARTINEZ



Pour utiliser l'application il faut préalablement changer l'IP du serveur principal dans ressources/strings de l'application.

Et changer, si besoin, les IP des serveurs KNX et Z-WAVE dans le serveur principal (main_rest_server.py).



 ## Fonctionnement de l'application :

L'application cherche des beacons à proximité. Si elle en trouve, elle utilise le plus proche. Chaque beacon et liée à une pièce (beacon 3 -> room 1, beacon 13 -> room 2). 

L'application fait ensuite une requête au serveur principal pour avoir la liste des devices dans cette pièce et les affiches.

Lorsqu'on clique sur un device l'application fait une requête pour obtenir la liste des actions possible sur ce device.



## Solution de sécurité :

L'objectif est de garantir qu'une personne soit obligé de se trouver dans une pièce pour contrôler les devices de cette pièce.

Pour éviter la capture des UUIDs des beacons il ne faudrait pas les stocker en clair dans l'application en clair mais dans la DB du serveur principal. Et utiliser HTTPS car un attaquant pourrait les sniffer. **Cependant si un attaquant à un accès physique aux beacons il peut sniffer le trafic Bluetooth qui n'est pas encrypté.** On ne peut donc pas éviter l'hijacking des identifiants des beacons, on ne peut pas se fier uniquement à eux.

Une solution complémentaire serait de créer des **comptes utilisateur** (login,password) avec des accès restreint à certaine pièces (beacons). Ainsi même en détournant les ids des beacons l'attaquant ne pourra pas utiliser des devices. Cette solution requiert d'utiliser HTTPS pour la même raison de précédemment et des tockens afin de créer des sessions utilisateur.