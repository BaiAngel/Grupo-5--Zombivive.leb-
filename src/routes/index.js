const router = require('express').Router();
const passport = require('passport');
const Customer = require('../models/user');
const MongoClient = require('mongodb').MongoClient;
const url = 'mongodb://a19angavimar:Grup5@labs.inspedralbes.cat:7010/?authMechanism=DEFAULT&authSource=DAMA_Grup5&tls=false' + "?retryWrites=true&w=majority&useUnifiedTopology=true";
const dbName = 'DAMA_Grup5';

router.get('/', (req, res, next) => {
  res.render('index');
});
router.get('/singup', (req, res, next) => {
  res.render('singup');
});
router.post('/singup', passport.authenticate('local-signup', {
  successRedirect: '/profile.html',
  failureRedirect: '/singin.html',
  failureFlash: true
})); 
router.get('/singin', (req, res, next) => {
  res.render('singin');
});
router.post('/singin', passport.authenticate('local-signin', {
  successRedirect: '/profile.html',
  failureRedirect: '/singup.html',
  failureFlash: true
}));
router.get('/profile',isAuthenticated, (req, res, next) => {
  res.render('profile');
});
router.get('/grafficas', (req, res) => {
  MongoClient.connect(url, function(err, client) {
    if (err) throw err;
    const db = client.db(dbName);
    const collection = db.collection('cusromers');

    collection.find().toArray(function(err, data) {
      if (err) throw err;

      const pandasData = collection.find();

      // Crear un DataFrame de Pandas
      const df = pandas.DataFrame(pandasData);

      // Genera la gráfica utilizando Plotly
      const plotlyData = [{
        type: 'bar',
        x: df.index,
        y: df.values,
        orientation: 'h'
      }];

      const plotlyLayout = {
        title: 'Mi gráfica',
        xaxis: {
          title: 'Eje X'
        },
        yaxis: {
          title: 'Eje Y'
        }
      };

      const plotlyConfig = {
        responsive: true
      };

      const plotlyHTML = `<div id="plotly-div"></div><script src="https://cdn.plot.ly/plotly-latest.min.js"></script><script>Plotly.newPlot('plotly-div', ${JSON.stringify(plotlyData)}, ${JSON.stringify(plotlyLayout)}, ${JSON.stringify(plotlyConfig)});</script>`;

      res.send(plotlyHTML);
      client.close();
    });
  });
});


const bodyParser = require('body-parser');
router.use(bodyParser.json());

router.post('/score', (req, res) => {
  const username = req.body.username;
  const score = req.body.score;

  // Aquí puedes hacer lo que quieras con los datos, como almacenarlos en una base de datos o procesarlos de alguna otra manera
  console.log(`Usuario: ${username}, Puntaje: ${score}`);

  const newCustomer = new Customer({
    name: username,
    kills: score
  });
  newCustomer.save()
    .then(() => console.log('Customer saved'))
    .catch(err => console.log('Error saving customer:', err));

  res.send('¡Datos recibidos!');
  const Customers = Customer.find({});
  
  // Copiar cada usuario en una nueva colección
  for (let Customer of Customers) {
    const newUser = new User({
      name: Customer.name,
      kills: Customer.kills
      // Agregar cualquier otro campo que desees copiar
    }); newCustomer.save();
  }
  
  console.log('Copiado exitosamente!');
});


router.get('/logout', (req, res, next) => {
  req.logout();
  res.redirect('/');
});


function isAuthenticated(req, res, next) {
  if(req.isAuthenticated()) {
    return next();
  }

  res.redirect('/')
}

module.exports = router;
