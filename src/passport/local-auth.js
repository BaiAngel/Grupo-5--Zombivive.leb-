const passport = require('passport');
const LocalStratergy = require('passport-local').Strategy;

const User = require('../models/users')

passport.serializeUser((user, done) => {
    done(null, user.id);
});

passport.deserializeUser(async(id, done) => {
    const user = await User.findById(id);
    done(null, user);
});

passport.use('local-singup', new LocalStratergy({
    usernameField: 'email',
    passwordField: 'passw',
    passReqToCallback: true
}, async (req, email, passw, done) => {
    const user = new User();
    user.email = email;
    user.passw = passw;
    await user.save();
    done(null, user);
}));