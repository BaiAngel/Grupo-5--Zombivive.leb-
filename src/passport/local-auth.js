const passport = require('passport');
const LocalStratergy = require('passport-local').Strategy;

passport.use('local-singup', new LocalStratergy({
    usernameField: 'email',
    passwordField: 'passw',
    passReqToCallback: true
}, (req, email, passw, done) => {
    
}))