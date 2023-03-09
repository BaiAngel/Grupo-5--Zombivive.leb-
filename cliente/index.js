new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
      value: [
        423,
        446,
        675,
        510,
        590,
        610,
        760,
      ],
    }),
  })

  new Vue({
    el: '#table',
    vuetify: new Vuetify(),
    data () {
      return {
        headers: [
          {
            text: 'NAME',
            align: 'start',
            sortable: false,
            value: 'name',
          },
          { text: 'KILLS', value: 'kills' },
         
        ],
        desserts: [
          Customer.find({})
          
        ],
      }
    },
  })

const Customer = require('../models/user');

// Obtener todos los usuarios
Customer.find({}, (err, customers) => {
  if (err) {
    // Manejar error
    console.error(err);
    
  }else{
    console.log(customers);
  }

  // Devolver usuarios como JSON
  res.json(customers);
});