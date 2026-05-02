const sqlite3 = require('sqlite3').verbose();
const bcrypt = require('bcrypt');

// Khởi tạo Database SQLite "database.db"
const db = new sqlite3.Database('./database.db', (err) => {
    if (err) {
        console.error('Lỗi khi kết nối database:', err.message);
    } else {
        console.log('Kết nối thành công với SQLite database.');

        db.serialize(() => {
            // 1. Tạo bảng users
            db.run(`CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE,
                password TEXT
            )`);

            // 2. Tạo bảng phong
            db.run(`CREATE TABLE IF NOT EXISTS phong (
                ma_phong TEXT PRIMARY KEY,
                dien_tich REAL,
                so_nguoi_toi_da INTEGER,
                gia_thue REAL,
                trang_thai TEXT DEFAULT 'Trống',
                mo_ta TEXT
            )`);

            // 3. Tạo bảng khach_thue
            db.run(`CREATE TABLE IF NOT EXISTS khach_thue (
                ma_khach_thue TEXT PRIMARY KEY,
                ten_khach_thue TEXT,
                gioi_tinh TEXT,
                ngay_sinh TEXT,
                sdt TEXT,
                cccd TEXT UNIQUE,
                que_quan TEXT
            )`);

            // 4. Tạo bảng hop_dong
            db.run(`CREATE TABLE IF NOT EXISTS hop_dong (
                ma_hop_dong TEXT PRIMARY KEY,
                ma_phong TEXT,
                ma_khach_thue TEXT,
                ngay_bat_dau TEXT,
                ngay_ket_thuc TEXT,
                tien_coc REAL,
                trang_thai TEXT DEFAULT 'Đang hiệu lực',
                dieu_khoan TEXT,
                FOREIGN KEY (ma_phong) REFERENCES phong(ma_phong),
                FOREIGN KEY (ma_khach_thue) REFERENCES khach_thue(ma_khach_thue)
            )`);

            // 5. Tạo bảng chi tiết hợp đồng
            db.run(`CREATE TABLE IF NOT EXISTS chi_tiet_hop_dong (
                ma_hop_dong TEXT,
                ma_khach_thue TEXT,
                PRIMARY KEY (ma_hop_dong, ma_khach_thue),
                FOREIGN KEY (ma_hop_dong) REFERENCES hop_dong(ma_hop_dong) ON DELETE CASCADE,
                FOREIGN KEY (ma_khach_thue) REFERENCES khach_thue(ma_khach_thue)
            )`);

            // Seed Admin User
            const adminUser = '0123456789';
            const adminPass = 'Admin@123';

            db.get(`SELECT * FROM users WHERE username = ?`, [adminUser], async (err, row) => {
                if (!row) {
                    const hashed = await bcrypt.hash(adminPass, 10);
                    db.run(`INSERT INTO users (username, password) VALUES (?, ?)`, [adminUser, hashed], (err) => {
                        if (!err) console.log('Đã tạo tài khoản admin mặc định: 0123456789 / Admin@123');
                    });
                }
            });

            // --- SEED SAMPLE DATA ---
            // 1. Seed Rooms
            db.run(`INSERT OR IGNORE INTO phong (ma_phong, dien_tich, so_nguoi_toi_da, gia_thue, trang_thai, mo_ta) VALUES 
                ('P101', 25.0, 2, 2500000, 'Trống', 'Phòng nhỏ, có ban công'),
                ('P102', 30.0, 3, 3000000, 'Đang thuê', 'Phòng lớn, có gác lửng'),
                ('P103', 20.0, 2, 2000000, 'Trống', 'Phòng tiêu chuẩn'),
                ('P201', 35.0, 4, 4000000, 'Trống', 'Phòng đôi, full nội thất')`);

            // 2. Seed Tenants (KT0001, KT0002, KT0003)
            db.run(`INSERT OR IGNORE INTO khach_thue (ma_khach_thue, ten_khach_thue, gioi_tinh, ngay_sinh, sdt, cccd, que_quan) VALUES 
                ('KT0001', 'Nguyễn Văn A', 'Nam', '2000-01-15', '0912345678', '001200000001', 'Hà Nội'),
                ('KT0002', 'Lê Thị B', 'Nữ', '2002-05-20', '0987654321', '001200000002', 'Hải Phòng'),
                ('KT0003', 'Trần Văn C', 'Nam', '1998-10-10', '0901234567', '001200000003', 'Đà Nẵng')`);

            // 3. Seed Contract (For P102)
            db.run(`INSERT OR IGNORE INTO hop_dong (ma_hop_dong, ma_phong, ma_khach_thue, ngay_bat_dau, ngay_ket_thuc, tien_coc, trang_thai, dieu_khoan) VALUES 
                ('HD0001', 'P102', 'KT0001', '2024-01-01', '2025-01-01', 3000000, 'Đang hiệu lực', 'Đóng tiền nhà trước ngày mùng 5 hàng tháng. Giữ vệ sinh chung.')`);

            // 4. Seed Contract Details (Associating KT0001 and KT0002 with HD0001)
            db.run(`INSERT OR IGNORE INTO chi_tiet_hop_dong (ma_hop_dong, ma_khach_thue) VALUES 
                ('HD0001', 'KT0001'),
                ('HD0001', 'KT0002')`);

        });
    }
});
// Kích hoạt foreign keys
db.run("PRAGMA foreign_keys = ON");
// BẮT BUỘC: Xuất biến db ra để dùng ở nơi khác
module.exports = db;