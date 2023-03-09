import pymongo
client = pymongo.MongoClient('mongodb://a19angavimar:Grup5@labs.inspedralbes.cat:7010/?authMechanism=DEFAULT&authSource=DAMA_Grup5&tls=false' + "?retryWrites=true&w=majority&useUnifiedTopology=true")
db = client["DAMA_Grup5"]

import pandas as pd
collection = db["consumers"]
data = pd.DataFrame(list(collection.find()))

import matplotlib.pyplot as plt

# procesar datos y generar gráfica
data.plot(kind='bar', x='name', y='kills')
plt.title('SCORES')
plt.xlabel('name')
plt.ylabel('kills')

# guardar gráfica como archivo HTML
plt.savefig('grafica.html')