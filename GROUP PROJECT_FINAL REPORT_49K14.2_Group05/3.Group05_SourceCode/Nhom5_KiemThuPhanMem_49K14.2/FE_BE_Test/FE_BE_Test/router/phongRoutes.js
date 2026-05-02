const express = require('express');
const router = express.Router();
const phongController = require('../controllers/phongController');

router.get('/phong', phongController.getPhong);
router.post('/phong', phongController.createPhong);
router.put('/phong/:id', phongController.updatePhong);
router.delete('/phong/:id', phongController.deletePhong);

module.exports = router;
