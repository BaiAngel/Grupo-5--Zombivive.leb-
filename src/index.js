const express = require('express');
const engine = require('ejs-mate');
const path = require('path');
const morgan = require('morgan');

//Initalizations
const app = express();
require('./database');

// setings
app.set('views', path.join(__dirname, 'cliente'));
app.engine('ejs', engine);
app.set('view engine', 'ejs');
app.set('port',process.env.PORT || 3000);

//middlewares
app.use(express.static('cliente'));
app.use(morgan('dev'));
app.use(express.urlencoded({extended: false}));

//Routes
app.use('/', require('./routes/index'));

//starting the server
app.listen(app.get('port'), () =>{
    console.log('Server on Port', app.get('port'));
});
