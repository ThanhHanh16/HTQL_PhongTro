const bcrypt = require('bcrypt');
const userModel = require('../models/userModel');

const authController = {
    login: (req, res) => {
        const username = req.body.username || req.body.sdt;
        const { password } = req.body;

        console.log('LOGIN BODY:', req.body);
        console.log('USERNAME NHAN DUOC:', username);
        console.log('PASSWORD NHAN DUOC:', password);

        if (!username || !password) {
            console.log('THIEU USERNAME HOAC PASSWORD');
            return res.status(400).json({
                success: false,
                message: 'Vui lòng nhập đầy đủ tài khoản và mật khẩu'
            });
        }

        userModel.findUserByUsername(username, async (err, user) => {
            console.log('FOUND USER:', user);

            if (err) {
                console.error('LOGIN ERROR:', err);
                return res.status(500).json({
                    success: false,
                    message: 'Lỗi hệ thống'
                });
            }

            if (!user) {
                console.log('KHONG TIM THAY USER');
                return res.status(400).json({
                    success: false,
                    message: 'Tên đăng nhập hoặc mật khẩu không hợp lệ, vui lòng nhập lại'
                });
                
            }

            try {
                const match = await bcrypt.compare(password, user.password);
                console.log('PASSWORD TRONG DB:', user.password);
                console.log('MATCH PASSWORD:', match);

                if (match) {
                    console.log('DANG NHAP THANH CONG');
                    return res.json({
                        success: true,
                        message: 'Đăng nhập thành công'
                    });
                } else {
                    console.log('SAI MAT KHAU');
                    return res.status(400).json({
                        success: false,
                        message: 'Tên đăng nhập hoặc mật khẩu không hợp lệ, vui lòng nhập lại'
                    });
                }
            } catch (error) {
                console.error('BCRYPT ERROR:', error);
                return res.status(500).json({
                    success: false,
                    message: 'Lỗi kiểm tra mật khẩu'
                });
            }
        });
    }
};

module.exports = authController;