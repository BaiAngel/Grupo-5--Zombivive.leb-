const express = require('express');
const router = express.Router();


router.get('/', (req, res, next) => {
    res.render('index');
});

router.post('/singup', (req, res, next) => {
    console.log(req.body);
    res.send('cire gay 2')
});

router.post('/singin', (req, res, next) => {
    console.log(req.body);
    res.send('cire gay 3')
});

module.exports = router;