const API_BASE_URL = 'http://localhost:3000';
let currentContracts = [];
let availableRooms = [];
let allTenants = [];
let deletingId = null;

let maxNguoi = 0;
let selectedTenants = []; // Array of tenant objects { id, name }

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

function formatDateDisplay(dateStr) {
    if (!dateStr) return '';
    const parts = dateStr.split('-');
    if (parts.length === 3) return `${parts[2]}-${parts[1]}-${parts[0]}`;
    return dateStr;
}

async function loadContracts(searchQuery = '') {
    try {
        const response = await fetch(`${API_BASE_URL}/api/hop_dong${searchQuery ? '?search=' + encodeURIComponent(searchQuery) : ''}`);
        const data = await response.json();

        const container = document.getElementById('contractContainer');
        container.innerHTML = '';
        const searchError = document.getElementById('search-error');
        searchError.innerText = '';

        if (data.success) {
            currentContracts = data.data;
            if (currentContracts.length === 0 && searchQuery) {
                searchError.innerText = "Không tìm thấy hợp đồng nào phù hợp.";
            } else {
                currentContracts.forEach(contract => {
                    const tr = document.createElement('tr');
                    tr.style.cursor = 'pointer';
                    // Determine status based on dates
                    const today = new Date();
                    const end = new Date(contract.ngay_ket_thuc);
                    let isExpired = end < today;
                    let tagClass = isExpired ? 'background:#fef2f2; color:#ef4444;' : 'background:#dcfce7; color:#15803d;';
                    let tagText = isExpired ? 'Hết Hợp Đồng' : 'Còn Hợp Đồng';

                    // find the phone number of dai_dien
                    let daiDienPhone = 'N/A';
                    if (contract.khach_thue_list && contract.khach_thue_list.length > 0) {
                        const dd = contract.khach_thue_list.find(kt => kt.ma_khach_thue === contract.ma_khach_thue);
                        if (dd) daiDienPhone = dd.sdt;
                    }

                    tr.innerHTML = `
                        <td style="padding:15px; border-bottom:1px solid #f0f0f0;" onclick="showContractDetail('${contract.ma_hop_dong}')">${contract.ma_hop_dong}</td>
                        <td style="padding:15px; border-bottom:1px solid #f0f0f0; color:#DB000A; font-weight:bold;" onclick="showContractDetail('${contract.ma_hop_dong}')">${contract.ma_phong}</td>
                        <td style="padding:15px; border-bottom:1px solid #f0f0f0;" onclick="showContractDetail('${contract.ma_hop_dong}')">${contract.ten_khach_thue}</td>
                        <td style="padding:15px; border-bottom:1px solid #f0f0f0;" onclick="showContractDetail('${contract.ma_hop_dong}')">${daiDienPhone}</td>
                        <td style="padding:15px; border-bottom:1px solid #f0f0f0;" onclick="showContractDetail('${contract.ma_hop_dong}')">${formatDateDisplay(contract.ngay_bat_dau)}</td>
                        <td style="padding:15px; border-bottom:1px solid #f0f0f0;" onclick="showContractDetail('${contract.ma_hop_dong}')">${formatDateDisplay(contract.ngay_ket_thuc)}</td>
                        <td style="padding:15px; border-bottom:1px solid #f0f0f0;" onclick="showContractDetail('${contract.ma_hop_dong}')">
                            <span style="padding:5px 10px; border-radius:4px; font-size:12px; font-weight:bold; ${tagClass}">${tagText}</span>
                        </td>
                        <td style="padding:15px; border-bottom:1px solid #f0f0f0; text-align:center;">
                            <button onclick="openDeleteConfirm('${contract.ma_hop_dong}', event)" style="background:none; border:none; cursor:pointer; color:#DB000A; font-size:18px;">
                                <i class="fa-solid fa-trash-can"></i>
                            </button>
                        </td>
                    `;
                    tr.onmouseenter = () => tr.style.backgroundColor = '#fafafa';
                    tr.onmouseleave = () => tr.style.backgroundColor = 'transparent';
                    container.appendChild(tr);
                });
            }
        }
    } catch (err) {
        showStatus('fatal', 'Có lỗi trong quá trình tải dữ liệu hợp đồng');
    }
}

document.getElementById('searchInput').addEventListener('input', (e) => {
    loadContracts(e.target.value);
});

// DETAIL VIEW
window.showContractDetail = function (id) {
    const c = currentContracts.find(x => x.ma_hop_dong === id);
    if (!c) return;

    document.getElementById('display-hd-id').innerText = c.ma_hop_dong;
    document.getElementById('detMaPhong').innerText = c.ma_phong;
    document.getElementById('detTienCoc').innerText = formatCurrency(c.tien_coc) + ' đ';
    document.getElementById('detNgayBD').innerText = formatDateDisplay(c.ngay_bat_dau);
    document.getElementById('detNgayKT').innerText = formatDateDisplay(c.ngay_ket_thuc);
    document.getElementById('detDieuKhoan').innerText = c.dieu_khoan;

    // Tenants list
    const tbody = document.getElementById('detTenantsList');
    tbody.innerHTML = '';
    if (c.khach_thue_list) {
        c.khach_thue_list.forEach(kt => {
            const isDaiDien = kt.ma_khach_thue === c.ma_khach_thue;
            const badgeClass = isDaiDien ? 'background:#dcfce7; color:#15803d;' : 'background:#eff6ff; color:#1d4ed8;';
            const badgeText = isDaiDien ? 'Người đại diện' : 'Thành viên';
            const iconBg = isDaiDien ? 'background:#fef3c7; color:#d97706;' : 'background:#f2f2f2; color:#666;';
            const iconHTML = isDaiDien ? '<i class="fa-solid fa-crown"></i>' : '<i class="fa-solid fa-user"></i>';

            const card = document.createElement('div');
            card.style.cssText = 'border:1px solid #eee; border-radius:8px; padding:15px; display:flex; gap:15px; align-items:flex-start; background: ' + (isDaiDien ? '#fffbeb' : '#ffffff');
            card.innerHTML = `
                <div style="${iconBg} width:40px; height:40px; border-radius:50%; display:flex; align-items:center; justify-content:center; font-size:20px; flex-shrink:0;">
                    ${iconHTML}
                </div>
                <div style="flex:1;">
                    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:10px;">
                        <span style="font-weight:bold; font-size:16px; color:#333;">${kt.ten_khach_thue}</span>
                        <span style="${badgeClass} padding:4px 8px; border-radius:4px; font-size:11px; font-weight:bold;">${badgeText}</span>
                    </div>
                    <div style="display:grid; grid-template-columns:1fr 1fr; gap:5px; font-size:14px; color:#555;">
                        <p style="margin:0;">SĐT: <strong>${kt.sdt}</strong></p>
                        <p style="margin:0;">CCCD: <strong>${kt.cccd}</strong></p>
                        <p style="margin:0;">Giới tính: <strong>${kt.gioi_tinh}</strong></p>
                        <p style="margin:0;">Ngày sinh: <strong>${formatDateDisplay(kt.ngay_sinh)}</strong></p>
                        <p style="margin:0;">Quê quán: <strong>${kt.que_quan}</strong></p>
                    </div>
                </div>
            `;
            tbody.appendChild(card);
        });
    }

    openModal('detailModal');
};

// CREATE FORM
function clearFormErrors() {
    ['errMaPhong', 'errKhachThue', 'errDaiDien', 'errNgayBD', 'errNgayKT', 'errTienCoc', 'errDieuKhoan', 'errFormGeneral'].forEach(id => {
        document.getElementById(id).innerText = '';
    });
}

window.openAddModal = async () => {
    document.getElementById('inpMaHD').value = "Mã tự động sinh";
    clearFormErrors();
    document.getElementById('inpNgayBD').value = '';
    document.getElementById('inpNgayKT').value = '';
    document.getElementById('inpTienCoc').value = '';
    document.getElementById('inpDieuKhoan').value = '';
    document.getElementById('khachThueInput').value = '';
    document.getElementById('txtSoNguoi').innerText = '0';
    document.getElementById('txtMaxNguoi').innerText = '0';
    document.getElementById('selDaiDien').innerHTML = '<option value="">-- Chọn người đại diện --</option>';

    selectedTenants = [];
    maxNguoi = 0;

    // Fetch rooms & tenants in parallel
    try {
        const [roomsRes, tenantsRes] = await Promise.all([
            fetch(`${API_BASE_URL}/api/phong`),
            fetch(`${API_BASE_URL}/api/khach_thue`)
        ]);

        const roomsData = await roomsRes.json();
        const tenantsData = await tenantsRes.json();

        if (roomsData.success) {
            availableRooms = roomsData.data.filter(r => r.trang_thai === 'Trống');
            const selMaPhong = document.getElementById('selMaPhong');
            selMaPhong.innerHTML = '<option value="">-- Chọn mã phòng --</option>';
            availableRooms.forEach(r => {
                selMaPhong.innerHTML += `<option value="${r.ma_phong}">${r.ma_phong} (Max: ${r.so_nguoi_toi_da} người)</option>`;
            });
        }

        if (tenantsData.success) {
            allTenants = tenantsData.data;
            const ktList = document.getElementById('ktListContainer');
            ktList.innerHTML = '';
            allTenants.forEach(kt => {
                ktList.innerHTML += `
                    <label style="display:flex; align-items:center; padding:12px 15px; border:1px solid #d1d5db; border-radius:0; cursor:pointer; background:white;">
                        <input type="checkbox" class="kt-checkbox" value="${kt.ma_khach_thue}" data-name="${kt.ten_khach_thue}" data-cccd="${kt.cccd}" style="margin-right:15px; width:16px; height:16px; cursor:pointer;">
                        <div style="display:flex; flex-direction:column;">
                            <span style="color:#333; font-size:15px; margin-bottom:3px;">${kt.ten_khach_thue} - CCCD: ${kt.cccd}</span>
                            <span style="color:#666; font-size:13px;">SĐT: ${kt.sdt}</span>
                        </div>
                    </label>
                `;
            });
        }

    } catch (err) {
        showStatus('error', 'Hệ thống lỗi khi tải danh sách phòng/khách thuê.');
    }

    openModal('contractModal');
};

document.getElementById('selMaPhong').addEventListener('change', function () {
    const val = this.value;
    if (!val) {
        maxNguoi = 0;
        document.getElementById('txtMaxNguoi').innerText = '0';
        return;
    }
    const r = availableRooms.find(x => x.ma_phong === val);
    if (r) {
        maxNguoi = r.so_nguoi_toi_da;
        document.getElementById('txtMaxNguoi').innerText = maxNguoi;
    }
});

window.openTenantPopup = () => {
    if (!document.getElementById('selMaPhong').value) {
        document.getElementById('errKhachThue').innerText = 'Vui lòng chọn phòng trước.';
        return;
    }
    document.getElementById('errKhachThue').innerText = '';
    document.getElementById('errKtPopup').innerText = '';

    // Restore checkbox states based on selectedTenants
    const cbs = document.querySelectorAll('.kt-checkbox');
    cbs.forEach(cb => {
        cb.checked = selectedTenants.some(t => t.id === cb.value);
    });

    openModal('ktModal');
};

window.confirmSelectKT = () => {
    const cbs = document.querySelectorAll('.kt-checkbox:checked');
    document.getElementById('errKtPopup').innerText = '';

    selectedTenants = [];
    cbs.forEach(cb => {
        selectedTenants.push({
            id: cb.value,
            name: cb.getAttribute('data-name'),
            cccd: cb.getAttribute('data-cccd')
        });
    });

    document.getElementById('khachThueInput').value = selectedTenants.map(t => `${t.name} (${t.cccd})`).join(', ');
    document.getElementById('txtSoNguoi').innerText = selectedTenants.length;

    // Populate Dại diện
    const selDaiDien = document.getElementById('selDaiDien');
    const oldVal = selDaiDien.value;
    selDaiDien.innerHTML = '<option value="">-- Chọn người đại diện --</option>';
    selectedTenants.forEach(t => {
        selDaiDien.innerHTML += `<option value="${t.id}">${t.name} - ${t.cccd}</option>`;
    });

    // Retain previous selection if valid
    if (selectedTenants.some(t => t.id === oldVal)) {
        selDaiDien.value = oldVal;
    }

    closeModal('ktModal');

    // Hiện lỗi xuống dưới Thêm KT nếu vượt quá
    if (selectedTenants.length > maxNguoi) {
        document.getElementById('errKhachThue').innerText = "Số lượng khách thuê không được vượt quá số lượng khách tối đa của phòng.";
    } else {
        document.getElementById('errKhachThue').innerText = "";
    }
};

window.promptCancelForm = () => openModal('modal-cancel-confirm');
window.confirmCancelForm = () => { closeModal('modal-cancel-confirm'); closeModal('contractModal'); };

document.getElementById('btnSaveAction').onclick = async () => {
    clearFormErrors();
    let hasError = false;

    const data = {
        ma_phong: document.getElementById('selMaPhong').value,
        khach_thue_ids: selectedTenants.map(t => t.id),
        ma_khach_thue: document.getElementById('selDaiDien').value,
        ngay_bat_dau: document.getElementById('inpNgayBD').value,
        ngay_ket_thuc: document.getElementById('inpNgayKT').value,
        tien_coc: document.getElementById('inpTienCoc').value,
        dieu_khoan: document.getElementById('inpDieuKhoan').value
    };

    if (!data.ma_phong) { document.getElementById('errMaPhong').innerText = "Vui lòng chọn phòng."; hasError = true; }
    if (data.khach_thue_ids.length === 0) {
        document.getElementById('errKhachThue').innerText = "Tên khách thuê trống. Vui lòng nhập tên khách thuê.";
        hasError = true;
    } else if (data.khach_thue_ids.length > maxNguoi) {
        document.getElementById('errKhachThue').innerText = "Số lượng khách thuê không được vượt quá số lượng khách tối đa của phòng.";
        hasError = true;
    }
    if (!data.ma_khach_thue) { document.getElementById('errDaiDien').innerText = "Vui lòng chọn người đại diện."; hasError = true; }

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (!data.ngay_bat_dau) {
        document.getElementById('errNgayBD').innerText = "Vui lòng chọn ngày bắt đầu."; hasError = true;
    } else {
        const bd = new Date(data.ngay_bat_dau);
        if (bd < today) {
            document.getElementById('errNgayBD').innerText = "Ngày bắt đầu không được nhỏ hơn ngày hiện tại."; hasError = true;
        }
    }

    if (!data.ngay_ket_thuc) {
        document.getElementById('errNgayKT').innerText = "Vui lòng chọn ngày kết thúc."; hasError = true;
    } else {
        const kt = new Date(data.ngay_ket_thuc);
        const bd = new Date(data.ngay_bat_dau);
        if (data.ngay_bat_dau && kt <= bd) {
            document.getElementById('errNgayKT').innerText = "Ngày kết thúc phải lớn hơn ngày bắt đầu."; hasError = true;
        }
    }

    if (!data.tien_coc || Number(data.tien_coc) <= 0) {
        document.getElementById('errTienCoc').innerText = "Tiền cọc phải là số và lớn hơn 0. Vui lòng nhập lại."; hasError = true;
    }

    if (!data.dieu_khoan.trim()) {
        document.getElementById('errDieuKhoan').innerText = "Điều khoản trống. Vui lòng nhập điều khoản."; hasError = true;
    }

    if (hasError) return;

    try {
        const response = await fetch(`${API_BASE_URL}/api/hop_dong`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const result = await response.json();

        if (result.success) {
            closeModal('contractModal');
            showStatus('success', 'Thêm hợp đồng thành công!');
            loadContracts(document.getElementById('searchInput').value);
        } else {
            document.getElementById('errFormGeneral').innerText = result.message || "Hệ thống đang lỗi, không thể thêm hợp đồng.";
        }
    } catch (err) {
        document.getElementById('errFormGeneral').innerText = "Hệ thống đang lỗi, không thể thêm hợp đồng. Vui lòng thử lại sau.";
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
        const response = await fetch(`${API_BASE_URL}/api/hop_dong/${deletingId}`, { method: 'DELETE' });
        const result = await response.json();

        if (result.success) {
            closeModal('modal-delete');
            showStatus('success', 'Xóa hợp đồng thành công.');
            loadContracts(document.getElementById('searchInput').value);
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
loadContracts();