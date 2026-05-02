const db = require('../config/database');

const phongModel = {
    // 1. Lấy danh sách phòng
    getAllPhong: (search, callback) => {
        // Auto-update trạng thái phòng dựa trên hạn hợp đồng
        const syncStatusQuery = `
            UPDATE phong 
            SET trang_thai = 'Trống' 
            WHERE trang_thai = 'Đang thuê' 
              AND ma_phong NOT IN (
                  SELECT ma_phong FROM hop_dong 
                  WHERE date(ngay_ket_thuc) >= date('now', 'localtime')
              )
        `;
        db.run(syncStatusQuery, [], (errSync) => {
            if (errSync) console.error("Lỗi đồng bộ trạng thái phòng:", errSync);
            
            let query = `SELECT * FROM phong`;
            let params = [];
            if (search) {
                query += ` WHERE ma_phong LIKE ?`;
                params.push(`%${search}%`);
            }
            db.all(query, params, (err3, rows) => {
                callback(err3, rows);
            });
        });
    },

    // 2. Thêm phòng mới (Kiểm tra trùng mã)
    checkAndAddPhong: (ma_phong, dien_tich, so_nguoi_toi_da, gia_thue, mo_ta, trang_thai, callback) => {
        db.get('SELECT ma_phong FROM phong WHERE ma_phong = ?', [ma_phong], (err, row) => {
            if (row) return callback('Mã phòng đã tồn tại. Vui lòng nhập mã khác.', null);
            db.run(
                `INSERT INTO phong (ma_phong, dien_tich, so_nguoi_toi_da, gia_thue, mo_ta, trang_thai) VALUES (?, ?, ?, ?, ?, ?)`,
                [ma_phong, dien_tich, so_nguoi_toi_da, gia_thue, mo_ta, 'Trống'],
                function (errInsert) {
                    callback(errInsert, this.lastID);
                }
            );
        });
    },

    // 3. Sửa thông tin phòng
    updatePhong: (ma_phong, dien_tich, so_nguoi_toi_da, gia_thue, mo_ta, trang_thai, callback) => {
        db.run(
            `UPDATE phong SET dien_tich = ?, so_nguoi_toi_da = ?, gia_thue = ?, mo_ta = ? WHERE ma_phong = ?`,
            [dien_tich, so_nguoi_toi_da, gia_thue, mo_ta, ma_phong],
            function (err) {
                callback(err);
            }
        );
    },

    // 4. Xóa phòng
    deletePhong: (ma_phong, callback) => {
        db.get('SELECT trang_thai FROM phong WHERE ma_phong = ?', [ma_phong], (err, row) => {
            if (!row) return callback('Không tìm thấy phòng', null);
            if (row.trang_thai === 'Đang thuê') {
                return callback('Không thể xóa phòng vì phòng đang được thuê.', null);
            }
            db.run('DELETE FROM phong WHERE ma_phong = ?', [ma_phong], function (errDelete) {
                callback(errDelete, true);
            });
        });
    }
};

module.exports = phongModel;
