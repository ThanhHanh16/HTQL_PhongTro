console.log("JS LOGIN LOADED");
const API_BASE_URL = 'http://localhost:3000';

function clearErrors() {
    document.getElementById('username-error').innerText = '';
    document.getElementById('password-error').innerText = '';
    document.getElementById('form-error').innerText = '';
}

async function handleLogin() {
    clearErrors();
    const username = document.getElementById('log-username').value.trim();
    const password = document.getElementById('log-password').value;

    let hasError = false;

    // Validate Username
    if (username === "") {
        document.getElementById('username-error').innerText = "Tên đăng nhập không được bỏ trống";
        hasError = true;
    } else {
        const phoneRegex = /^0\d{9}$/;
        if (!phoneRegex.test(username)) {
            document.getElementById('form-error').innerText = "Tên đăng nhập hoặc mật khẩu không hợp lệ, vui lòng nhập lại";
            hasError = true;
        }
    }

    // Validate Password
    if (password === "") {
        document.getElementById('password-error').innerText = "Mật khẩu không được bỏ trống";
        hasError = true;
    } else {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,12}$/;
        if (!passwordRegex.test(password)) {
            document.getElementById('form-error').innerText = "Tên đăng nhập hoặc mật khẩu không hợp lệ, vui lòng nhập lại";
            hasError = true;
        }
    }

    if (hasError) return;

    try {
        const response = await fetch(`${API_BASE_URL}/api/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        const data = await response.json();
        if (data.success) {
            window.location.href = "../Phòng/Phong.html";
        } else {
            // Hiển thị thông báo “Tên đăng nhập hoặc mật khẩu không hợp lệ, vui lòng nhập lại”
            document.getElementById('form-error').innerText = data.message || "Tên đăng nhập hoặc mật khẩu không hợp lệ, vui lòng nhập lại";
        }
    } catch (error) {
        console.error("Backend not accessible", error);
        document.getElementById('form-error').innerText = "Hệ thống đang lỗi. Vui lòng thử lại sau.";
    }
}

document.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        handleLogin();
    }
});