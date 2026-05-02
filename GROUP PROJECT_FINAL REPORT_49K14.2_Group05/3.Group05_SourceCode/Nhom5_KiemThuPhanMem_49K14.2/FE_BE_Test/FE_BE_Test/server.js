const express = require('express');
const cors = require('cors');
const path = require('path');

// 1. Khởi tạo Express app
const app = express();
const PORT = 3000;

// 2. Cấu hình middleware
app.use(cors());
app.use(express.json());

// Phục vụ các file tĩnh trong thư mục dự án
app.use(express.static(path.join(__dirname, 'public')));

// Điều hướng mặc định localhost:3000 bay thẳng vào trang Login
app.get('/', (req, res) => {
    res.redirect('/Đăng_nhập/Dang_nhap.html');
});

// 3. Import các Routes
const authRoutes = require('./router/authRoutes');
const phongRoutes = require('./router/phongRoutes');
const khachThueRoutes = require('./router/khachThueRoutes');
const hopDongRoutes = require('./router/hopDongRoutes');

// 4. Định tuyến URL (Gắn routes vào app)
app.use('/api', authRoutes);      // /api/login
app.use('/api', phongRoutes);     // /api/phong
app.use('/api', khachThueRoutes); // /api/khach_thue
app.use('/api', hopDongRoutes);   // /api/hop_dong

// ==========================================

app.listen(PORT, () => {
    console.log(`Server đang chạy tại http://localhost:${PORT}`);
});
