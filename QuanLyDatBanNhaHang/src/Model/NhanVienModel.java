package Model;

public class NhanVienModel {
	public String maNV;
	public String tenNV;
	public String chucVu;
	public String taiKhoan;
	public String matKhau;

	// Constructor mặc định
	public NhanVienModel() {
	}

	// Constructor đầy đủ (tùy chọn)
	public NhanVienModel(String maNV, String tenNV, String chucVu) {
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.chucVu = chucVu;
	}

	public String getMaNV() {
		return this.maNV;
	}

	public String getTenNV() {
		return this.tenNV;
	}
}