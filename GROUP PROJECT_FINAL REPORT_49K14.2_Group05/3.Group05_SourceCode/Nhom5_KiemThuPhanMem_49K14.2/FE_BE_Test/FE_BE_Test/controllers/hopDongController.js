const hopDongModel = require('../models/hopDongModel');

const hopDongController = {
    getHopDong: (req, res) => {
        const search = req.query.search || '';
        hopDongModel.getAllHopDong(search, (err, data) => {
            if (err) return res.status(500).json({ success: false, message: 'Lỗi server khi lấy hợp đồng' });
            res.json({ success: true, data });
        });
    },

    createHopDong: (req, res) => {
        hopDongModel.createHopDong(req.body, (err, newMaHD) => {
            if (err) return res.status(500).json({ success: false, message: 'Lỗi hệ thống khi tạo hợp đồng' });
            res.json({ success: true, message: 'Thêm hợp đồng thành công', data: { ma_hop_dong: newMaHD } });
        });
    },

    deleteHopDong: (req, res) => {
        hopDongModel.deleteHopDong(req.params.id, (err) => {
            if (err) return res.status(500).json({ success: false, message: 'Lỗi hệ thống khi xóa hợp đồng' });
            res.json({ success: true, message: 'Xóa hợp đồng thành công.' });
        });
    }
};

module.exports = hopDongController;
