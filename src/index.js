const express = require('express');

const app = express();

// setings
app.set('port',process.env.PORT || 3000);

//starting the server
app.listen(app.get('port'), () =>{
    console.log('Server on Port', app.get('port'));
});