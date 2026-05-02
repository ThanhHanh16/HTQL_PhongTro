const express = require('express');
const router = express.Router();
const khachThueController = require('../controllers/khachThueController');

router.get('/khach_thue', khachThueController.getKhachThue);
router.post('/khach_thue', khachThueController.createKhachThue);
router.put('/khach_thue/:id', khachThueController.updateKhachThue);
router.delete('/khach_thue/:id', khachThueController.deleteKhachThue);

module.exports = router;
