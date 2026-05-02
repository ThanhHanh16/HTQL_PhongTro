const khachThueModel = require('../models/khachThueModel');

const khachThueController = {
    getKhachThue: (req, res) => {
        const search = req.query.search || '';
        khachThueModel.getAllKhachThue(search, (err, rows) => {
            if (err) return res.status(500).json({ success: false, message: 'Lỗi server' });
            res.json({ success: true, data: rows });
        });
    },

    createKhachThue: (req, res) => {
        console.log('BODY ADD KHACH:', req.body);

        khachThueModel.addKhachThue(req.body, (err, newMaKT) => {
            console.log('ADD KHACH ERR:', err);
            console.log('ADD KHACH NEW MA:', newMaKT);

            if (err && err.status === 400) {
                return res.status(400).json({ success: false, message: err.message });
            }

            if (err) {
                return res.status(500).json({
                    success: false,
                    message: 'Hệ thống đang lỗi, không thể thêm khách.',
                    debug: err.message || String(err)
                });
            }

            res.json({
                success: true,
                message: 'Thêm khách thuê thành công',
                data: { ma_khach_thue: newMaKT }
            });
        });
    },

    updateKhachThue: (req, res) => {
        khachThueModel.updateKhachThue(req.params.id, req.body, (err) => {
            if (err && err.status === 400) return res.status(400).json({ success: false, message: err.message });
            if (err) return res.status(500).json({ success: false, message: 'Hệ thống đang lỗi, không thể sửa khách.' });

            res.json({ success: true, message: 'Cập nhật thông tin khách thuê thành công' });
        });
    },

    deleteKhachThue: (req, res) => {
        khachThueModel.deleteKhachThue(req.params.id, (err) => {
            if (err && err.status === 400) return res.status(400).json({ success: false, message: err.message });
            if (err) return res.status(500).json({ success: false, message: 'Hệ thống đang lỗi, không thể xóa khách.' });

            res.json({ success: true, message: 'Xóa khách thuê thành công.' });
        });
    }
};

module.exports = khachThueController;