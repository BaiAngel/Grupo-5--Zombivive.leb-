const mongoose = require('mongoose');
const bcrypt = require('bcrypt-nodejs');
const {Schema} = mongoose;

const userSchema = new Schema({
    email: String,
    pass: String
});

userSchema.method.encryptPassword = (pass) => {
    return bcrypt.hashSync(pass, bcrypt.genSaltSync(10));
};

userSchema.method.comparePassword = function (pass) {
    return bcrypt.compareSync(pass, this.pass);
};

module.expors = mongoose.model('user', userSchema);