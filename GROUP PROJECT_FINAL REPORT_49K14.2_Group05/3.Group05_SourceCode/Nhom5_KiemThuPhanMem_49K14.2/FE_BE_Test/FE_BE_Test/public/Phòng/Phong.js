const API_BASE_URL = 'http://localhost:3000';
let currentRooms = [];
let editingId = null;
let deletingId = null;

const INP_MAPHONG = document.getElementById('inpMaPhong');
const INP_DIENTICH = document.getElementById('inpDienTich');
const INP_SONGUOI = document.getElementById('inpSoNguoi');
const INP_GIATHUE = document.getElementById('inpGiaThue');
const INP_TRANGTHAI = document.getElementById('inpTrangThai');
const INP_MOTA = document.getElementById('inpMoTa');

function toggleLogout(event) {
    if (event) event.stopPropagation();
    const menu = document.getElementById('logoutMenu');
    if (menu) {
        menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
    }
}

document.addEventListener('click', function (e) {
    const menu = document.getElementById('logoutMenu');
    const userInfo = document.querySelector('.user-info');
    if (menu && userInfo && !userInfo.contains(e.target)) {
        menu.style.display = 'none';
    }
});

function openModal(id) { document.getElementById(id).style.display = 'block'; }
function closeModal(id) { document.getElementById(id).style.display = 'none'; }

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

function formatCurrency(num) {
    return Number(num).toLocaleString('vi-VN');
}

async function loadRooms() {
    try {
        const searchQuery = document.getElementById('searchInput').value.trim();

        const response = await fetch(`${API_BASE_URL}/api/phong${searchQuery ? '?search=' + encodeURIComponent(searchQuery) : ''}`);
        const data = await response.json();
        
        const container = document.getElementById('roomContainer');
        container.innerHTML = '';
        const searchError = document.getElementById('search-error');
        searchError.innerText = '';

        let total = 0, empty = 0, rented = 0;

        if (data.success) {
            currentRooms = data.data; // Cập nhật biến toàn cục
            let rooms = currentRooms;
            total = rooms.length;

            rooms.forEach(r => {
                if (r.trang_thai === 'Trống') empty++;
                else rented++;
            });

            if (rooms.length === 0) {
                searchError.innerText = searchQuery ? "Phòng không tồn tại" : "Danh sách trống";
            } else {
                // Sắp xếp các phòng theo tên
                rooms.sort((a, b) => a.ma_phong.localeCompare(b.ma_phong, undefined, {numeric: true, sensitivity: 'base'}));

                rooms.forEach(room => {
                    const isRented = room.trang_thai === 'Đang thuê';
                    const tagStyle = isRented 
                        ? 'background:#dcfce7; color:#15803d; padding:4px 10px; border-radius:4px; font-size:12px; font-weight:bold;' 
                        : 'background:#fef2f2; color:#ef4444; padding:4px 10px; border-radius:4px; font-size:12px; font-weight:bold;';

                    const card = document.createElement('div');
                    card.className = 'room-card'; // Dùng class để định nghĩa border-top trong CSS
                    card.innerHTML = `
                        <div style="padding:15px; border-bottom:1px solid #eee; display:flex; justify-content:space-between; align-items:center;">
                            <div style="display:flex; align-items:center; flex: 1;">
                                <i class="fa-solid fa-house" style="background:#fef2f2; color:#DB000A; padding:8px; border-radius:50%; margin-right:10px;"></i>
                                <span style="font-weight:bold; font-size:18px;">${room.ma_phong}</span>
                            </div>
                            <div style="display:flex; align-items:center; gap:10px;">
                                <span style="${tagStyle}">${room.trang_thai}</span>
                                <i class="fa-solid fa-pen-to-square" style="color:#DB000A; cursor:pointer;" onclick="openEditModal('${room.ma_phong}', event)"></i>
                                <i class="fa-solid fa-trash-can" style="color:#DB000A; cursor:pointer;" onclick="openDeleteConfirm('${room.ma_phong}', event)"></i>
                            </div>
                        </div>
                        <div style="padding:15px; color:#555; line-height:1.6;">
                            <p style="margin:0;"><i class="fa-solid fa-vector-square" style="width:20px;"></i> Diện tích: ${room.dien_tich} m²</p>
                            <p style="margin:5px 0;"><i class="fa-solid fa-user-group" style="width:20px;"></i> Số lượng người tối đa: ${room.so_nguoi_toi_da}</p>
                            <p style="margin:0;"><i class="fa-solid fa-money-bill" style="width:20px;"></i> Giá thuê: <span style="color:#DB000A; font-weight:bold;">${formatCurrency(room.gia_thue)}</span></p>
                            <div style="margin-top:10px; padding-top:10px; border-top:1px dashed #ddd; font-size:13px; color:#777;">
                                <i class="fa-solid fa-bookmark"></i> ${room.mo_ta || 'Không có mô tả'}
                            </div>
                        </div>
                    `;
                    container.appendChild(card);
                });
            }
        }
        
        document.getElementById('statTotal').innerText = total;
        document.getElementById('statEmpty').innerText = empty;
        document.getElementById('statRented').innerText = rented;

    } catch (err) {
        showStatus('fatal', 'Có lỗi trong quá trình tải dữ liệu');
    }
}

document.getElementById('searchInput').addEventListener('input', loadRooms);


function clearFormErrors() {
    ['errMaPhong', 'errDienTich', 'errSoNguoi', 'errGiaThue', 'errFormGeneral'].forEach(id => {
        document.getElementById(id).innerText = '';
    });
}

function clearFormValues() {
    INP_MAPHONG.value = '';
    INP_DIENTICH.value = '';
    INP_SONGUOI.value = '';
    INP_GIATHUE.value = '';
    INP_TRANGTHAI.value = 'Trống';
    INP_MOTA.value = '';
}

window.openAddModal = () => {
    document.getElementById('modalTitle').innerText = "Thêm phòng";
    editingId = null;
    clearFormValues();
    clearFormErrors();
    INP_MAPHONG.readOnly = false;
    INP_MAPHONG.classList.remove('bg-gray-input');
    openModal('modal-form');
};

window.openEditModal = (id, event) => {
    if (event) event.stopPropagation();
    const r = currentRooms.find(x => x.ma_phong === id);
    if (!r) return;

    document.getElementById('modalTitle').innerText = `Chỉnh sửa phòng - ${id}`;
    editingId = id;
    clearFormErrors();

    INP_MAPHONG.value = r.ma_phong;
    INP_MAPHONG.readOnly = true;
    INP_MAPHONG.classList.add('bg-gray-input');

    INP_DIENTICH.value = r.dien_tich;
    INP_SONGUOI.value = r.so_nguoi_toi_da;
    INP_GIATHUE.value = r.gia_thue;
    INP_TRANGTHAI.value = r.trang_thai;
    INP_MOTA.value = r.mo_ta || '';

    openModal('modal-form');
};

window.promptCancelForm = () => {
    openModal('modal-cancel-confirm');
};

window.confirmCancelForm = () => {
    closeModal('modal-cancel-confirm');
    closeModal('modal-form');
};

document.getElementById('btnSaveAction').onclick = async () => {
    clearFormErrors();
    let hasError = false;

    const data = {
        ma_phong: INP_MAPHONG.value.trim(),
        dien_tich: INP_DIENTICH.value.trim(),
        so_nguoi_toi_da: INP_SONGUOI.value.trim(),
        gia_thue: INP_GIATHUE.value.trim(),
        mo_ta: INP_MOTA.value.trim(),
        trang_thai: INP_TRANGTHAI.value
    };


    if (!editingId) {
        if (!data.ma_phong) {
            document.getElementById('errMaPhong').innerText = "Mã phòng trống. Vui lòng nhập mã phòng.";
            hasError = true;
        } else if (data.ma_phong.length > 6) {
            document.getElementById('errMaPhong').innerText = "Mã phòng chỉ được nhập tối đa 6 ký tự.";
            hasError = true;
        }
    }

    if (!data.dien_tich) {
        document.getElementById('errDienTich').innerText = "Diện tích trống. Vui lòng nhập diện tích."; hasError = true;
    } else if (isNaN(data.dien_tich) || Number(data.dien_tich) <= 0) {
        document.getElementById('errDienTich').innerText = "Diện tích phòng phải là số và lớn hơn 0. Vui lòng nhập lại."; hasError = true;
    }

    if (!data.so_nguoi_toi_da) {
        document.getElementById('errSoNguoi').innerText = "Số lượng tối đa trống. Vui lòng nhập số lượng người tối đa."; hasError = true;
    } else if (isNaN(data.so_nguoi_toi_da) || Number(data.so_nguoi_toi_da) <= 0) {
        document.getElementById('errSoNguoi').innerText = "Số lượng người tối đa phải là số và lớn hơn 0. Vui lòng nhập lại."; hasError = true;
    }

    if (!data.gia_thue) {
        document.getElementById('errGiaThue').innerText = "Giá thuê trống. Vui lòng nhập giá thuê phòng."; hasError = true;
    } else if (isNaN(data.gia_thue) || Number(data.gia_thue) <= 0) {
        document.getElementById('errGiaThue').innerText = "Giá thuê phòng hiện tại phải là số và lớn hơn 0. Vui lòng nhập lại."; hasError = true;
    }

    if (hasError) return;

    try {
        const method = editingId ? 'PUT' : 'POST';
        const url = editingId ? `${API_BASE_URL}/api/phong/${editingId}` : `${API_BASE_URL}/api/phong`;

        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        const result = await response.json();
        if (result.success) {
            closeModal('modal-form');
            showStatus('success', editingId ? 'Cập nhật thông tin phòng thành công' : 'Thêm phòng mới thành công');
            loadRooms(document.getElementById('searchInput').value);
        } else {
            if (result.message.includes('Mã phòng đã tồn tại')) {
                document.getElementById('errMaPhong').innerText = "Mã phòng đã tồn tại. Vui lòng nhập mã khác.";
            } else {
                document.getElementById('errFormGeneral').innerText = "Hệ thống đang lỗi, không thể " + (editingId ? "cập nhật thông tin phòng" : "thêm phòng") + ". Vui lòng thử lại sau.";
            }
        }
    } catch (err) {
        document.getElementById('errFormGeneral').innerText = "Hệ thống đang lỗi, không thể " + (editingId ? "cập nhật thông tin phòng" : "thêm phòng") + ". Vui lòng thử lại sau.";
    }
};

window.openDeleteConfirm = (id, event) => {
    if (event) event.stopPropagation();
    deletingId = id;
    document.getElementById('errDelete').innerText = '';
    openModal('modal-delete');
};

document.getElementById('btnConfirmDelete').onclick = async () => {
    if (!deletingId) return;
    try {
        const response = await fetch(`${API_BASE_URL}/api/phong/${deletingId}`, { method: 'DELETE' });
        const result = await response.json();

        if (result.success) {
            closeModal('modal-delete');
            showStatus('success', 'Xóa phòng thành công.');
            loadRooms(document.getElementById('searchInput').value);
        } else {
            document.getElementById('errDelete').innerText = result.message;
            if (result.message.includes('Hệ thống đang lỗi')) {
                closeModal('modal-delete');
                showStatus('fatal', 'Hệ thống đang lỗi, không thể xóa. Vui lòng thử lại sau.');
            }
        }
    } catch (err) {
        closeModal('modal-delete');
        showStatus('fatal', 'Hệ thống đang lỗi, không thể xóa. Vui lòng thử lại sau.');
    }
};

// Khởi tạo
loadRooms();