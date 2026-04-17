package GUI;

import DAO.NhanVienDAO;
import Entity.LuuLog;
import Entity.NhanVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.RenderingHints;

public class FrmCaiDat extends JPanel {

	private static final Color BG_MAIN = new Color(243, 244, 246);
	private static final Color BG_CARD = Color.WHITE;
	private static final Color BORDER_CLR = new Color(229, 231, 235);
	private static final Color TEXT_TITLE = new Color(17, 24, 39);
	private static final Color TEXT_DARK = new Color(31, 41, 55);
	private static final Color TEXT_GRAY = new Color(107, 114, 128);
	private static final Color BTN_BG = new Color(15, 23, 42);

	private static final Font FONT_H2 = new Font("Segoe UI", Font.BOLD, 16);
	private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
	private static final Font FONT_VALUE = new Font("Segoe UI", Font.PLAIN, 15);

	private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
	private NhanVien nhanVienHienTai;

	public FrmCaiDat() {
		taiDuLieuNhanVien();
		initUI();
	}

	private void taiDuLieuNhanVien() {
		try {
			if (LuuLog.nhanVienDangNhap != null && LuuLog.nhanVienDangNhap.getMaNV() != null) {
				nhanVienHienTai = nhanVienDAO.getNhanVienTheoMa(LuuLog.nhanVienDangNhap.getMaNV());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initUI() {
		setLayout(new BorderLayout());
		setBackground(BG_MAIN);

		JPanel root = new JPanel(new BorderLayout(0, 24));
		root.setBackground(BG_MAIN);
		root.setBorder(new EmptyBorder(30, 40, 40, 40));

		JPanel bodyContent = new JPanel();
		bodyContent.setLayout(new BoxLayout(bodyContent, BoxLayout.Y_AXIS));
		bodyContent.setBackground(BG_MAIN);

		// Ai cũng thấy
		bodyContent.add(createAccountCard());

		// Chỉ quản lý mới thấy
		if (laQuanLy()) {
			bodyContent.add(Box.createVerticalStrut(24));
			bodyContent.add(createRestaurantCard());

			bodyContent.add(Box.createVerticalStrut(24));
			bodyContent.add(createSystemCard());
		}

		JPanel bodyWrapper = new JPanel(new BorderLayout());
		bodyWrapper.setBackground(BG_MAIN);
		bodyWrapper.add(bodyContent, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(bodyWrapper);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(BG_MAIN);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		root.add(scrollPane, BorderLayout.CENTER);
		add(root, BorderLayout.CENTER);
	}

	private boolean laQuanLy() {
		return nhanVienHienTai != null
				&& nhanVienHienTai.getVaiTro() != null
				&& "Quản lý".equalsIgnoreCase(nhanVienHienTai.getVaiTro().trim());
	}

	private JPanel createAccountCard() {
		JPanel card = createCustomCard();

		card.add(createCardHeader("Thông tin tài khoản"));
		card.add(Box.createVerticalStrut(20));

		String maNV = "...";
		String hoTen = "Chưa cập nhật";
		String sdt = "Chưa cập nhật";
		String vaiTro = "Chưa cập nhật";
		String tenDangNhap = "Chưa cập nhật";

		if (nhanVienHienTai != null) {
			if (nhanVienHienTai.getMaNV() != null) maNV = nhanVienHienTai.getMaNV();
			if (nhanVienHienTai.getHoTenNV() != null) hoTen = nhanVienHienTai.getHoTenNV();
			if (nhanVienHienTai.getSoDienThoai() != null) sdt = nhanVienHienTai.getSoDienThoai();
			if (nhanVienHienTai.getVaiTro() != null) vaiTro = nhanVienHienTai.getVaiTro();
			if (nhanVienHienTai.getTenDangNhap() != null) tenDangNhap = nhanVienHienTai.getTenDangNhap();
		}

		card.add(createTwoColRow(
				createLabelValue("Mã nhân viên", maNV),
				createLabelValue("Họ và tên", hoTen)
		));
		card.add(Box.createVerticalStrut(20));

		card.add(createTwoColRow(
				createLabelValue("Tên đăng nhập", tenDangNhap),
				createLabelValue("Vai trò", vaiTro)
		));
		card.add(Box.createVerticalStrut(20));

		card.add(createTwoColRow(
				createLabelValue("Số điện thoại", sdt),
				createEmptyBlock()
		));
		card.add(Box.createVerticalStrut(24));

		card.add(createDivider());
		card.add(Box.createVerticalStrut(16));

		JButton btnDoiMatKhau = new JButton("Đổi mật khẩu");
		btnDoiMatKhau.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnDoiMatKhau.setBackground(BTN_BG);
		btnDoiMatKhau.setForeground(Color.WHITE);
		btnDoiMatKhau.setFocusPainted(false);
		btnDoiMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDoiMatKhau.setBorder(new EmptyBorder(8, 16, 8, 16));
		btnDoiMatKhau.addActionListener(e -> hienThiDialogDoiMatKhau());

		JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		btnWrap.setOpaque(false);
		btnWrap.add(btnDoiMatKhau);

		card.add(btnWrap);
		return card;
	}
	private JPanel createRestaurantCard() {
		JPanel card = createCustomCard();
		card.add(createCardHeader("Thông tin nhà hàng"));
		card.add(Box.createVerticalStrut(20));

		// Hàng 1: Tên và SĐT
		card.add(createTwoColRow(
				createLabelValue("Tên nhà hàng", "Nhà hàng Ngói Đỏ"),
				createLabelValue("Số điện thoại", "0123 456 789")
		));
		card.add(Box.createVerticalStrut(20));

		// Hàng 2: Địa chỉ & Nút Maps (Đã fix lệch)
		JPanel row2 = new JPanel(new BorderLayout());
		row2.setOpaque(false);
		row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

		JPanel pnlAddress = new JPanel();
		pnlAddress.setLayout(new BoxLayout(pnlAddress, BoxLayout.Y_AXIS));
		pnlAddress.setOpaque(false);

		JLabel lblDiaChi = new JLabel("Địa chỉ");
		lblDiaChi.setFont(FONT_LABEL);
		lblDiaChi.setForeground(TEXT_GRAY);
		lblDiaChi.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel pnlValueAndBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnlValueAndBtn.setOpaque(false);
		pnlValueAndBtn.setAlignmentX(Component.LEFT_ALIGNMENT); 

		String diaChi = "123 Đường Trung Tâm, TP. Hồ Chí Minh";
		JLabel valDiaChi = new JLabel(diaChi);
		valDiaChi.setFont(FONT_VALUE);
		valDiaChi.setForeground(TEXT_DARK);
		valDiaChi.setBorder(new EmptyBorder(0, 0, 0, 15));

		JButton btnMap = new JButton("Xem bản đồ");
		btnMap.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnMap.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnMap.setFocusPainted(false);
		btnMap.addActionListener(e -> {
			try {
				String url = "https://www.google.com/maps/search/?api=1&query=" + java.net.URLEncoder.encode(diaChi, "UTF-8");
				java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
			} catch (Exception ex) { ex.printStackTrace(); }
		});

		pnlValueAndBtn.add(valDiaChi);
		pnlValueAndBtn.add(btnMap);
		pnlAddress.add(lblDiaChi);
		pnlAddress.add(Box.createVerticalStrut(6));
		pnlAddress.add(pnlValueAndBtn);

		row2.add(pnlAddress, BorderLayout.CENTER);
		card.add(row2);
		card.add(Box.createVerticalStrut(20));

		// Hàng 3: Email & Nút Web (Đã fix lệch)
		JPanel row3 = new JPanel(new GridLayout(1, 2, 20, 0));
		row3.setOpaque(false);
		row3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

		row3.add(createLabelValue("Email liên hệ", "ngoido@gmail.com"));

		JPanel pnlWebsite = new JPanel();
		pnlWebsite.setLayout(new BoxLayout(pnlWebsite, BoxLayout.Y_AXIS));
		pnlWebsite.setOpaque(false);

		JLabel lblWebTitle = new JLabel("Hệ thống Web");
		lblWebTitle.setFont(FONT_LABEL);
		lblWebTitle.setForeground(TEXT_GRAY);
		lblWebTitle.setAlignmentX(Component.LEFT_ALIGNMENT); // Quan trọng

		JPanel pnlWebBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnlWebBtn.setOpaque(false);
		pnlWebBtn.setAlignmentX(Component.LEFT_ALIGNMENT); // Quan trọng

		JButton btnGoWeb = new JButton("Tới trang chủ Nhà hàng");
		btnGoWeb.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnGoWeb.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnGoWeb.addActionListener(e -> {
			try {
				java.io.File htmlFile = new java.io.File("index.html");
				if (htmlFile.exists()) java.awt.Desktop.getDesktop().browse(htmlFile.toURI());
				else JOptionPane.showMessageDialog(this, "Không thấy file index.html!");
			} catch (Exception ex) { ex.printStackTrace(); }
		});

		pnlWebBtn.add(btnGoWeb);
		pnlWebsite.add(lblWebTitle);
		pnlWebsite.add(Box.createVerticalStrut(6));
		pnlWebsite.add(pnlWebBtn);

		row3.add(pnlWebsite);
		card.add(row3);

		return card;
	}

	private JPanel createSystemCard() {
		JPanel card = createCustomCard();

		card.add(createCardHeader("Hệ thống & Thiết bị"));
		card.add(Box.createVerticalStrut(20));

		card.add(createToggleRow("Tự động in hóa đơn", "In hóa đơn tự động sau khi thanh toán thành công", true));
		card.add(Box.createVerticalStrut(16));
		card.add(createDivider());
		card.add(Box.createVerticalStrut(16));

		card.add(createToggleRow("Âm thanh thông báo", "Phát âm thanh khi có yêu cầu thanh toán hoặc thông báo mới", true));
		card.add(Box.createVerticalStrut(16));
		card.add(createDivider());
		card.add(Box.createVerticalStrut(16));

		card.add(createToggleRow("Xác nhận trước khi xóa mềm", "Hiển thị hộp thoại xác nhận trước các thao tác xóa mềm", true));

		return card;
	}

	private JPanel createCustomCard() {
		JPanel card = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(BG_CARD);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
				g2.setColor(BORDER_CLR);
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
				g2.dispose();
				super.paintComponent(g);
			}
		};
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setOpaque(false);
		card.setBorder(new EmptyBorder(24, 24, 24, 24));
		return card;
	}

	private JPanel createCardHeader(String title) {
		JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnl.setOpaque(false);
		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(FONT_H2);
		lblTitle.setForeground(TEXT_TITLE);
		pnl.add(lblTitle);
		pnl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		return pnl;
	}

	private JPanel createLabelValue(String label, String value) {
		JPanel pnl = new JPanel();
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
		pnl.setOpaque(false);

		JLabel lbl = new JLabel(label);
		lbl.setFont(FONT_LABEL);
		lbl.setForeground(TEXT_GRAY);
		lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel val = new JLabel(value);
		val.setFont(FONT_VALUE);
		val.setForeground(TEXT_DARK);
		val.setAlignmentX(Component.LEFT_ALIGNMENT); // Ép lề trái
		pnl.add(lbl);
		pnl.add(Box.createVerticalStrut(6));
		pnl.add(val);
		return pnl;
	}

	private JPanel createTwoColRow(JPanel col1, JPanel col2) {
		JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
		row.setOpaque(false);
		row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		row.add(col1);
		row.add(col2);
		return row;
	}

	private JPanel createEmptyBlock() {
		JPanel pnl = new JPanel();
		pnl.setOpaque(false);
		return pnl;
	}

	private JPanel createDivider() {
		JPanel line = new JPanel();
		line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		line.setPreferredSize(new Dimension(0, 1));
		line.setBackground(BORDER_CLR);
		return line;
	}

	private JPanel createToggleRow(String title, String sub, boolean isSelected) {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);
		row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

		JPanel leftWrap = new JPanel();
		leftWrap.setLayout(new BoxLayout(leftWrap, BoxLayout.Y_AXIS));
		leftWrap.setOpaque(false);

		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblTitle.setForeground(TEXT_TITLE);

		JLabel lblSub = new JLabel(sub);
		lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblSub.setForeground(TEXT_GRAY);

		leftWrap.add(lblTitle);
		leftWrap.add(Box.createVerticalStrut(4));
		leftWrap.add(lblSub);

		JCheckBox chk = new JCheckBox();
		chk.setSelected(isSelected);
		chk.setOpaque(false);
		chk.setCursor(new Cursor(Cursor.HAND_CURSOR));
		chk.setFocusPainted(false);

		chk.addActionListener(e -> {
			boolean checked = chk.isSelected();
			System.out.println(title + ": " + checked);
		});

		row.add(leftWrap, BorderLayout.CENTER);
		row.add(chk, BorderLayout.EAST);

		return row;
	}

	private void hienThiDialogDoiMatKhau() {
		if (nhanVienHienTai == null) {
			JOptionPane.showMessageDialog(this, "Không lấy được thông tin tài khoản hiện tại!", "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		JPanel panel = new JPanel(new GridLayout(3, 2, 10, 15));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel lblOld = new JLabel("Mật khẩu cũ:");
		JPasswordField txtOld = new JPasswordField();

		JLabel lblNew = new JLabel("Mật khẩu mới:");
		JPasswordField txtNew = new JPasswordField();

		JLabel lblConfirm = new JLabel("Xác nhận mật khẩu mới:");
		JPasswordField txtConfirm = new JPasswordField();

		panel.add(lblOld);
		panel.add(txtOld);
		panel.add(lblNew);
		panel.add(txtNew);
		panel.add(lblConfirm);
		panel.add(txtConfirm);

		int result = JOptionPane.showConfirmDialog(this, panel, "Đổi mật khẩu",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			String oldPass = new String(txtOld.getPassword()).trim();
			String newPass = new String(txtNew.getPassword()).trim();
			String confirmPass = new String(txtConfirm.getPassword()).trim();

			if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!newPass.equals(confirmPass)) {
				JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (newPass.length() < 4) {
				JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 4 ký tự!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			boolean ok = nhanVienDAO.doiMatKhau(nhanVienHienTai.getMaNV(), oldPass, newPass);
			if (ok) {
				JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
			} else {
				JOptionPane.showMessageDialog(this, "Mật khẩu cũ không đúng hoặc cập nhật thất bại!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}