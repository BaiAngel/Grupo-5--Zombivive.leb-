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

   const user = User.findOne({email: email});
    if(user){
        return done(null, false, req.flash('singupMessage', 'El email ya existe'));
    }else{
    const newUser = new User();
    newUser.email = email;
    newUser.passw = newUser.encryptPassword(passw);
    await newUser.save();
    done(null, newUser);
    }
}));