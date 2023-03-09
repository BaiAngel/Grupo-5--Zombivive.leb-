const mongoose = require('mongoose');
const bcrypt = require('bcrypt-nodejs');

const { Schema } = mongoose;

const userSchema = new Schema({
  
  email: String,
  password: String
}, {
  timestamps: true,
  maxTimeMS: 60000 // aumentar tiempo de espera a 10000 ms fin
});

userSchema.methods.encryptPassword = (password) => {
  return bcrypt.hashSync(password, bcrypt.genSaltSync(10));
};

userSchema.methods.comparePassword= function (password) {
  return bcrypt.compareSync(password, this.password);
};

module.exports = mongoose.model('user', userSchema);

const customerSchema = new mongoose.Schema({
  name: String,
  kills: Number,
});

const Customer = mongoose.model('Customer', customerSchema);


/*
const newCustomer = new Customer({
  name: 'John Doe',
  kills: 23132
});
newCustomer.save()
  .then(() => console.log('Customer saved'))
  .catch(err => console.log('Error saving customer:', err));*/