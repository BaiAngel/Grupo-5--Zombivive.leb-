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
