const express = require('express');
const app = express();
const MongoClient = require('mongodb').MongoClient;
const pandas = require('pandas-js');
const Chart = require('chart.js');

// Conectar a la base de datos de MongoDB
const uri = 'mongodb://localhost:27017/mi-base-de-datos';
const client = new MongoClient(uri, { useNewUrlParser: true });
client.connect(async (err) => {
  if (err) {
    console.error(err);
    return;
  }

  // Recuperar los datos de MongoDB
  const db = client.db();
  const collection = db.collection('mi-coleccion');
  const data = await collection.find().toArray();

  // Crear un DataFrame de Pandas
  const df = pandas.DataFrame(data);

  // Calcular los datos necesarios para la gráfica
  const counts = df.groupby('categoria').count().iloc[:, 0].values;
  const categorias = df.groupby('categoria').count().index.values;

  // Renderizar la gráfica con Chart.js
  const chartData = {
    labels: categorias,
    datasets: [
      {
        label: 'Cantidad',
        data: counts,
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1,
      },
    ],
  };

  const chartConfig = {
    type: 'bar',
    data: chartData,
    options: {
      scales: {
        yAxes: [
          {
            ticks: {
              beginAtZero: true,
            },
          },
        ],
      },
    },
  };

  const chartHTML = `
    <div>
      <canvas id="mi-grafica"></canvas>
    </div>
    <script>
      var ctx = document.getElementById('mi-grafica').getContext('2d');
      var chart = new Chart(ctx, ${JSON.stringify(chartConfig)});
    </script>
  `;

  // Enviar la página HTML con la gráfica
  res.send(chartHTML);
});
