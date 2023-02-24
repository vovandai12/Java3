package views;

import dao.StudentDao;
import helper.DataValidator;
import helper.ImageHelper;
import helper.MessageDialogHelper;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Student;

/**
 *
 * @author ACER
 */
public class ManagerUser extends javax.swing.JPanel {

    private final StudentDao dao = new StudentDao();
    private byte[] personalImage;
    private final ImageIcon icon = new ImageIcon(getClass().
            getResource("/icon/1634374532_168_44-Mau-anh-the-dep-cho-CMND-CCCD-Visa-ho.jpeg"));

    /**
     * Creates new form ManagerUser
     */
    public ManagerUser() {
        initComponents();
        enventButton();
        fillTable();
        customJtable();
    }

    private void customJtable() {
        jTStudent.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int row, int column) {
                super.getTableCellRendererComponent(jtable, o, bln, bln1, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        });
    }

    //Xử lý sự kiến nút button
    private void enventButton() {
        JButton[] btns = {btnInfo, btnListUser, btnNew, btnSave, btnUpdate, btnDelete};
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

    //Phương thức lấy và xuất dữ liệu
    private void setModel(Student student) {
        txtMaSV.setText(student.getMaSV());
        txtHoVaTen.setText(student.getHoTen());
        txtEmail.setText(student.getEmail());
        txtSDT.setText(student.getSoDT());
        if (student.getGioiTinh() == 1) {
            rbtNam.setSelected(true);
        } else {
            rbtNu.setSelected(true);
        }
        txtDiaChi.setText(student.getDiaChi());
        if (student.getHinh() != null) {
            try {
                Image img = ImageHelper.createImageFromByteArray(student.getHinh(), "jpg");
                showHinh.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                e.printStackTrace();
            }
            personalImage = student.getHinh();
        } else {
            showHinh.setIcon(icon);
        }
    }

    private Student getModel() {
        Student student = new Student();
        student.setMaSV(txtMaSV.getText());
        student.setHoTen(txtHoVaTen.getText());
        student.setEmail(txtEmail.getText());
        student.setSoDT(txtSDT.getText());
        student.setGioiTinh(rbtNam.isSelected() == true ? 1 : 0);
        student.setDiaChi(txtDiaChi.getText());
        student.setHinh(personalImage);
        return student;
    }

    private void fillTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) jTStudent.getModel();
            model.setRowCount(0);
            for (Student student : dao.selectAll()) {
                model.addRow(new Object[]{
                    student.getMaSV(),
                    student.getHoTen(),
                    student.getEmail(),
                    student.getSoDT(),
                    student.getGioiTinh() == 1 ? "Nam" : "Nữ",
                    student.getDiaChi()
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
        txtMaSV.setText("");
        txtHoVaTen.setText("");
        txtEmail.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        showHinh.setIcon(icon);

        txtMaSV.setBackground(new Color(0, 102, 0));
        txtHoVaTen.setBackground(new Color(0, 102, 0));
        txtEmail.setBackground(new Color(0, 102, 0));
        txtSDT.setBackground(new Color(0, 102, 0));
        txtDiaChi.setBackground(new Color(0, 102, 0));
    }

    private void save() {
        StringBuilder sb = check();
        if (sb.length() > 0) {
            MessageDialogHelper.showErrorDialog(this, sb.toString(), "Lỗi nhập liệu");
            return;
        }
        try {
            Student student = getModel();
            if (dao.selectById(student.getMaSV()) != null) {
                MessageDialogHelper.showMessageDialog(this, "Mã sinh viên đã tồn tại !", "Thông báo");
                txtMaSV.setText("");
            } else if (dao.insert(student) > 0) {
                MessageDialogHelper.showMessageDialog(this, "Lưu dữ liệu sinh viên thành công !", "Thông báo");
                news();
                fillTable();
                jPInfo.setVisible(false);
                jPList.setVisible(true);
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
            if (MessageDialogHelper.showComfirmDialog(this, "Bạn có muốn xoá sinh viên không ?", "Cảnh báo") == JOptionPane.YES_OPTION) {
                Student student = dao.selectById(txtMaSV.getText());
                if (student == null) {
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
            Student student = getModel();
            if (dao.selectById(student.getMaSV()) == null) {
                MessageDialogHelper.showMessageDialog(this, "Mã sinh viên không tồn tại !", "Thông báo");
                txtMaSV.setText("");
            } else if (dao.update(student) > 0) {
                MessageDialogHelper.showMessageDialog(this, "Cập nhập dữ liệu sinh viên thành công !", "Thông báo");
                news();
                fillTable();
                jPInfo.setVisible(false);
                jPList.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Phương thức kiểm lỗi và thông báo
    private StringBuilder check() {
        StringBuilder sb = new StringBuilder();
        DataValidator.validateCode(txtMaSV, sb, "Mã sinh viên trống hoặc không đúng định dạng !");
        DataValidator.validateEmpty(txtHoVaTen, sb, "Họ và tên sinh viên không được để trống !");
        DataValidator.validateEmail(txtEmail, sb, "Email sinh viên trống hoặc không đúng định dạng !");
        DataValidator.validateEmpty(txtSDT, sb, "Số điện thoại sinh viên không được để trống !");
        DataValidator.validateEmpty(txtDiaChi, sb, "Địa chỉ sinh viên không được để trống !");
        if (personalImage == null) {
            sb.append("Vui lòng chọn hình ảnh !");
        }
        return sb;
    }

    //Mở hộp thư file
    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().toLowerCase().endsWith(".jpg");
                }
            }

            @Override
            public String getDescription() {
                return "Image File (*.jpg)";
            }
        });

        if (chooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        try {
            ImageIcon icon = new ImageIcon(file.getPath());
            Image img = ImageHelper.resize(icon.getImage(), 230, 321);

            ImageIcon resizedIcon = new ImageIcon(img);
            showHinh.setIcon(resizedIcon);

            personalImage = ImageHelper.toByteArray(img, "jpg");
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialogHelper.showErrorDialog(this, e.getMessage(), "Lỗi");
        }
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
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        txtMaSV = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        txtHoVaTen = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        txtEmail = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        txtSDT = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        rbtNam = new javax.swing.JRadioButton();
        rbtNu = new javax.swing.JRadioButton();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jSeparator6 = new javax.swing.JSeparator();
        txtDiaChi = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        showHinh = new javax.swing.JLabel();
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
        jTStudent = new javax.swing.JTable();

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
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setMaximumSize(new java.awt.Dimension(970, 570));
        jPanel11.setMinimumSize(new java.awt.Dimension(970, 570));
        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        jPanel13.setBackground(new java.awt.Color(0, 102, 0));
        jPanel13.setMaximumSize(new java.awt.Dimension(485, 570));
        jPanel13.setMinimumSize(new java.awt.Dimension(485, 570));
        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 80));

        jPanel15.setMaximumSize(new java.awt.Dimension(400, 60));
        jPanel15.setMinimumSize(new java.awt.Dimension(400, 60));
        jPanel15.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.LINE_AXIS));

        jPanel17.setBackground(new java.awt.Color(0, 102, 0));
        jPanel17.setMaximumSize(new java.awt.Dimension(60, 60));
        jPanel17.setMinimumSize(new java.awt.Dimension(60, 60));
        jPanel17.setPreferredSize(new java.awt.Dimension(60, 60));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(0, 102, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_data_matrix_code_30px.png"))); // NOI18N
        jPanel17.add(jLabel2, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel17);

        jPanel16.setBackground(new java.awt.Color(0, 102, 0));
        jPanel16.setMaximumSize(new java.awt.Dimension(340, 60));
        jPanel16.setMinimumSize(new java.awt.Dimension(340, 60));
        jPanel16.setPreferredSize(new java.awt.Dimension(340, 60));
        jPanel16.setLayout(new java.awt.BorderLayout());

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.add(jSeparator1, java.awt.BorderLayout.PAGE_END);

        txtMaSV.setBackground(new java.awt.Color(0, 102, 0));
        txtMaSV.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        txtMaSV.setForeground(new java.awt.Color(255, 255, 255));
        txtMaSV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaSV.setText("MÃ SINH VIÊN");
        txtMaSV.setBorder(null);
        txtMaSV.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel16.add(txtMaSV, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel16);

        jPanel13.add(jPanel15);

        jPanel18.setLayout(new javax.swing.BoxLayout(jPanel18, javax.swing.BoxLayout.LINE_AXIS));

        jPanel19.setBackground(new java.awt.Color(0, 102, 0));
        jPanel19.setMaximumSize(new java.awt.Dimension(60, 60));
        jPanel19.setMinimumSize(new java.awt.Dimension(60, 60));
        jPanel19.setPreferredSize(new java.awt.Dimension(60, 60));
        jPanel19.setLayout(new java.awt.BorderLayout());

        jLabel3.setBackground(new java.awt.Color(0, 102, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_name_30px.png"))); // NOI18N
        jPanel19.add(jLabel3, java.awt.BorderLayout.CENTER);

        jPanel18.add(jPanel19);

        jPanel20.setBackground(new java.awt.Color(0, 102, 0));
        jPanel20.setMaximumSize(new java.awt.Dimension(340, 60));
        jPanel20.setMinimumSize(new java.awt.Dimension(340, 60));
        jPanel20.setPreferredSize(new java.awt.Dimension(340, 60));
        jPanel20.setLayout(new java.awt.BorderLayout());

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.add(jSeparator2, java.awt.BorderLayout.PAGE_END);

        txtHoVaTen.setBackground(new java.awt.Color(0, 102, 0));
        txtHoVaTen.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        txtHoVaTen.setForeground(new java.awt.Color(255, 255, 255));
        txtHoVaTen.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHoVaTen.setText("HỌ VÀ TÊN");
        txtHoVaTen.setBorder(null);
        txtHoVaTen.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel20.add(txtHoVaTen, java.awt.BorderLayout.CENTER);

        jPanel18.add(jPanel20);

        jPanel13.add(jPanel18);

        jPanel21.setLayout(new javax.swing.BoxLayout(jPanel21, javax.swing.BoxLayout.LINE_AXIS));

        jPanel22.setBackground(new java.awt.Color(0, 102, 0));
        jPanel22.setMaximumSize(new java.awt.Dimension(60, 60));
        jPanel22.setMinimumSize(new java.awt.Dimension(60, 60));
        jPanel22.setPreferredSize(new java.awt.Dimension(60, 60));
        jPanel22.setLayout(new java.awt.BorderLayout());

        jLabel4.setBackground(new java.awt.Color(0, 102, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_email_sign_30px.png"))); // NOI18N
        jPanel22.add(jLabel4, java.awt.BorderLayout.CENTER);

        jPanel21.add(jPanel22);

        jPanel23.setBackground(new java.awt.Color(0, 102, 0));
        jPanel23.setMaximumSize(new java.awt.Dimension(340, 60));
        jPanel23.setMinimumSize(new java.awt.Dimension(340, 60));
        jPanel23.setPreferredSize(new java.awt.Dimension(340, 60));
        jPanel23.setLayout(new java.awt.BorderLayout());

        jSeparator3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.add(jSeparator3, java.awt.BorderLayout.PAGE_END);

        txtEmail.setBackground(new java.awt.Color(0, 102, 0));
        txtEmail.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(255, 255, 255));
        txtEmail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEmail.setText("EMAIL");
        txtEmail.setBorder(null);
        txtEmail.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel23.add(txtEmail, java.awt.BorderLayout.CENTER);

        jPanel21.add(jPanel23);

        jPanel13.add(jPanel21);

        jPanel11.add(jPanel13);

        jPanel14.setBackground(new java.awt.Color(0, 102, 0));
        jPanel14.setMaximumSize(new java.awt.Dimension(485, 570));
        jPanel14.setMinimumSize(new java.awt.Dimension(485, 570));
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 80));

        jPanel24.setLayout(new javax.swing.BoxLayout(jPanel24, javax.swing.BoxLayout.LINE_AXIS));

        jPanel25.setBackground(new java.awt.Color(0, 102, 0));
        jPanel25.setMaximumSize(new java.awt.Dimension(60, 60));
        jPanel25.setMinimumSize(new java.awt.Dimension(60, 60));
        jPanel25.setPreferredSize(new java.awt.Dimension(60, 60));
        jPanel25.setLayout(new java.awt.BorderLayout());

        jLabel5.setBackground(new java.awt.Color(0, 102, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_phone_30px.png"))); // NOI18N
        jPanel25.add(jLabel5, java.awt.BorderLayout.CENTER);

        jPanel24.add(jPanel25);

        jPanel26.setBackground(new java.awt.Color(0, 102, 0));
        jPanel26.setMaximumSize(new java.awt.Dimension(340, 60));
        jPanel26.setMinimumSize(new java.awt.Dimension(340, 60));
        jPanel26.setPreferredSize(new java.awt.Dimension(340, 60));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jSeparator4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.add(jSeparator4, java.awt.BorderLayout.PAGE_END);

        txtSDT.setBackground(new java.awt.Color(0, 102, 0));
        txtSDT.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        txtSDT.setForeground(new java.awt.Color(255, 255, 255));
        txtSDT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSDT.setText("SỐ ĐIỆN THOẠI");
        txtSDT.setBorder(null);
        txtSDT.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel26.add(txtSDT, java.awt.BorderLayout.CENTER);

        jPanel24.add(jPanel26);

        jPanel14.add(jPanel24);

        jPanel27.setLayout(new javax.swing.BoxLayout(jPanel27, javax.swing.BoxLayout.LINE_AXIS));

        jPanel28.setBackground(new java.awt.Color(0, 102, 0));
        jPanel28.setMaximumSize(new java.awt.Dimension(60, 60));
        jPanel28.setMinimumSize(new java.awt.Dimension(60, 60));
        jPanel28.setPreferredSize(new java.awt.Dimension(60, 60));
        jPanel28.setLayout(new java.awt.BorderLayout());

        jLabel6.setBackground(new java.awt.Color(0, 102, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_sexy_boy_30px.png"))); // NOI18N
        jPanel28.add(jLabel6, java.awt.BorderLayout.CENTER);

        jPanel27.add(jPanel28);

        jPanel29.setBackground(new java.awt.Color(0, 102, 0));
        jPanel29.setMaximumSize(new java.awt.Dimension(340, 60));
        jPanel29.setMinimumSize(new java.awt.Dimension(340, 60));
        jPanel29.setPreferredSize(new java.awt.Dimension(340, 60));
        jPanel29.setLayout(new javax.swing.BoxLayout(jPanel29, javax.swing.BoxLayout.LINE_AXIS));

        rbtNam.setBackground(new java.awt.Color(0, 102, 0));
        buttonGroup1.add(rbtNam);
        rbtNam.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        rbtNam.setForeground(new java.awt.Color(255, 255, 255));
        rbtNam.setSelected(true);
        rbtNam.setText("NAM");
        rbtNam.setBorder(null);
        rbtNam.setMaximumSize(new java.awt.Dimension(170, 60));
        rbtNam.setMinimumSize(new java.awt.Dimension(170, 60));
        rbtNam.setPreferredSize(new java.awt.Dimension(170, 60));
        jPanel29.add(rbtNam);

        rbtNu.setBackground(new java.awt.Color(0, 102, 0));
        buttonGroup1.add(rbtNu);
        rbtNu.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        rbtNu.setForeground(new java.awt.Color(255, 255, 255));
        rbtNu.setText("NỮ");
        rbtNu.setBorder(null);
        rbtNu.setMaximumSize(new java.awt.Dimension(170, 60));
        rbtNu.setMinimumSize(new java.awt.Dimension(170, 60));
        rbtNu.setPreferredSize(new java.awt.Dimension(170, 60));
        jPanel29.add(rbtNu);

        jPanel27.add(jPanel29);

        jPanel14.add(jPanel27);

        jPanel30.setLayout(new javax.swing.BoxLayout(jPanel30, javax.swing.BoxLayout.LINE_AXIS));

        jPanel31.setBackground(new java.awt.Color(0, 102, 0));
        jPanel31.setMaximumSize(new java.awt.Dimension(60, 60));
        jPanel31.setMinimumSize(new java.awt.Dimension(60, 60));
        jPanel31.setPreferredSize(new java.awt.Dimension(60, 60));
        jPanel31.setLayout(new java.awt.BorderLayout());

        jLabel7.setBackground(new java.awt.Color(0, 102, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_address_30px.png"))); // NOI18N
        jPanel31.add(jLabel7, java.awt.BorderLayout.CENTER);

        jPanel30.add(jPanel31);

        jPanel32.setBackground(new java.awt.Color(0, 102, 0));
        jPanel32.setMaximumSize(new java.awt.Dimension(340, 60));
        jPanel32.setMinimumSize(new java.awt.Dimension(340, 60));
        jPanel32.setPreferredSize(new java.awt.Dimension(340, 60));
        jPanel32.setLayout(new java.awt.BorderLayout());

        jSeparator6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.add(jSeparator6, java.awt.BorderLayout.PAGE_END);

        txtDiaChi.setBackground(new java.awt.Color(0, 102, 0));
        txtDiaChi.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        txtDiaChi.setForeground(new java.awt.Color(255, 255, 255));
        txtDiaChi.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDiaChi.setText("ĐỊA CHỈ");
        txtDiaChi.setBorder(null);
        txtDiaChi.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel32.add(txtDiaChi, java.awt.BorderLayout.CENTER);

        jPanel30.add(jPanel32);

        jPanel14.add(jPanel30);

        jPanel11.add(jPanel14);

        jPanel5.add(jPanel11);

        jPanel12.setBackground(new java.awt.Color(0, 102, 0));
        jPanel12.setMaximumSize(new java.awt.Dimension(250, 570));
        jPanel12.setMinimumSize(new java.awt.Dimension(250, 570));
        jPanel12.setPreferredSize(new java.awt.Dimension(250, 570));
        jPanel12.setLayout(new java.awt.BorderLayout());

        showHinh.setBackground(new java.awt.Color(0, 102, 0));
        showHinh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        showHinh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/1634374532_168_44-Mau-anh-the-dep-cho-CMND-CCCD-Visa-ho.jpeg"))); // NOI18N
        showHinh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        showHinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showHinhMouseClicked(evt);
            }
        });
        jPanel12.add(showHinh, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel12);

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

        jTStudent.setBackground(new java.awt.Color(0, 102, 0));
        jTStudent.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        jTStudent.setForeground(new java.awt.Color(255, 255, 255));
        jTStudent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã SV", "Họ tên", "Email", "Số điện thoại", "Địa chỉ", "Giới tính"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTStudent.setGridColor(new java.awt.Color(102, 102, 102));
        jTStudent.setRowHeight(80);
        jTStudent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTStudentMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTStudent);

        jPList.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPList, "card3");

        add(jPanel2);
    }// </editor-fold>//GEN-END:initComponents

    private void showHinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showHinhMouseClicked
        openFile();
    }//GEN-LAST:event_showHinhMouseClicked

    private void jTStudentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTStudentMouseClicked
        int row = jTStudent.getSelectedRow();
        if (row >= 0) {
            String maSV = (String) jTStudent.getValueAt(row, 0);
            Student student = dao.selectById(maSV);
            setModel(student);
        }
        jPInfo.setVisible(true);
        jPList.setVisible(false);
    }//GEN-LAST:event_jTStudentMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnInfo;
    private javax.swing.JButton btnListUser;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTable jTStudent;
    private javax.swing.JRadioButton rbtNam;
    private javax.swing.JRadioButton rbtNu;
    private javax.swing.JLabel showHinh;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoVaTen;
    private javax.swing.JTextField txtMaSV;
    private javax.swing.JTextField txtSDT;
    // End of variables declaration//GEN-END:variables
}
