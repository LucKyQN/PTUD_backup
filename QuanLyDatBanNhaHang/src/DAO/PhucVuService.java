package DAO;

import Entity.MonAn;
import Model.BanAnModel;
import Model.MonAnModel;

import java.util.List;

public interface PhucVuService {

	List<BanAnModel> getDanhSachBanChuaThanhToan();

	List<MonAnModel> getChiTietHoaDon(String maHD);

	List<BanAnModel> getDanhSachBanCanPhucVu();

	// Hàm lấy món ăn (thông minh: tự biết lấy từ Hóa đơn hay Phiếu đặt)
	List<MonAnModel> getMonAnTheoBan(String maBan, String trangThai);

	// Trong file PhucVuService.java

	boolean capNhatSoLuongMon(String maHD, String maMonAn, int soLuongMoi);

	boolean xoaMonKhoiChiTiet(String maHD, String maMonAn);

	// Trong file PhucVuService.java
	boolean capNhatTrangThaiMon(int idCTHD, String trangThaiMoi);

	boolean yeuCauThanhToan(String maHD, String maBan);

	boolean themHoacTangMon(String maHD, String maMonAn, int sl, String ghiChu);

	/** Món đang kinh doanh (đổ vào combo thêm món). */
	List<MonAn> getMonAnDangPhucVu();
}
