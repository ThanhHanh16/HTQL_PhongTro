const phongModel = require('../models/phongModel');

const phongController = {
    // API GET /api/phong
    getPhong: (req, res) => {
        const search = req.query.search || '';
        phongModel.getAllPhong(search, (err, rows) => {
            if (err) return res.status(500).json({ success: false, message: 'Lỗi server' });
            res.json({ success: true, data: rows });
        });
    },

    // API POST /api/phong
    createPhong: (req, res) => {
        const { ma_phong, dien_tich, so_nguoi_toi_da, gia_thue, mo_ta, trang_thai } = req.body;
        phongModel.checkAndAddPhong(ma_phong, dien_tich, so_nguoi_toi_da, gia_thue, mo_ta, trang_thai, (errMessage) => {
            if (errMessage && typeof errMessage === 'string') {
                return res.status(400).json({ success: false, message: errMessage }); // Lỗi logic (vd trùng mã)
            } else if (errMessage) {
                return res.status(500).json({ success: false, message: 'Hệ thống đang lỗi, không thể thêm phòng. Vui lòng thử lại sau.' });
            }
            res.json({ success: true, message: 'Thêm phòng mới thành công' });
        });
    },

    // API PUT /api/phong/:id
    updatePhong: (req, res) => {
        const { dien_tich, so_nguoi_toi_da, gia_thue, mo_ta, trang_thai } = req.body;
        const ma_phong = req.params.id;
        phongModel.updatePhong(ma_phong, dien_tich, so_nguoi_toi_da, gia_thue, mo_ta, trang_thai, (err) => {
            if (err) return res.status(500).json({ success: false, message: 'Hệ thống đang lỗi, không thể cập nhật.' });
            res.json({ success: true, message: 'Cập nhật thông tin phòng thành công' });
        });
    },

    // API DELETE /api/phong/:id
    deletePhong: (req, res) => {
        const ma_phong = req.params.id;
        phongModel.deletePhong(ma_phong, (errMessage) => {
            if (errMessage && typeof errMessage === 'string') {
                return res.status(400).json({ success: false, message: errMessage });
            } else if (errMessage) {
                return res.status(500).json({ success: false, message: 'Hệ thống đang lỗi, không thể xóa phòng.' });
            }
            res.json({ success: true, message: 'Xóa phòng thành công.' });
        });
    }
};

module.exports = phongController;
