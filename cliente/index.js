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
        ],
      }
    },
  })
  