package views;

import dao.GradeDao;
import helper.DataValidator;
import helper.MessageDialogHelper;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Grade;

/**
 *
 * @author ACER
 */
public class ManagerGrade extends javax.swing.JPanel {

    private final GradeDao dao = new GradeDao();

    /**
     * Creates new form ManagerUser
     */
    public ManagerGrade() {
        initComponents();
        enventButton();
        customJtable();
        fillTable();
    }

    //Xử lý sự kiến nút button
    private void enventButton() {
        JButton[] btns = {btnInfo, btnListUser, btnNew, btnSave, btnUpdate, btnDelete, btnSreach};
        for (JButton btn : btns) {
            btn.setUI(new BasicButtonUI());
            btn.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    btn.setBackground(new Color(0, 102, 153));
                    String cmd = btn.getActionCommand();
                    switch (cmd) {
                        case "Info":
                            jPInfo.setVisible(true);
                            jPList.setVisible(false);
                            break;
                        case "ListUser":
                            jPInfo.setVisible(false);
                            jPList.setVisible(true);
                            break;
                        case "New":
                            news();
                            break;
                        case "Save":
                            save();
                            break;
                        case "Update":
                            update();
                            break;
                        case "Delete":
                            delete();
                            break;
                        case "Search":
                            search();
                            break;
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    btn.setBackground(new Color(102, 204, 5));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    btn.setBackground(new Color(102, 204, 5));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(102, 204, 255));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(new Color(0, 102, 0));
                }
            });
        }
    }

    private void customJtable() {
        jtbGrade.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int row, int column) {
                super.getTableCellRendererComponent(jtable, o, bln, bln1, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        });
    }

    //Phương thức lấy và xuất dữ liệu
    private void setModel(Grade grade) {
        lbHoVanTen.setText(grade.getTenSV());
        txtMaSV.setText(grade.getMaSV());
        txtTiengAnh.setText(String.valueOf(grade.getTiengAnh()));
        txtTinHoc.setText(String.valueOf(grade.getTinHoc()));
        txtGiaoDTC.setText(String.valueOf(grade.getGDTC()));
        lbDiemTB.setText(String.valueOf(grade.getDTB()));
    }

    private Grade getModel() {
        Grade grade = new Grade();
        grade.setMaSV(txtMaSV.getText());
        grade.setTiengAnh(Integer.parseInt(txtTiengAnh.getText()));
        grade.setGDTC(Integer.parseInt(txtGiaoDTC.getText()));
        grade.setTinHoc(Integer.parseInt(txtTinHoc.getText()));
        return grade;
    }

    private void fillTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) jtbGrade.getModel();
            model.setRowCount(0);
            for (Grade grade : dao.selectAll()) {
                model.addRow(new Object[]{
                    grade.getMaSV(),
                    grade.getTenSV(),
                    grade.getTiengAnh(),
                    grade.getTinHoc(),
                    grade.getGDTC(),
                    grade.getDTB()
                });
            }
            model.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialogHelper.showErrorDialog(this, e.getMessage(), "Lỗi");
        }
    }

    //Phương thức điều kiển
    private void news() {
        txtMaSVSearch.setText("");
        lbHoVanTen.setText("Nguyễn Văn An");
        txtMaSV.setText("");
        txtTiengAnh.setText("");
        txtTinHoc.setText("");
        txtGiaoDTC.setText("");
        lbDiemTB.setText("9.0");

        txtMaSVSearch.setBackground(new Color(0, 102, 0));
        txtMaSV.setBackground(new Color(0, 102, 0));
        txtTiengAnh.setBackground(new Color(0, 102, 0));
        txtTinHoc.setBackground(new Color(0, 102, 0));
        txtGiaoDTC.setBackground(new Color(0, 102, 0));
    }

    private void save() {
        StringBuilder sb = check();
        if (sb.length() > 0) {
            MessageDialogHelper.showErrorDialog(this, sb.toString(), "Lỗi nhập liệu");
            return;
        }
        try {
            Grade grade = getModel();
            if (dao.selectById(grade.getMaSV()) != null) {
                MessageDialogHelper.showMessageDialog(this, "Mã sinh viên đã có điểm !", "Thông báo");
                txtMaSV.setText("");
            } else if (dao.insert(grade) > 0) {
                MessageDialogHelper.showMessageDialog(this, "Lưu điểm sinh viên thành công !", "Thông báo");
                news();
                fillTable();
                jPInfo.setVisible(false);
                jPList.setVisible(true);
            } else {
                MessageDialogHelper.showErrorDialog(this, "Không có sinh viên trong dữ liệu !", "Thông báo");
                news();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        StringBuilder sb = new StringBuilder();
        DataValidator.validateCode(txtMaSV, sb, "Mã sinh viên trống hoặc không đúng định dạng !");
        if (sb.length() > 0) {
            MessageDialogHelper.showErrorDialog(this, sb.toString(), "Lỗi nhập liệu");
        } else {
            if (MessageDialogHelper.showComfirmDialog(this, "Bạn có muốn xoá bảng điểm sinh viên không ?", "Cảnh báo")
                    == JOptionPane.YES_OPTION) {
                Grade grade = dao.selectById(txtMaSV.getText());
                if (grade == null) {
                    MessageDialogHelper.showMessageDialog(this, "Không tìm thấy sinh viên !", "Thông báo");
                    news();
                } else if (dao.delete(txtMaSV.getText()) > 0) {
                    MessageDialogHelper.showMessageDialog(this, "Xoá dữ liệu sinh viên thành công !", "Thông báo");
                    news();
                    fillTable();
                    jPInfo.setVisible(false);
                    jPList.setVisible(true);
                }
            }
        }
    }

    private void update() {
        StringBuilder sb = check();
        if (sb.length() > 0) {
            MessageDialogHelper.showErrorDialog(this, sb.toString(), "Lỗi nhập liệu");
            return;
        }
        try {
            Grade grade = getModel();
            if (dao.update(grade) > 0) {
                MessageDialogHelper.showMessageDialog(this, "Cập nhập bảng điểm sinh viên thành công !", "Thông báo");
                news();
                fillTable();
                jPInfo.setVisible(false);
                jPList.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void search() {
        StringBuilder sb = new StringBuilder();
        DataValidator.validateCode(txtMaSVSearch, sb, "Mã sinh viên trống hoặc không đúng định dạng !");
        if (sb.length() > 0) {
            MessageDialogHelper.showErrorDialog(this, sb.toString(), "Lỗi nhập liệu");
            return;
        }
        try {
            Grade grade = dao.selectById(txtMaSVSearch.getText());
            if (grade != null) {
                MessageDialogHelper.showMessageDialog(this, "Đã tìm thấy sinh viên.", "Thông báo");
                setModel(grade);
            } else {
                MessageDialogHelper.showErrorDialog(this, "Không tìm thấy sinh viên.", "Thông báo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Phương thức kiểm lỗi và thông báo
    private StringBuilder check() {
        StringBuilder sb = new StringBuilder();
        DataValidator.validateCode(txtMaSV, sb, "Mã sinh viên trống hoặc không đúng định dạng !");
        DataValidator.validateEmpty(txtTiengAnh, sb, "Điểm tiếng anh không được để trống !");
        DataValidator.validateEmpty(txtTinHoc, sb, "Điểm tin học không được để trống !");
        DataValidator.validateEmpty(txtGiaoDTC, sb, "Điểm giáo dục thể chất không được để trống !");

        DataValidator.validateCheck(txtTiengAnh, sb, "Điểm tiếng anh chỉ nằm trong khoảng 0  - > 10 !");
        DataValidator.validateCheck(txtTinHoc, sb, "Điểm tin học chỉ nằm trong khoảng 0  - > 10 !");
        DataValidator.validateCheck(txtGiaoDTC, sb, "Điểm giáo dục thể chất chỉ nằm trong khoảng 0  - > 10 !");
        return sb;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnInfo = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnListUser = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPInfo = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        txtMaSVSearch = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel13 = new javax.swing.JPanel();
        btnSreach = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        lbHoVanTen = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        txtMaSV = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel20 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        txtTiengAnh = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel22 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        txtTinHoc = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel24 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        txtGiaoDTC = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel15 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lbDiemTB = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        btnUpdate = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        jPList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbGrade = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1220, 770));
        setMinimumSize(new java.awt.Dimension(1220, 770));
        setPreferredSize(new java.awt.Dimension(1220, 770));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setBackground(new java.awt.Color(0, 102, 0));
        jPanel1.setMaximumSize(new java.awt.Dimension(1220, 100));
        jPanel1.setMinimumSize(new java.awt.Dimension(1220, 100));
        jPanel1.setPreferredSize(new java.awt.Dimension(1220, 100));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 100, 10));

        jPanel3.setBackground(new java.awt.Color(0, 102, 0));
        jPanel3.setMaximumSize(new java.awt.Dimension(300, 80));
        jPanel3.setMinimumSize(new java.awt.Dimension(300, 80));
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 80));
        jPanel3.setLayout(new java.awt.BorderLayout());

        btnInfo.setBackground(new java.awt.Color(0, 102, 0));
        btnInfo.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        btnInfo.setForeground(new java.awt.Color(0, 0, 32));
        btnInfo.setText("THÔNG TIN");
        btnInfo.setActionCommand("Info");
        btnInfo.setBorder(null);
        btnInfo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel3.add(btnInfo, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3);

        jPanel4.setBackground(new java.awt.Color(0, 102, 0));
        jPanel4.setMaximumSize(new java.awt.Dimension(300, 80));
        jPanel4.setMinimumSize(new java.awt.Dimension(300, 80));
        jPanel4.setPreferredSize(new java.awt.Dimension(300, 80));
        jPanel4.setLayout(new java.awt.BorderLayout());

        btnListUser.setBackground(new java.awt.Color(0, 102, 0));
        btnListUser.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        btnListUser.setForeground(new java.awt.Color(0, 0, 32));
        btnListUser.setText("DANH SÁCH");
        btnListUser.setActionCommand("ListUser");
        btnListUser.setBorder(null);
        btnListUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.add(btnListUser, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4);

        add(jPanel1);

        jPanel2.setBackground(new java.awt.Color(0, 102, 0));
        jPanel2.setLayout(new java.awt.CardLayout());

        jPInfo.setBackground(new java.awt.Color(0, 102, 0));
        jPInfo.setLayout(new javax.swing.BoxLayout(jPInfo, javax.swing.BoxLayout.Y_AXIS));

        jPanel5.setBackground(new java.awt.Color(0, 102, 0));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.X_AXIS));

        jPanel33.setMaximumSize(new java.awt.Dimension(1220, 570));
        jPanel33.setMinimumSize(new java.awt.Dimension(1220, 570));
        jPanel33.setPreferredSize(new java.awt.Dimension(1220, 570));
        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jPanel34.setBackground(new java.awt.Color(0, 102, 0));
        jPanel34.setMaximumSize(new java.awt.Dimension(1220, 100));
        jPanel34.setMinimumSize(new java.awt.Dimension(1220, 100));
        jPanel34.setPreferredSize(new java.awt.Dimension(1220, 100));
        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        jPanel11.setBackground(new java.awt.Color(0, 102, 0));
        jPanel11.setMaximumSize(new java.awt.Dimension(80, 80));
        jPanel11.setMinimumSize(new java.awt.Dimension(80, 80));
        jPanel11.setPreferredSize(new java.awt.Dimension(80, 80));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(0, 102, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_data_matrix_code_50px.png"))); // NOI18N
        jPanel11.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel34.add(jPanel11);

        jPanel12.setBackground(new java.awt.Color(0, 102, 0));
        jPanel12.setMaximumSize(new java.awt.Dimension(300, 80));
        jPanel12.setMinimumSize(new java.awt.Dimension(300, 80));
        jPanel12.setPreferredSize(new java.awt.Dimension(300, 80));
        jPanel12.setLayout(new java.awt.BorderLayout());

        txtMaSVSearch.setBackground(new java.awt.Color(0, 102, 0));
        txtMaSVSearch.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        txtMaSVSearch.setForeground(new java.awt.Color(255, 255, 255));
        txtMaSVSearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaSVSearch.setText("MÃ SINH VIÊN");
        txtMaSVSearch.setBorder(null);
        txtMaSVSearch.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel12.add(txtMaSVSearch, java.awt.BorderLayout.CENTER);

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.add(jSeparator1, java.awt.BorderLayout.PAGE_END);

        jPanel34.add(jPanel12);

        jPanel13.setBackground(new java.awt.Color(0, 102, 0));
        jPanel13.setMaximumSize(new java.awt.Dimension(80, 80));
        jPanel13.setMinimumSize(new java.awt.Dimension(80, 80));
        jPanel13.setPreferredSize(new java.awt.Dimension(80, 80));
        jPanel13.setLayout(new java.awt.BorderLayout());

        btnSreach.setBackground(new java.awt.Color(0, 102, 0));
        btnSreach.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_search_50px.png"))); // NOI18N
        btnSreach.setActionCommand("Search");
        btnSreach.setBorder(null);
        btnSreach.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel13.add(btnSreach, java.awt.BorderLayout.CENTER);

        jPanel34.add(jPanel13);

        jPanel33.add(jPanel34);

        jPanel35.setBackground(new java.awt.Color(0, 102, 0));
        jPanel35.setMaximumSize(new java.awt.Dimension(1220, 470));
        jPanel35.setMinimumSize(new java.awt.Dimension(1220, 470));
        jPanel35.setPreferredSize(new java.awt.Dimension(1220, 470));
        jPanel35.setLayout(new javax.swing.BoxLayout(jPanel35, javax.swing.BoxLayout.LINE_AXIS));

        jPanel14.setBackground(new java.awt.Color(0, 102, 0));
        jPanel14.setMaximumSize(new java.awt.Dimension(820, 470));
        jPanel14.setMinimumSize(new java.awt.Dimension(820, 470));
        jPanel14.setPreferredSize(new java.awt.Dimension(820, 470));
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 30));

        jPanel16.setBackground(new java.awt.Color(0, 102, 0));
        jPanel16.setMaximumSize(new java.awt.Dimension(500, 50));
        jPanel16.setMinimumSize(new java.awt.Dimension(500, 50));
        jPanel16.setPreferredSize(new java.awt.Dimension(500, 50));
        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel4.setBackground(new java.awt.Color(0, 102, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_name_30px.png"))); // NOI18N
        jLabel4.setMaximumSize(new java.awt.Dimension(50, 50));
        jLabel4.setMinimumSize(new java.awt.Dimension(50, 50));
        jLabel4.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel16.add(jLabel4);

        jPanel17.setBackground(new java.awt.Color(0, 102, 0));
        jPanel17.setMaximumSize(new java.awt.Dimension(450, 50));
        jPanel17.setMinimumSize(new java.awt.Dimension(450, 50));
        jPanel17.setPreferredSize(new java.awt.Dimension(450, 50));
        jPanel17.setLayout(new java.awt.BorderLayout());

        lbHoVanTen.setFont(new java.awt.Font("DialogInput", 1, 36)); // NOI18N
        lbHoVanTen.setForeground(new java.awt.Color(0, 0, 0));
        lbHoVanTen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbHoVanTen.setText("NGUYỄN VĂN AN");
        jPanel17.add(lbHoVanTen, java.awt.BorderLayout.CENTER);

        jPanel16.add(jPanel17);

        jPanel14.add(jPanel16);

        jPanel18.setBackground(new java.awt.Color(0, 102, 0));
        jPanel18.setMaximumSize(new java.awt.Dimension(500, 50));
        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel5.setBackground(new java.awt.Color(0, 102, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_data_matrix_code_30px.png"))); // NOI18N
        jLabel5.setMaximumSize(new java.awt.Dimension(50, 50));
        jLabel5.setMinimumSize(new java.awt.Dimension(50, 50));
        jLabel5.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel18.add(jLabel5);

        jPanel19.setBackground(new java.awt.Color(0, 102, 0));
        jPanel19.setMaximumSize(new java.awt.Dimension(450, 50));
        jPanel19.setMinimumSize(new java.awt.Dimension(450, 50));
        jPanel19.setPreferredSize(new java.awt.Dimension(450, 50));
        jPanel19.setLayout(new java.awt.BorderLayout());

        txtMaSV.setBackground(new java.awt.Color(0, 102, 0));
        txtMaSV.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        txtMaSV.setForeground(new java.awt.Color(255, 255, 255));
        txtMaSV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaSV.setText("MÃ SINH VIÊN");
        txtMaSV.setBorder(null);
        txtMaSV.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel19.add(txtMaSV, java.awt.BorderLayout.CENTER);

        jSeparator3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.add(jSeparator3, java.awt.BorderLayout.PAGE_END);

        jPanel18.add(jPanel19);

        jPanel14.add(jPanel18);

        jPanel20.setBackground(new java.awt.Color(0, 102, 0));
        jPanel20.setMaximumSize(new java.awt.Dimension(500, 50));
        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel6.setBackground(new java.awt.Color(0, 102, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_english_to_arabic_30px.png"))); // NOI18N
        jLabel6.setMaximumSize(new java.awt.Dimension(50, 50));
        jLabel6.setMinimumSize(new java.awt.Dimension(50, 50));
        jLabel6.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel20.add(jLabel6);

        jPanel21.setBackground(new java.awt.Color(0, 102, 0));
        jPanel21.setMaximumSize(new java.awt.Dimension(450, 50));
        jPanel21.setMinimumSize(new java.awt.Dimension(450, 50));
        jPanel21.setPreferredSize(new java.awt.Dimension(450, 50));
        jPanel21.setLayout(new java.awt.BorderLayout());

        txtTiengAnh.setBackground(new java.awt.Color(0, 102, 0));
        txtTiengAnh.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        txtTiengAnh.setForeground(new java.awt.Color(255, 255, 255));
        txtTiengAnh.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTiengAnh.setText("TIẾNG ANH");
        txtTiengAnh.setBorder(null);
        txtTiengAnh.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel21.add(txtTiengAnh, java.awt.BorderLayout.CENTER);

        jSeparator4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.add(jSeparator4, java.awt.BorderLayout.PAGE_END);

        jPanel20.add(jPanel21);

        jPanel14.add(jPanel20);

        jPanel22.setBackground(new java.awt.Color(0, 102, 0));
        jPanel22.setMaximumSize(new java.awt.Dimension(500, 50));
        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel7.setBackground(new java.awt.Color(0, 102, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_cloud_storage_30px.png"))); // NOI18N
        jLabel7.setMaximumSize(new java.awt.Dimension(50, 50));
        jLabel7.setMinimumSize(new java.awt.Dimension(50, 50));
        jLabel7.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel22.add(jLabel7);

        jPanel23.setBackground(new java.awt.Color(0, 102, 0));
        jPanel23.setMaximumSize(new java.awt.Dimension(450, 50));
        jPanel23.setMinimumSize(new java.awt.Dimension(450, 50));
        jPanel23.setPreferredSize(new java.awt.Dimension(450, 50));
        jPanel23.setLayout(new java.awt.BorderLayout());

        txtTinHoc.setBackground(new java.awt.Color(0, 102, 0));
        txtTinHoc.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        txtTinHoc.setForeground(new java.awt.Color(255, 255, 255));
        txtTinHoc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTinHoc.setText("TIN HỌC");
        txtTinHoc.setBorder(null);
        txtTinHoc.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel23.add(txtTinHoc, java.awt.BorderLayout.CENTER);

        jSeparator5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.add(jSeparator5, java.awt.BorderLayout.PAGE_END);

        jPanel22.add(jPanel23);

        jPanel14.add(jPanel22);

        jPanel24.setBackground(new java.awt.Color(0, 102, 0));
        jPanel24.setMaximumSize(new java.awt.Dimension(500, 50));
        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel8.setBackground(new java.awt.Color(0, 102, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_deadlift_30px.png"))); // NOI18N
        jLabel8.setMaximumSize(new java.awt.Dimension(50, 50));
        jLabel8.setMinimumSize(new java.awt.Dimension(50, 50));
        jLabel8.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel24.add(jLabel8);

        jPanel25.setBackground(new java.awt.Color(0, 102, 0));
        jPanel25.setMaximumSize(new java.awt.Dimension(450, 50));
        jPanel25.setMinimumSize(new java.awt.Dimension(450, 50));
        jPanel25.setPreferredSize(new java.awt.Dimension(450, 50));
        jPanel25.setLayout(new java.awt.BorderLayout());

        txtGiaoDTC.setBackground(new java.awt.Color(0, 102, 0));
        txtGiaoDTC.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        txtGiaoDTC.setForeground(new java.awt.Color(255, 255, 255));
        txtGiaoDTC.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGiaoDTC.setText("GIÁO DỤC THỂ CHẤT");
        txtGiaoDTC.setBorder(null);
        txtGiaoDTC.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel25.add(txtGiaoDTC, java.awt.BorderLayout.CENTER);

        jSeparator6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.add(jSeparator6, java.awt.BorderLayout.PAGE_END);

        jPanel24.add(jPanel25);

        jPanel14.add(jPanel24);

        jPanel35.add(jPanel14);

        jPanel15.setBackground(new java.awt.Color(0, 102, 0));
        jPanel15.setMaximumSize(new java.awt.Dimension(400, 470));
        jPanel15.setMinimumSize(new java.awt.Dimension(400, 470));
        jPanel15.setPreferredSize(new java.awt.Dimension(400, 470));
        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.Y_AXIS));

        jLabel2.setBackground(new java.awt.Color(0, 102, 0));
        jLabel2.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ĐIỂM TRUNG BÌNH");
        jLabel2.setMaximumSize(new java.awt.Dimension(400, 170));
        jLabel2.setMinimumSize(new java.awt.Dimension(400, 170));
        jLabel2.setPreferredSize(new java.awt.Dimension(400, 170));
        jPanel15.add(jLabel2);

        lbDiemTB.setBackground(new java.awt.Color(0, 102, 0));
        lbDiemTB.setFont(new java.awt.Font("DialogInput", 1, 80)); // NOI18N
        lbDiemTB.setForeground(new java.awt.Color(230, 0, 0));
        lbDiemTB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDiemTB.setText("9.0");
        lbDiemTB.setMaximumSize(new java.awt.Dimension(400, 300));
        lbDiemTB.setMinimumSize(new java.awt.Dimension(400, 300));
        lbDiemTB.setPreferredSize(new java.awt.Dimension(400, 300));
        jPanel15.add(lbDiemTB);

        jPanel35.add(jPanel15);

        jPanel33.add(jPanel35);

        jPanel5.add(jPanel33);

        jPInfo.add(jPanel5);

        jPanel6.setBackground(new java.awt.Color(0, 102, 0));
        jPanel6.setMaximumSize(new java.awt.Dimension(1220, 100));
        jPanel6.setMinimumSize(new java.awt.Dimension(1220, 100));
        jPanel6.setPreferredSize(new java.awt.Dimension(1220, 100));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        jPanel7.setBackground(new java.awt.Color(0, 102, 0));
        jPanel7.setMaximumSize(new java.awt.Dimension(80, 80));
        jPanel7.setMinimumSize(new java.awt.Dimension(80, 80));
        jPanel7.setPreferredSize(new java.awt.Dimension(80, 80));
        jPanel7.setLayout(new java.awt.BorderLayout());

        btnNew.setBackground(new java.awt.Color(0, 102, 0));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_new_document_50px.png"))); // NOI18N
        btnNew.setActionCommand("New");
        btnNew.setBorder(null);
        btnNew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel7.add(btnNew, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel7);

        jPanel8.setBackground(new java.awt.Color(0, 102, 0));
        jPanel8.setMaximumSize(new java.awt.Dimension(80, 80));
        jPanel8.setMinimumSize(new java.awt.Dimension(80, 80));
        jPanel8.setPreferredSize(new java.awt.Dimension(80, 80));
        jPanel8.setLayout(new java.awt.BorderLayout());

        btnSave.setBackground(new java.awt.Color(0, 102, 0));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_save_50px.png"))); // NOI18N
        btnSave.setActionCommand("Save");
        btnSave.setBorder(null);
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel8.add(btnSave, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel8);

        jPanel9.setBackground(new java.awt.Color(0, 102, 0));
        jPanel9.setMaximumSize(new java.awt.Dimension(80, 80));
        jPanel9.setMinimumSize(new java.awt.Dimension(80, 80));
        jPanel9.setPreferredSize(new java.awt.Dimension(80, 80));
        jPanel9.setLayout(new java.awt.BorderLayout());

        btnUpdate.setBackground(new java.awt.Color(0, 102, 0));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_update_50px.png"))); // NOI18N
        btnUpdate.setActionCommand("Update");
        btnUpdate.setBorder(null);
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel9.add(btnUpdate, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel9);

        jPanel10.setBackground(new java.awt.Color(0, 102, 0));
        jPanel10.setMaximumSize(new java.awt.Dimension(80, 80));
        jPanel10.setMinimumSize(new java.awt.Dimension(80, 80));
        jPanel10.setPreferredSize(new java.awt.Dimension(80, 80));
        jPanel10.setLayout(new java.awt.BorderLayout());

        btnDelete.setBackground(new java.awt.Color(0, 102, 0));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_Delete_50px.png"))); // NOI18N
        btnDelete.setActionCommand("Delete");
        btnDelete.setBorder(null);
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel10.add(btnDelete, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel10);

        jPInfo.add(jPanel6);

        jPanel2.add(jPInfo, "card2");

        jPList.setBackground(new java.awt.Color(0, 102, 0));
        jPList.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);

        jtbGrade.setBackground(new java.awt.Color(0, 102, 0));
        jtbGrade.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        jtbGrade.setForeground(new java.awt.Color(255, 255, 255));
        jtbGrade.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã SV", "Họ tên", "Tiếng anh", "Tin học", "GDTC", "Trung bình"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtbGrade.setGridColor(new java.awt.Color(102, 102, 102));
        jtbGrade.setRowHeight(80);
        jtbGrade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbGradeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbGrade);

        jPList.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPList, "card3");

        add(jPanel2);
    }// </editor-fold>//GEN-END:initComponents

    private void jtbGradeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbGradeMouseClicked
        int row = jtbGrade.getSelectedRow();
        if (row >= 0) {
            String maSV = (String) jtbGrade.getValueAt(row, 0);
            Grade grade = dao.selectById(maSV);
            setModel(grade);
            jPInfo.setVisible(true);
            jPList.setVisible(false);
        }
    }//GEN-LAST:event_jtbGradeMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnInfo;
    private javax.swing.JButton btnListUser;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSreach;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPInfo;
    private javax.swing.JPanel jPList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTable jtbGrade;
    private javax.swing.JLabel lbDiemTB;
    private javax.swing.JLabel lbHoVanTen;
    private javax.swing.JTextField txtGiaoDTC;
    private javax.swing.JTextField txtMaSV;
    private javax.swing.JTextField txtMaSVSearch;
    private javax.swing.JTextField txtTiengAnh;
    private javax.swing.JTextField txtTinHoc;
    // End of variables declaration//GEN-END:variables
}
