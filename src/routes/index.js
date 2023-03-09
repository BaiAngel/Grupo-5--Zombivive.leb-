const router = require('express').Router();
const passport = require('passport');

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

const bodyParser = require('body-parser');
router.use(bodyParser.json());

router.post('/score', (req, res) => {
  const username = req.body.username;
  const score = req.body.score;

  // Aquí puedes hacer lo que quieras con los datos, como almacenarlos en una base de datos o procesarlos de alguna otra manera
  console.log(`Usuario: ${username}, Puntaje: ${score}`);

  res.send('¡Datos recibidos!');
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
