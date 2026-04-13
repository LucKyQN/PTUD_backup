package DAO;

import Entity.PhieuDatBan;
import connectDatabase.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class PhieuDatBanDAO {

	private Connection getConnection() throws Exception {
		ConnectDB.getInstance().connect();
		return ConnectDB.getInstance().getConnection();
	}

	// Hàm thêm mới phiếu đặt bàn
	public boolean taoPhieuDatBan(PhieuDatBan phieu) {
		String sql = "INSERT INTO PhieuDatBan (maPhieu, tenKhachHang, soDienThoai, thoiGianDen, soLuongKhach, ghiChu, maBan, trangThai, ngayTao, tienMonDatTruoc, tienCoc) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, N'Chờ khách', GETDATE(), ?, ?)";
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, phieu.getMaPhieu());
			ps.setString(2, phieu.getTenKhachHang());
			ps.setString(3, phieu.getSoDienThoai());
			// Convert java.util.Date sang java.sql.Timestamp để lưu được cả ngày và giờ
			ps.setTimestamp(4, new Timestamp(phieu.getThoiGianDen().getTime()));
			ps.setInt(5, phieu.getSoLuongKhach());
			ps.setString(6, phieu.getGhiChu());
			ps.setString(7, phieu.getMaBan());
			ps.setDouble(8, phieu.getTienMonDatTruoc());
			ps.setDouble(9, phieu.getTienCoc());
			int rows = ps.executeUpdate();
			ps.close();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 1. Lấy danh sách phiếu đang "Chờ khách" (Hôm nay)
	public java.util.List<PhieuDatBan> getDanhSachDatChoChuaCheckIn() {
		java.util.List<PhieuDatBan> list = new java.util.ArrayList<>();
		// JOIN với bảng BanAn để lấy cái tên bàn hiển thị cho đẹp
		String sql = "SELECT p.*, b.tenBan FROM PhieuDatBan p " + "JOIN BanAn b ON p.maBan = b.maBan "
				+ "WHERE p.trangThai = N'Chờ khách' " + "ORDER BY p.thoiGianDen ASC";
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			java.sql.ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PhieuDatBan p = new PhieuDatBan();
				p.setMaPhieu(rs.getString("maPhieu"));
				p.setTenKhachHang(rs.getString("tenKhachHang"));
				p.setSoDienThoai(rs.getString("soDienThoai"));
				p.setThoiGianDen(rs.getTimestamp("thoiGianDen"));
				p.setSoLuongKhach(rs.getInt("soLuongKhach"));
				p.setGhiChu(rs.getString("ghiChu"));
				p.setMaBan(rs.getString("maBan"));
				p.setTrangThai(rs.getString("trangThai"));
				// Lấy thêm tên bàn từ câu JOIN
				p.setTenBan(rs.getString("tenBan"));
				p.setTienMonDatTruoc(rs.getDouble("tienMonDatTruoc"));
				p.setTienCoc(rs.getDouble("tienCoc"));
				list.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 2. Cập nhật trạng thái phiếu (VD: Khi khách tới thì đổi thành 'Đã đến')
	public boolean capNhatTrangThaiPhieu(String maPhieu, String trangThaiMoi) {
		String sql = "UPDATE PhieuDatBan SET trangThai = ? WHERE maPhieu = ?";
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, trangThaiMoi);
			ps.setString(2, maPhieu);
			int rows = ps.executeUpdate();
			ps.close();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}