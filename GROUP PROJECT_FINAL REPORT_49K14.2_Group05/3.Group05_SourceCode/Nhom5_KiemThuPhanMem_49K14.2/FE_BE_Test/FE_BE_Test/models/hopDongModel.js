const db = require('../config/database');

const hopDongModel = {
    // 1. Lấy danh sách hợp đồng kèm thông tin phòng & khách
    getAllHopDong: (search, callback) => {
        let query = `
            SELECT hd.*, p.dien_tich, kt.ten_khach_thue
            FROM hop_dong hd
            JOIN phong p ON hd.ma_phong = p.ma_phong
            LEFT JOIN khach_thue kt ON hd.ma_khach_thue = kt.ma_khach_thue
        `;
        let params = [];
        if (search) {
            query += ` WHERE hd.ma_hop_dong LIKE ?`;
            params.push(`%${search}%`);
        }
        
        query += ` ORDER BY CAST(SUBSTR(hd.ma_hop_dong, 3) AS INTEGER) ASC`;
        console.log('QUERY HOP DONG:', query);

        db.all(query, params, (err, rows) => {
            if (err) return callback(err, null);
            console.log('ROWS HOP DONG:', rows.map(x => x.ma_hop_dong));
            // Lấy thêm chi tiết khách thuê cho từng hợp đồng (Promise)
            const promises = rows.map(row => {
                return new Promise((resolve, reject) => {
                    db.all(`
                        SELECT kt.* FROM khach_thue kt 
                        JOIN chi_tiet_hop_dong cthd ON kt.ma_khach_thue = cthd.ma_khach_thue 
                        WHERE cthd.ma_hop_dong = ?
                    `, [row.ma_hop_dong], (errKT, kts) => {
                        if (errKT) reject(errKT);
                        row.khach_thue_list = kts || [];
                        resolve(row);
                    });
                });
            });

            Promise.all(promises).then(results => {
                callback(null, results);
            }).catch(errP => callback(errP, null));
        });
    },

    // 2. Tạo hợp đồng mới (Sử dụng Transaction để đảm bảo an toàn)
    createHopDong: (data, callback) => {
        const { ma_phong, ma_khach_thue, ngay_bat_dau, ngay_ket_thuc, tien_coc, dieu_khoan, khach_thue_ids } = data;
        
        db.get('SELECT ma_hop_dong FROM hop_dong ORDER BY rowid DESC LIMIT 1', (err, row) => {
            let newMaHD = 'HD0001';
            if (row) {
                const lastNum = parseInt(row.ma_hop_dong.replace('HD', ''));
                newMaHD = `HD${(lastNum + 1).toString().padStart(4, '0')}`;
            }
            
            db.serialize(() => {
                db.run('BEGIN TRANSACTION');
                
                db.run(
                    `INSERT INTO hop_dong (ma_hop_dong, ma_phong, ma_khach_thue, ngay_bat_dau, ngay_ket_thuc, tien_coc, dieu_khoan) VALUES (?, ?, ?, ?, ?, ?, ?)`,
                    [newMaHD, ma_phong, ma_khach_thue, ngay_bat_dau, ngay_ket_thuc, tien_coc, dieu_khoan],
                    function(errHD) {
                        if (errHD) { db.run('ROLLBACK'); return callback(errHD); }
                        
                        // Thêm chi tiết khách thuê
                        if (khach_thue_ids && khach_thue_ids.length > 0) {
                            const stmt = db.prepare('INSERT INTO chi_tiet_hop_dong (ma_hop_dong, ma_khach_thue) VALUES (?, ?)');
                            khach_thue_ids.forEach(kt_id => stmt.run([newMaHD, kt_id]));
                            stmt.finalize();
                        }

                        // Cập nhật trạng thái phòng sang 'Đang thuê'
                        db.run(`UPDATE phong SET trang_thai = 'Đang thuê' WHERE ma_phong = ?`, [ma_phong], (errUpdate) => {
                            if (errUpdate) { db.run('ROLLBACK'); return callback(errUpdate); }
                            db.run('COMMIT');
                            callback(null, newMaHD);
                        });
                    }
                );
            });
        });
    },

    // 3. Xóa hợp đồng (Và trả phòng về trạng thái 'Trống')
    deleteHopDong: (id, callback) => {
        db.get('SELECT ma_phong FROM hop_dong WHERE ma_hop_dong = ?', [id], (err, row) => {
            if (!row) return callback('Hợp đồng không tồn tại');
            const maPhong = row.ma_phong;
            
            db.serialize(() => {
                db.run('BEGIN TRANSACTION');
                db.run('DELETE FROM chi_tiet_hop_dong WHERE ma_hop_dong = ?', [id]);
                db.run('DELETE FROM hop_dong WHERE ma_hop_dong = ?', [id], (errDel) => {
                    if (errDel) { db.run('ROLLBACK'); return callback(errDel); }
                    
                    db.run(`UPDATE phong SET trang_thai = 'Trống' WHERE ma_phong = ?`, [maPhong], (errUp) => {
                        if (errUp) { db.run('ROLLBACK'); return callback(errUp); }
                        db.run('COMMIT');
                        callback(null);
                    });
                });
            });
        });
    }
};

module.exports = hopDongModel;
