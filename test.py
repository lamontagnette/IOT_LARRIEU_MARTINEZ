import sqlite3

name = "blind11"

conn = sqlite3.connect('iot.db')
c = conn.cursor()

techno = next(c.execute('SELECT techno FROM devices WHERE name="'+name+'"'))[0]
 
r = []
for row in c.execute('SELECT actions FROM '+ techno +' WHERE name="'+name+'"'):
    r.append(row[0])
    
print(str(r))

