package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import DAO.MonAnDAO;
import Entity.MonAn;
import java.util.List;

public class FrmTaoDatCho extends JDialog {

	private static final Color RED_MAIN = new Color(220, 38, 38);
	private static final Color BORDER_CLR = new Color(230, 230, 230);
	private static final Color TEXT_DARK = new Color(40, 40, 40);
	private static final Color TEXT_GRAY = new Color(120, 120, 120);

	// Các biến toàn cục
	private DefaultTableModel tbModelDaChon;
	private JLabel lblTongTien;
	private int tongTien = 0;

	// TỐI ƯU 1: Đổi JComboBox để chứa Object thay vì String (giúp lấy mã bàn dễ
	// dàng)
	private JComboBox<ComboItem> cbBan;

	private JTextField txtTenKhach;
	private JTextField txtSDT;
	private JTextField txtSoLuong;
	private JTextField txtThoiGian;
	private JTextArea txtNote;

	public FrmTaoDatCho(JFrame parent) {
		super(parent, true); // Modal
		setUndecorated(true);
		setSize(950, 700);
		setLocationRelativeTo(parent);

		JPanel root = new JPanel(new BorderLayout());
		root.setBackground(Color.WHITE);
		root.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));

		root.add(createHeader(), BorderLayout.NORTH);
		root.add(createBody(), BorderLayout.CENTER);
		root.add(createFooter(), BorderLayout.SOUTH);

		setContentPane(root);
	}

	// --- 1. HEADER ---
	private JPanel createHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(new EmptyBorder(20, 30, 20, 30));

		JLabel title = new JLabel("Tạo đặt chỗ mới");
		title.setFont(new Font("Segoe UI", Font.BOLD, 22));

		JButton btnClose = new JButton("✕");
		btnClose.setFont(new Font("Segoe UI", Font.BOLD, 18));
		btnClose.setContentAreaFilled(false);
		btnClose.setBorderPainted(false);
		btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnClose.addActionListener(e -> this.dispose());

		header.add(title, BorderLayout.WEST);
		header.add(btnClose, BorderLayout.EAST);

		JPanel bottomLine = new JPanel(new BorderLayout());
		bottomLine.add(header, BorderLayout.CENTER);
		bottomLine.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR));

		return bottomLine;
	}

	// --- 2. BODY ---
	private JPanel createBody() {
		JPanel body = new JPanel(new GridLayout(1, 2, 40, 0));
		body.setBackground(Color.WHITE);
		body.setBorder(new EmptyBorder(20, 30, 20, 30));

		// ==========================================
		// CỘT TRÁI: THÔNG TIN KHÁCH HÀNG
		// ==========================================
		JPanel pnlLeft = new JPanel();
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
		pnlLeft.setBackground(Color.WHITE);

		JLabel lblInfoTitle = new JLabel("Thông tin khách hàng");
		lblInfoTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
		pnlLeft.add(lblInfoTitle);
		pnlLeft.add(Box.createVerticalStrut(15));

		txtTenKhach = new JTextField();
		txtSDT = new JTextField();
		pnlLeft.add(createInputGroup("Tên khách hàng *", txtTenKhach));
		pnlLeft.add(Box.createVerticalStrut(15));
		pnlLeft.add(createInputGroup("Số điện thoại *", txtSDT));
		pnlLeft.add(Box.createVerticalStrut(15));

		JPanel rowTime = new JPanel(new GridLayout(1, 2, 15, 0));
		rowTime.setBackground(Color.WHITE);

		txtSoLuong = new JTextField("2");
		txtThoiGian = new JTextField("--:-- --");
		rowTime.add(createInputGroup("Số lượng khách *", txtSoLuong));
		rowTime.add(createInputGroup("Thời gian *", txtThoiGian));
		rowTime.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		pnlLeft.add(rowTime);
		pnlLeft.add(Box.createVerticalStrut(15));

		// TỐI ƯU 1: Load danh sách bàn trống vào ComboBox
		DAO.BanAnDAO dao = new DAO.BanAnDAO();
		List<Entity.BanAn> dsBan = dao.getAllBanAn();

		cbBan = new JComboBox<>();
		cbBan.addItem(new ComboItem("", "Chọn bàn"));

		for (Entity.BanAn ban : dsBan) {
			// CHỈ HIỂN THỊ NHỮNG BÀN ĐANG "Trống" ĐỂ ĐẶT
			if (ban.getTrangThai() != null && ban.getTrangThai().equals("Trống")) {
				cbBan.addItem(new ComboItem(ban.getMaBan(), ban.getMaBan() + " - " + ban.getTenBan()));
			}
		}

		pnlLeft.add(createInputGroup("Chọn bàn *", cbBan));
		pnlLeft.add(Box.createVerticalStrut(15));

		txtNote = new JTextArea();
		txtNote.setLineWrap(true);
		txtNote.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
		JScrollPane scrollNote = new JScrollPane(txtNote);
		scrollNote.setPreferredSize(new Dimension(0, 80));
		pnlLeft.add(createInputGroup("Ghi chú", scrollNote));

		// CỘT PHẢI: CHIA LÀM 2 NỬA (MENU VÀ GIỎ HÀNG)
		JPanel pnlRight = new JPanel(new GridLayout(2, 1, 0, 15));
		pnlRight.setBackground(Color.WHITE);

		// NỬA TRÊN: Danh sách thực đơn
		JPanel pnlMenu = new JPanel(new BorderLayout(0, 5));
		pnlMenu.setBackground(Color.WHITE);
		JLabel lblMenuTitle = new JLabel("Thực đơn (Tùy chọn)");
		lblMenuTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
		pnlMenu.add(lblMenuTitle, BorderLayout.NORTH);

		JPanel listFood = new JPanel();
		listFood.setLayout(new BoxLayout(listFood, BoxLayout.Y_AXIS));
		listFood.setBackground(Color.WHITE);

		MonAnDAO monAnDAO = new MonAnDAO();
		List<MonAn> dsMon = monAnDAO.getAllMonAn();

		for (MonAn mon : dsMon) {
			// chỉ lấy món đang phục vụ
			if (!mon.isTinhTrang())
				continue;

			String ten = mon.getTenMon();
			int gia = (int) mon.getGiaMon();
			String icon = getIconByName(ten);

			listFood.add(createFoodItem(icon, ten, gia, "Món ăn"));
			listFood.add(Box.createVerticalStrut(10));
		}

		JScrollPane scrollFood = new JScrollPane(listFood);
		scrollFood.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
		// Tùy chỉnh thanh cuộn cho mượt
		scrollFood.getVerticalScrollBar().setUnitIncrement(16);
		pnlMenu.add(scrollFood, BorderLayout.CENTER);

		// NỬA DƯỚI: Bảng các món đã chọn (Giỏ hàng)
		JPanel pnlCart = new JPanel(new BorderLayout(0, 5));
		pnlCart.setBackground(Color.WHITE);

		JLabel lblCartTitle = new JLabel("Món đã chọn");
		lblCartTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
		pnlCart.add(lblCartTitle, BorderLayout.NORTH);

		String[] columns = { "Tên món", "SL", "Đơn giá" };
		tbModelDaChon = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable tbDaChon = new JTable(tbModelDaChon);
		tbDaChon.setRowHeight(25);
		tbDaChon.getColumnModel().getColumn(1).setPreferredWidth(30);
		JScrollPane scrollCart = new JScrollPane(tbDaChon);
		scrollCart.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
		pnlCart.add(scrollCart, BorderLayout.CENTER);

		lblTongTien = new JLabel("Tổng cộng: 0 đ");
		lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTongTien.setForeground(RED_MAIN);
		lblTongTien.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlCart.add(lblTongTien, BorderLayout.SOUTH);

		pnlRight.add(pnlMenu);
		pnlRight.add(pnlCart);

		body.add(pnlLeft);
		body.add(pnlRight);
		return body;
	}

	private String getIconByName(String ten) {
		if (ten == null)
			return "🍽️";
		ten = ten.toLowerCase();

		if (ten.contains("bò") || ten.contains("heo") || ten.contains("nướng"))
			return "🥩";
		if (ten.contains("gà") || ten.contains("vịt"))
			return "🍗";
		if (ten.contains("lẩu") || ten.contains("canh"))
			return "🍲";
		if (ten.contains("bia") || ten.contains("nước") || ten.contains("trà"))
			return "🥤";
		if (ten.contains("salad") || ten.contains("rau"))
			return "🥗";

		return "🍽️";
	}

	// --- 3. FOOTER ---
	private JPanel createFooter() {
		JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
		footer.setBackground(Color.WHITE);
		footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR));

		JButton btnHuy = new JButton("Hủy");
		btnHuy.setPreferredSize(new Dimension(150, 40));
		btnHuy.setBackground(Color.WHITE);
		btnHuy.setFocusPainted(false);
		btnHuy.addActionListener(e -> this.dispose());

		JButton btnXacNhan = new JButton("Xác nhận đặt chỗ");
		btnXacNhan.setPreferredSize(new Dimension(200, 40));
		btnXacNhan.setBackground(RED_MAIN);
		btnXacNhan.setForeground(Color.WHITE);
		btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnXacNhan.setFocusPainted(false);
		btnXacNhan.setBorderPainted(false);

		// --- XỬ LÝ LƯU DATABASE & ĐỔI MÀU BÀN 
		btnXacNhan.addActionListener(e -> {

			String tenKhach = txtTenKhach.getText().trim();
			String sdt = txtSDT.getText().trim();
			String soLuongStr = txtSoLuong.getText().trim();
			String thoiGianStr = txtThoiGian.getText().trim();
			String ghiChu = txtNote.getText().trim();

			if (tenKhach.isEmpty() || sdt.isEmpty() || soLuongStr.isEmpty() || thoiGianStr.isEmpty()
					|| thoiGianStr.equals("--:-- --")) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc (*)", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			ComboItem selectedBan = (ComboItem) cbBan.getSelectedItem();
			if (selectedBan == null || selectedBan.getKey().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn để đặt!", "Lưu ý", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int soLuongKhach;
			try {
				soLuongKhach = Integer.parseInt(soLuongStr);
				if (soLuongKhach <= 0)
					throw new Exception();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Số lượng khách phải là số nguyên dương!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			java.util.Date thoiGianDen;
			try {
				// Ép kiểu thời gian theo định dạng chuẩn: Năm-Tháng-Ngày Giờ:Phút
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
				thoiGianDen = sdf.parse(thoiGianStr);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this,
						"Thời gian không đúng định dạng (yyyy-MM-dd HH:mm)!\nVí dụ: 2026-04-02 19:30", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			String maBan = selectedBan.getKey();
			String tenHienThi = selectedBan.getValue();

			// 2. Đóng gói dữ liệu vào Entity PhieuDatBan
			Entity.PhieuDatBan phieu = new Entity.PhieuDatBan();
			phieu.setMaPhieu("PDB" + System.currentTimeMillis()); // Sinh mã phiếu tự động bằng thời gian thực
			phieu.setTenKhachHang(tenKhach);
			phieu.setSoDienThoai(sdt);
			phieu.setSoLuongKhach(soLuongKhach);
			phieu.setThoiGianDen(thoiGianDen);
			phieu.setGhiChu(ghiChu);
			phieu.setMaBan(maBan);

			// 3. Gọi DAO để lưu xuống Database
			DAO.PhieuDatBanDAO phieuDAO = new DAO.PhieuDatBanDAO();
			DAO.BanAnDAO banDAO = new DAO.BanAnDAO();

			if (phieuDAO.taoPhieuDatBan(phieu)) {
				// Nếu lưu Phiếu thành công -> Cập nhật trạng thái Bàn thành "Đã đặt"
				if (banDAO.capNhatTrangThai(maBan, "Đã đặt")) {
					JOptionPane.showMessageDialog(this,
							"✅ Đặt chỗ thành công cho " + tenKhach + " tại " + tenHienThi + "!");

					// F5 lại giao diện Sơ đồ bàn của Lễ tân (cập nhật màu bàn ngay lập tức)
					JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
					if (parentFrame instanceof FrmLeTan) {
						((FrmLeTan) parentFrame).refreshSoDoBan();
						((FrmLeTan) parentFrame).loadDanhSachDatCho();
					}

					this.dispose(); // Đóng popup
				} else {
					JOptionPane.showMessageDialog(this, "Lỗi: Đã tạo phiếu nhưng không thể cập nhật trạng thái bàn!",
							"Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "❌ Lỗi: Không thể lưu thông tin đặt chỗ vào cơ sở dữ liệu!",
						"Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
			}
		});

		footer.add(btnHuy);
		footer.add(btnXacNhan);
		return footer;
	}

	// --- HÀM HỖ TRỢ TẠO GIAO DIỆN ---
	private JPanel createInputGroup(String title, JComponent input) {
		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.setBackground(Color.WHITE);
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

		JLabel lbl = new JLabel(title);
		lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		if (input instanceof JTextField) {
			((JTextField) input).setPreferredSize(new Dimension(0, 35));
			input.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_CLR),
					new EmptyBorder(0, 10, 0, 10)));
		} else if (input instanceof JComboBox) {
			input.setPreferredSize(new Dimension(0, 35));
		}

		panel.add(lbl, BorderLayout.NORTH);
		panel.add(input, BorderLayout.CENTER);
		return panel;
	}

	private JPanel createFoodItem(String emoji, String name, int price, String category) {
		JPanel item = new JPanel(new BorderLayout(10, 0));
		item.setBackground(Color.WHITE);
		item.setBorder(new EmptyBorder(10, 10, 10, 10));
		item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

		JLabel lblImg = new JLabel(emoji, SwingConstants.CENTER);
		lblImg.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
		lblImg.setPreferredSize(new Dimension(60, 60));
		lblImg.setBorder(BorderFactory.createLineBorder(BORDER_CLR));

		JPanel info = new JPanel(new GridLayout(3, 1));
		info.setBackground(Color.WHITE);
		JLabel lblName = new JLabel(name);
		lblName.setFont(new Font("Segoe UI", Font.BOLD, 13));

		JLabel lblPrice = new JLabel(formatMoney(price));
		lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblPrice.setForeground(RED_MAIN);

		JLabel lblCat = new JLabel(category);
		lblCat.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblCat.setForeground(TEXT_GRAY);
		info.add(lblName);
		info.add(lblPrice);
		info.add(lblCat);

		JButton btnAdd = new JButton("Thêm");
		btnAdd.setBackground(RED_MAIN);
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setFocusPainted(false);
		btnAdd.setBorderPainted(false);

		btnAdd.addActionListener(e -> {
			boolean isExist = false;
			for (int i = 0; i < tbModelDaChon.getRowCount(); i++) {
				if (tbModelDaChon.getValueAt(i, 0).equals(name)) {
					int slCu = (int) tbModelDaChon.getValueAt(i, 1);
					tbModelDaChon.setValueAt(slCu + 1, i, 1);
					isExist = true;
					break;
				}
			}

			if (!isExist) {
				tbModelDaChon.addRow(new Object[] { name, 1, formatMoney(price) });
			}

			tongTien += price;
			lblTongTien.setText("Tổng cộng: " + formatMoney(tongTien));
		});

		item.add(lblImg, BorderLayout.WEST);
		item.add(info, BorderLayout.CENTER);
		item.add(btnAdd, BorderLayout.EAST);

		return item;
	}

	private String formatMoney(int amount) {
		return String.format("%,d đ", amount).replace(',', '.');
	}

	// CLASS PHỤ TRỢ: CHỨA ID VÀ TÊN CHO COMBOBOX
	class ComboItem {
		private String key;
		private String value;

		public ComboItem(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return value; // Dòng chữ sẽ hiển thị trên giao diện ComboBox
		}
	}
}