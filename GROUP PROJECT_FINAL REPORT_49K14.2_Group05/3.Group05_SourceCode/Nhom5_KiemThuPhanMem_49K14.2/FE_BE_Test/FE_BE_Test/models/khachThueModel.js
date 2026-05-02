const db = require('../config/database');

const khachThueModel = {
    getAllKhachThue: (search, callback) => {
        let query = `
            SELECT * FROM khach_thue
        `;
        let params = [];

        if (search) {
            query += ` WHERE ten_khach_thue LIKE ?`;
            params.push(`%${search}%`);
        }

        query += ` ORDER BY CAST(SUBSTR(ma_khach_thue, 3) AS INTEGER) ASC`;

        console.log('QUERY KHACH THUE:', query);

        db.all(query, params, (err, rows) => {
            console.log('ROWS KHACH THUE:', rows.map(x => x.ma_khach_thue));
            callback(err, rows);
        });
    },

    addKhachThue: (data, callback) => {
        const { ten_khach_thue, gioi_tinh, ngay_sinh, sdt, cccd, que_quan } = data;
        console.log('MODEL ADD DATA:', data);
        // 1. Lấy mã lớn nhất rồi + 1
            db.get(`
                SELECT MAX(CAST(SUBSTR(ma_khach_thue, 3) AS INTEGER)) AS maxNum
                FROM khach_thue
            `, (err, row) => {
                if (err) return callback(err, null);

                const nextNum = (row && row.maxNum ? row.maxNum : 0) + 1;
                const newMaKT = `KT${nextNum.toString().padStart(4, '0')}`;
            
            // 2. Kiểm tra trùng CCCD
            db.get('SELECT cccd FROM khach_thue WHERE cccd = ?', [cccd], (errCheck, rowCheck) => {
                if (errCheck) {
                    console.log('LỖI CHECK CCCD:', errCheck);
                    return callback(errCheck, null);
                }
                if (rowCheck) return callback({ status: 400, message: 'Số CCCD đã tồn tại. Vui lòng kiểm tra lại' });
                
                // 3. Insert mới
                db.run(
                    `INSERT INTO khach_thue (ma_khach_thue, ten_khach_thue, gioi_tinh, ngay_sinh, sdt, cccd, que_quan) VALUES (?, ?, ?, ?, ?, ?, ?)`,
                    [newMaKT, ten_khach_thue, gioi_tinh, ngay_sinh, sdt, cccd, que_quan],
                    function (errInsert) {
                        console.log('LỖI INSERT:', errInsert);
                        callback(errInsert, newMaKT);
                    }
                );
            });
        });
    },

    updateKhachThue: (id, data, callback) => {
        const { ten_khach_thue, gioi_tinh, ngay_sinh, sdt, cccd, que_quan } = data;
        db.get('SELECT cccd FROM khach_thue WHERE cccd = ? AND ma_khach_thue != ?', [cccd, id], (errCheck, rowCheck) => {
            if (errCheck) return callback(errCheck);
            if (rowCheck) return callback({ status: 400, message: 'Số CCCD đã bị trùng với người khác.' });
            
            db.run(
                `UPDATE khach_thue SET ten_khach_thue = ?, gioi_tinh = ?, ngay_sinh = ?, sdt = ?, cccd = ?, que_quan = ? WHERE ma_khach_thue = ?`,
                [ten_khach_thue, gioi_tinh, ngay_sinh, sdt, cccd, que_quan, id],
                function (errUpdate) {
                    callback(errUpdate);
                }
            );
        });
    },

    deleteKhachThue: (id, callback) => {
        db.get('SELECT ma_khach_thue FROM chi_tiet_hop_dong WHERE ma_khach_thue = ?', [id], (err, row) => {
            if (err) return callback(err);
            if (row) return callback({ status: 400, message: 'Không thể xóa vì khách này đang còn hợp đồng thuê.' });
            
            db.run('DELETE FROM khach_thue WHERE ma_khach_thue = ?', [id], function (errDel) {
                callback(errDel);
            });
        });
    }
};

module.exports = khachThueModel;
