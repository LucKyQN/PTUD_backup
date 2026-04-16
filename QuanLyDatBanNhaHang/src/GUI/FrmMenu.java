package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.*;
import connectDatabase.ConnectDB;

public class FrmMenu extends JFrame {

	private JPanel panelMenu;
	private JPanel panelCategory;
	private String currentCategory = "Tất cả";

	private final Color BG_MAIN = new Color(248, 248, 248);
	private final Color CARD_BG = Color.WHITE;
	private final Color BORDER_CLR = new Color(230, 230, 230);
	private final Color RED_MAIN = new Color(220, 38, 38);
	private final Color TEXT_DARK = new Color(30, 30, 30);
	private final Color TEXT_GRAY = new Color(110, 110, 110);
	private final Color GREEN_PRICE = new Color(22, 163, 74);

	public FrmMenu() {
		setTitle("Thực đơn nhà hàng");
		setSize(1200, 760);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		initUI();
		loadDataFromDatabase();
	}

	private void initUI() {
		JPanel root = new JPanel(new BorderLayout());
		root.setBackground(BG_MAIN);
		root.setBorder(new EmptyBorder(20, 20, 20, 20));

		root.add(createHeader(), BorderLayout.NORTH);
		root.add(createContent(), BorderLayout.CENTER);

		setContentPane(root);
	}

	private JPanel createHeader() {
		JPanel headerWrap = new JPanel();
		headerWrap.setLayout(new BoxLayout(headerWrap, BoxLayout.Y_AXIS));
		headerWrap.setBackground(BG_MAIN);
		headerWrap.setBorder(new EmptyBorder(0, 0, 16, 0));

		JLabel lblTitle = new JLabel("THỰC ĐƠN NHÀ HÀNG");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
		lblTitle.setForeground(TEXT_DARK);
		lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblSub = new JLabel("Khám phá các món ăn hấp dẫn của nhà hàng");
		lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblSub.setForeground(TEXT_GRAY);
		lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

		headerWrap.add(lblTitle);
		headerWrap.add(Box.createVerticalStrut(6));
		headerWrap.add(lblSub);
		headerWrap.add(Box.createVerticalStrut(16));

		panelCategory = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		panelCategory.setBackground(BG_MAIN);
		panelCategory.setAlignmentX(Component.LEFT_ALIGNMENT);

		String[] categories = { "Tất cả", "Món khai vị", "Món chính", "Đồ uống", "Tráng miệng" };

		for (String cat : categories) {
			panelCategory.add(createCategoryButton(cat));
		}

		headerWrap.add(panelCategory);

		return headerWrap;
	}

	private JButton createCategoryButton(String text) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btn.setFocusPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setBorder(new EmptyBorder(10, 18, 10, 18));

		updateCategoryButtonStyle(btn, text.equals(currentCategory));

		btn.addActionListener(e -> {
			currentCategory = text;
			refreshCategoryButtons();
			loadDataFromDatabase();
		});

		return btn;
	}

	private void refreshCategoryButtons() {
		for (Component c : panelCategory.getComponents()) {
			if (c instanceof JButton btn) {
				updateCategoryButtonStyle(btn, btn.getText().equals(currentCategory));
			}
		}
	}

	private void updateCategoryButtonStyle(JButton btn, boolean active) {
		if (active) {
			btn.setBackground(RED_MAIN);
			btn.setForeground(Color.WHITE);
			btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(RED_MAIN),
					new EmptyBorder(10, 18, 10, 18)));
		} else {
			btn.setBackground(Color.WHITE);
			btn.setForeground(TEXT_DARK);
			btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_CLR),
					new EmptyBorder(10, 18, 10, 18)));
		}
	}

	private JScrollPane createContent() {
		panelMenu = new JPanel(new GridLayout(0, 4, 18, 18));
		panelMenu.setBackground(BG_MAIN);

		JScrollPane scrollPane = new JScrollPane(panelMenu);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(BG_MAIN);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		return scrollPane;
	}

	private void loadDataFromDatabase() {

		String sql = "SELECT m.tenMonAn, m.giaBan, m.donVi, m.moTa, m.anhMon, dm.tenDM " + "FROM MonAn m "
				+ "JOIN DanhMucMonAn dm ON m.maDM = dm.maDM " + "WHERE m.tinhTrang = 1 ";

		boolean locTheoLoai = !currentCategory.equalsIgnoreCase("Tất cả");
		if (locTheoLoai) {
			sql += "AND dm.tenDM = ? ";
		}

		sql += "ORDER BY dm.tenDM, m.tenMonAn";

		try {
			ConnectDB.getInstance().connect();
			Connection conn = ConnectDB.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);

			if (locTheoLoai) {
				ps.setString(1, currentCategory);
			}

			ResultSet rs = ps.executeQuery();

			panelMenu.removeAll();

			while (rs.next()) {
				String ten = rs.getString("tenMonAn");
				String gia = String.format("%,.0f đ", rs.getDouble("giaBan"));
				String donVi = rs.getString("donVi");
				String moTa = rs.getString("moTa");
				String anh = rs.getString("anhMon");
				String danhMuc = rs.getString("tenDM");

				panelMenu.add(createItemCard(ten, gia, donVi, moTa, anh, danhMuc));
			}

			rs.close();
			ps.close();

			panelMenu.revalidate();
			panelMenu.repaint();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu thực đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JPanel createItemCard(String ten, String gia, String donVi, String moTa, String anh, String danhMuc) {
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(CARD_BG);
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_CLR, 1, true),
				new EmptyBorder(10, 10, 10, 10)));
		card.setPreferredSize(new Dimension(220, 300));
		card.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JLabel lblImg = new JLabel();
		lblImg.setPreferredSize(new Dimension(200, 140));
		lblImg.setHorizontalAlignment(SwingConstants.CENTER);
		hienThiAnh(lblImg, anh);

		JPanel info = new JPanel();
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		info.setBackground(CARD_BG);
		info.setBorder(new EmptyBorder(10, 2, 2, 2));

		JLabel lblLoai = new JLabel(danhMuc != null ? danhMuc : "");
		lblLoai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblLoai.setForeground(RED_MAIN);
		lblLoai.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblTen = new JLabel("<html><div style='width:180px'>" + ten + "</div></html>");
		lblTen.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblTen.setForeground(TEXT_DARK);
		lblTen.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblGia = new JLabel(gia);
		lblGia.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblGia.setForeground(GREEN_PRICE);
		lblGia.setAlignmentX(Component.LEFT_ALIGNMENT);

		String moTaHienThi = (moTa == null || moTa.trim().isEmpty()) ? "Chưa có mô tả món ăn." : moTa.trim();

		if (moTaHienThi.length() > 70) {
			moTaHienThi = moTaHienThi.substring(0, 70) + "...";
		}

		JLabel lblMoTa = new JLabel("<html><div style='width:180px; color:#6b7280;'>" + moTaHienThi + "</div></html>");
		lblMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblMoTa.setAlignmentX(Component.LEFT_ALIGNMENT);

		info.add(lblLoai);
		info.add(Box.createVerticalStrut(6));
		info.add(lblTen);
		info.add(Box.createVerticalStrut(8));
		info.add(lblGia);
		info.add(Box.createVerticalStrut(8));
		info.add(lblMoTa);

		card.add(lblImg, BorderLayout.NORTH);
		card.add(info, BorderLayout.CENTER);

		MouseAdapter clickDetail = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showDetail(ten, gia, donVi, moTa, anh, danhMuc);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(RED_MAIN, 1, true),
						new EmptyBorder(10, 10, 10, 10)));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_CLR, 1, true),
						new EmptyBorder(10, 10, 10, 10)));
			}
		};

		card.addMouseListener(clickDetail);
		lblImg.addMouseListener(clickDetail);
		info.addMouseListener(clickDetail);
		lblTen.addMouseListener(clickDetail);
		lblGia.addMouseListener(clickDetail);
		lblMoTa.addMouseListener(clickDetail);

		return card;
	}

	private void hienThiAnh(JLabel label, String path) {
		label.setIcon(null);

		if (path == null || path.trim().isEmpty()) {
			label.setText("No Image");
			label.setForeground(TEXT_GRAY);
			return;
		}

		try {
			ImageIcon icon;

			File file = new File(path);
			if (file.exists()) {
				icon = new ImageIcon(path);
			} else {
				java.net.URL url = getClass().getResource("/images/monan/" + path);
				if (url == null) {
					label.setText("Not found");
					label.setForeground(TEXT_GRAY);
					return;
				}
				icon = new ImageIcon(url);
			}

			Image img = icon.getImage().getScaledInstance(200, 140, Image.SCALE_SMOOTH);
			label.setText("");
			label.setIcon(new ImageIcon(img));

		} catch (Exception e) {
			label.setText("Error");
			label.setForeground(TEXT_GRAY);
		}
	}

	private void showDetail(String ten, String gia, String donVi, String moTa, String anh, String danhMuc) {
		JPanel panel = new JPanel(new BorderLayout(12, 12));
		panel.setPreferredSize(new Dimension(420, 420));
		panel.setBackground(Color.WHITE);

		JLabel lblImg = new JLabel();
		lblImg.setHorizontalAlignment(SwingConstants.CENTER);
		lblImg.setPreferredSize(new Dimension(380, 180));
		hienThiAnh(lblImg, anh);

		String html = "<html><div style='width:350px;'>" + "<h2 style='margin-bottom:8px;'>" + ten + "</h2>"
				+ "<b>Danh mục:</b> " + (danhMuc == null ? "..." : danhMuc) + "<br>" + "<b>Giá:</b> " + gia + "<br>"
				+ "<b>Đơn vị:</b> " + (donVi == null ? "..." : donVi) + "<br><br>" + "<b>Mô tả:</b><br>"
				+ (moTa == null || moTa.trim().isEmpty() ? "Chưa có mô tả món ăn." : moTa) + "</div></html>";

		JLabel lblInfo = new JLabel(html);
		lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		panel.add(lblImg, BorderLayout.NORTH);
		panel.add(lblInfo, BorderLayout.CENTER);

		JOptionPane.showMessageDialog(this, panel, "Chi tiết món ăn", JOptionPane.PLAIN_MESSAGE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new FrmMenu().setVisible(true));
	}
}