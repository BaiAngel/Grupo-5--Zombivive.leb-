const mongoose = require("mongoose");
const options = {
    serverSelectionTimeoutMS: 5000,
    dbName: "DAMA_Grup5" 
}
mongoose.set('strictQuery', true);
const uri = 'mongodb://a19angavimar:Grup5@labs.inspedralbes.cat:7010/?authMechanism=DEFAULT&authSource=DAMA_Grup5&tls=false';
// connect your database
mongoose.connect(uri, options, {useNewUrlParser: true})
  .then(()=> console.log('conectado a mongodb')) 
  .catch(e => console.log('error de conexión', e))