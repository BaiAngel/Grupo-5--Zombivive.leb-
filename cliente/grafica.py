import pandas as pd
from pymongo import MongoClient

# Configuración de conexión a la base de datos de MongoDB
client = MongoClient('mongodb://a19angavimar:Grup5@labs.inspedralbes.cat:7010/?authMechanism=DEFAULT&authSource=DAMA_Grup5&tls=false' + "?retryWrites=true&w=majority&useUnifiedTopology=true")
db = client['DAMA_Grup5']
collection = db['customers']

# Obtener los documentos de la colección como una lista de diccionarios
data = list(collection.find())

# Crear el DataFrame de Pandas
df = pd.DataFrame(data)
from flask import Flask, jsonify

app = Flask(__name__)

variable = df

@app.route('/variable')
def get_variable():
    return jsonify(variable=variable)