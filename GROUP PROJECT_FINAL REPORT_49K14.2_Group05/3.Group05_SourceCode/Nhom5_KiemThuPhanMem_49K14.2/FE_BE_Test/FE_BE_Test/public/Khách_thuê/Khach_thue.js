const API_BASE_URL = 'http://localhost:3000';
let currentTenants = [];
let editingId = null;
let deletingId = null;

const INP_HOTEN = document.getElementById('inpHoten');
const INP_GIOITINH = document.getElementById('inpGioiTinh');
const INP_NGAYSINH = document.getElementById('inpNgaySinh');
const INP_SDT = document.getElementById('inpSdt');
const INP_CCCD = document.getElementById('inpCccd');
const INP_QUEQUAN = document.getElementById('inpQueQuan');

function openModal(id) { document.getElementById(id).style.display = 'block'; }
function closeModal(id) { document.getElementById(id).style.display = 'none'; }

function toggleLogout(event) {
    if (event) event.stopPropagation();
    const menu = document.getElementById('logoutMenu');
    if (menu) menu.classList.toggle('active');
}

window.onclick = function (event) {
    const userInfo = document.querySelector('.user-info');
    const logoutMenu = document.getElementById('logoutMenu');
    if (userInfo && !userInfo.contains(event.target)) {
        if (logoutMenu) logoutMenu.classList.remove('active');
    }
};

// ==========================================
// THÔNG BÁO CHUNG
// ==========================================
function showStatus(mode, msg) {
    openModal('modal-status');
    const iconBox = document.getElementById('statusIcon');
    const msgBox = document.getElementById('statusMsg');

    iconBox.innerHTML = mode === 'error'
        ? `<div style="background:#fef3c7; color:#d97706; border:2px solid #fbbf24; width:60px; height:60px; border-radius:50%; display:flex; align-items:center; justify-content:center; margin:0 auto; font-size:30px;"><i class="fa-solid fa-triangle-exclamation"></i></div>`
        : `<div style="background:#dcfce7; color:#15803d; border:2px solid #4ade80; width:60px; height:60px; border-radius:50%; display:flex; align-items:center; justify-content:center; margin:0 auto; font-size:30px;"><i class="fa-solid fa-check"></i></div>`;

    if (mode === 'fatal') {
        iconBox.innerHTML = `<div style="background:#ffebeb; color:#DB000A; border:2px solid #DB000A; width:60px; height:60px; border-radius:50%; display:flex; align-items:center; justify-content:center; margin:0 auto; font-size:30px;"><i class="fa-solid fa-xmark"></i></div>`;
    }

    msgBox.innerText = msg;
}

// ==========================================
// TẢI HIỂN THỊ DỮ LIỆU & TÌM KIẾM
// ==========================================
console.log('loadTenants đang chạy');
async function loadTenants(searchQuery = '') {
    try {
        
        const response = await fetch(`${API_BASE_URL}/api/khach_thue${searchQuery ? '?search=' + encodeURIComponent(searchQuery) : ''}`);
        const data = await response.json();

        const tbody = document.getElementById('tenantTableBody');
        tbody.innerHTML = '';
        const searchError = document.getElementById('search-error');
        searchError.innerText = '';

        if (data.success) {
            currentTenants = data.data;
            if (currentTenants.length === 0 && searchQuery) {
                searchError.innerText = "Khách thuê không tồn tại";
            } else {
                currentTenants.forEach(t => {
                    const tr = document.createElement('tr');
                    // Format date to dd/MM/yyyy
                    let formattedDate = t.ngay_sinh;
                    if (t.ngay_sinh) {
                        const parts = t.ngay_sinh.split('-');
                        if (parts.length === 3) formattedDate = `${parts[2]}/${parts[1]}/${parts[0]}`;
                    }

                    tr.innerHTML = `
                        <td>${t.ma_khach_thue}</td>
                        <td>${t.ten_khach_thue}</td>
                        <td>${t.gioi_tinh}</td>
                        <td>${formattedDate}</td>
                        <td>${t.sdt}</td>
                        <td>${t.cccd}</td>
                        <td>${t.que_quan}</td>
                        <td>
                            <i class="fa-solid fa-pen-to-square edit-icon" onclick="openEditModal('${t.ma_khach_thue}')"></i>
                            <i class="fa-solid fa-trash-can delete-icon" onclick="openDeleteConfirm('${t.ma_khach_thue}')"></i>
                        </td>
                    `;
                    tbody.appendChild(tr);
                });
            }
        }
    } catch (err) {
        showStatus('fatal', 'Có lỗi trong quá trình tìm kiếm');
    }
}

document.getElementById('searchInput').addEventListener('input', (e) => {
    loadTenants(e.target.value);
});


// ==========================================
// THÊM & SỬA KHÁCH THUÊ
// ==========================================
function clearFormErrors() {
    ['errHoten', 'errGioiTinh', 'errNgaySinh', 'errSdt', 'errCccd', 'errQueQuan', 'errFormGeneral'].forEach(id => {
        document.getElementById(id).innerText = '';
    });
}

function clearFormValues() {
    INP_HOTEN.value = '';
    INP_GIOITINH.value = '';
    INP_NGAYSINH.value = '';
    INP_SDT.value = '';
    INP_CCCD.value = '';
    INP_QUEQUAN.value = '';
    document.getElementById('inpMaKhach').value = '';
}

document.getElementById('btnOpenAdd').onclick = () => {
    document.getElementById('modalTitle').innerText = "Thêm khách thuê";
    editingId = null;
    clearFormValues();
    clearFormErrors();
    openModal('modal-form');
};

function openEditModal(id) {
    const t = currentTenants.find(x => x.ma_khach_thue === id);
    if (!t) return;

    document.getElementById('modalTitle').innerText = `Chỉnh sửa khách thuê - ${id}`;
    editingId = id;
    clearFormErrors();

    document.getElementById('inpMaKhach').value = t.ma_khach_thue;
    INP_HOTEN.value = t.ten_khach_thue;
    INP_GIOITINH.value = t.gioi_tinh;
    INP_NGAYSINH.value = t.ngay_sinh;
    INP_SDT.value = t.sdt;
    INP_CCCD.value = t.cccd;
    INP_QUEQUAN.value = t.que_quan;

    openModal('modal-form');
}

function promptCancelForm() {
    openModal('modal-cancel-confirm');
}

function confirmCancelForm() {
    closeModal('modal-cancel-confirm');
    closeModal('modal-form');
}

document.getElementById('btnSaveAction').onclick = async () => {
    clearFormErrors();
    let hasError = false;

    const data = {
        ten_khach_thue: INP_HOTEN.value.trim(),
        gioi_tinh: INP_GIOITINH.value,
        ngay_sinh: INP_NGAYSINH.value,
        sdt: INP_SDT.value.trim(),
        cccd: INP_CCCD.value.trim(),
        que_quan: INP_QUEQUAN.value.trim()
    };

    if (!data.ten_khach_thue) { document.getElementById('errHoten').innerText = "Tên khách thuê trống. Vui lòng nhập tên khách thuê."; hasError = true; }
    if (!data.gioi_tinh) { document.getElementById('errGioiTinh').innerText = "Giới tính trống. Vui lòng chọn giới tính."; hasError = true; }

    if (!data.ngay_sinh) {
        document.getElementById('errNgaySinh').innerText = "Ngày sinh trống. Vui lòng nhập ngày sinh."; hasError = true;
    } else {
        const today = new Date();
        const dobDate = new Date(data.ngay_sinh);
        if (dobDate > today) {
            document.getElementById('errNgaySinh').innerText = "Ngày sinh không hợp lệ. Vui lòng nhập lại."; hasError = true;
        }
    }

    if (!data.sdt) {
        document.getElementById('errSdt').innerText = "Số điện thoại trống. Vui lòng nhập số điện thoại."; hasError = true;
    } else if (!/^0\d{9}$/.test(data.sdt)) {
        document.getElementById('errSdt').innerText = "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng số 0 và chỉ chứa số."; hasError = true;
    }

    if (!data.cccd) {
        document.getElementById('errCccd').innerText = "Số CCCD trống. Vui lòng nhập số CCCD."; hasError = true;
    } else if (!/^\d{12}$/.test(data.cccd)) {
        document.getElementById('errCccd').innerText = "Số CCCD không hợp lệ."; hasError = true;
    }

    if (!data.que_quan) { document.getElementById('errQueQuan').innerText = "Quê quán trống. Vui lòng nhập quê quán."; hasError = true; }

    if (hasError) return;

    try {
        const method = editingId ? 'PUT' : 'POST';
        const url = editingId ? `${API_BASE_URL}/api/khach_thue/${editingId}` : `${API_BASE_URL}/api/khach_thue`;

        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        const result = await response.json();
        if (result.success) {
            closeModal('modal-form');
            showStatus('success', editingId ? 'Cập nhật thông tin khách thuê thành công' : 'Thêm khách thuê thành công');
            loadTenants(document.getElementById('searchInput').value);
        } else {
            const msg = (result.message || '').toLowerCase();

            if (msg.includes('cccd') && (msg.includes('tồn tại') || msg.includes('trùng'))) {
                document.getElementById('errCccd').innerText = "Số CCCD đã tồn tại. Vui lòng kiểm tra lại.";
            } else {
                document.getElementById('errFormGeneral').innerText = result.message || "Lỗi không xác định!";
            }
        }
    } catch (err) {
        console.error("Lỗi gọi API:", err);
        document.getElementById('errFormGeneral').innerText =
            "Không kết nối được server. Vui lòng kiểm tra lại!";
    }
};

// ==========================================
// XÓA KHÁCH THUÊ
// ==========================================
function openDeleteConfirm(id) {
    deletingId = id;
    document.getElementById('errDelete').innerText = '';
    openModal('modal-delete');
}

document.getElementById('btnConfirmDelete').onclick = async () => {
    if (!deletingId) return;
    try {
        const response = await fetch(`${API_BASE_URL}/api/khach_thue/${deletingId}`, { method: 'DELETE' });
        const result = await response.json();

        if (result.success) {
            closeModal('modal-delete');
            showStatus('success', 'Xóa khách thuê thành công.');
            loadTenants(document.getElementById('searchInput').value);
        } else {
            // "Không thể xóa vì khách thuê đang còn hợp đồng thuê."
            document.getElementById('errDelete').innerText = result.message;
            if (result.message.includes('lỗi')) {
                closeModal('modal-delete');
                showStatus('fatal', 'Hệ thống đang lỗi, không thể xóa. Vui lòng thử lại sau.');
            }
        }
    } catch (err) {
        closeModal('modal-delete');
        showStatus('fatal', 'Hệ thống đang lỗi, không thể xóa. Vui lòng thử lại sau.');
    }
};

// Boot
loadTenants();