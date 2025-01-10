package com.foodygo.enums;

public enum OrderStatus {
    PREPARING, // Cửa hàng nhận đơn
    SHIPPING, // Cửa hàng đưa shipper
    ARRIVED, // Nhân viên Hub xác nhận đã tới Hub
    COMPLETED, // Nhân viên Hub xác nhận + bằng chứng
    CANCELLED, // Nhân viên Hub sau 30 phút huỷ KHÔNG HOÀN TIỀN
}
