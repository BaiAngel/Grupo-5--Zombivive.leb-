const express = require('express');
const router = express.Router();
const passport = require('passport');


router.get('/', (req, res, next) => {
    res.render('index');
});

router.post('/singup', passport.authenticate('local-singup',{
    successRedirect: '/',
    failureRedirect: '/blog.html',
    passReqToCallback: true
}));

router.post('/singin', (req, res, next) => {
    console.log(req.body);
    res.send('cire gay 3')
});

module.exports = router;