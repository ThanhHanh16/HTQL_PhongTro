const express = require('express');
const router = express.Router();
const hopDongController = require('../controllers/hopDongController');

router.get('/hop_dong', hopDongController.getHopDong);
router.post('/hop_dong', hopDongController.createHopDong);
router.delete('/hop_dong/:id', hopDongController.deleteHopDong);

module.exports = router;
