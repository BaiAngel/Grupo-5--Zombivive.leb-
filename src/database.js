const mongoose = require('mongoose');
const { mongodb } = require('./keys');
const options = {
  dbName: "DAMA_Grup5"
}

mongoose.set('useFindAndModify', true);
mongoose.connect(mongodb.URI, options, {
  useNewUrlParser: true, useUnifiedTopology: true, bufferTimeMS: 60000
})
  .then(db => console.log('DB is connected'))
  .catch(err => console.log(err));
