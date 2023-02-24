/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Color;
import java.util.regex.Pattern;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author ACER
 */
public class DataValidator {

    //Kiểm tra JTextField có bằng rỗng
    public static void validateEmpty(JTextField field, StringBuilder sb, String errorMessage) {
        if (field.getText().equals("")) {
            sb.append(errorMessage).append("\n");
            field.setBackground(Color.yellow);
            field.setForeground(Color.red);
            field.requestFocus();
        } else {
            field.setBackground(new Color(0, 102, 0));
        }
    }

    //Kiểm tra JTextField có bằng rỗng
    public static void validateEmpty(JTextArea jTextArea, StringBuilder sb, String errorMessage) {
        if (jTextArea.getText().equals("")) {
            sb.append(errorMessage).append("\n");
            jTextArea.setBackground(Color.yellow);
            jTextArea.setForeground(Color.red);
            jTextArea.requestFocus();
        } else {
            jTextArea.setBackground(new Color(0, 102, 0));
        }
    }

    //Kiểm tra JPasswordField có bằng rỗng
    public static void validateEmpty(JPasswordField field, StringBuilder sb, String errorMessage) {
        String password = new String(field.getPassword());
        if (password.equals("")) {
            sb.append(errorMessage).append("\n");
            field.setBackground(Color.yellow);
            field.setForeground(Color.red);
            field.requestFocus();
        } else {
            field.setBackground(new Color(0, 102, 0));
        }
    }

    //Kiểm tra định dạng mã
    public static void validateCode(JTextField field, StringBuilder sb, String errorMessage) {
        String codePattern = "^(PD)\\d";
        Pattern pattern = Pattern.compile(codePattern);
        if (pattern.matcher(field.getText()).find()) {
            field.setBackground(new Color(0, 102, 0));
        } else {
            sb.append(errorMessage).append("\n");
            field.setBackground(Color.yellow);
            field.setForeground(Color.red);
            field.requestFocus();
        }
    }

    //Kiểm tra định dạng email
    public static void validateEmail(JTextField field, StringBuilder sb, String errorMessage) {
        String emailPattern = "^[_A-Za-z0-9-\\\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        if (pattern.matcher(field.getText()).find()) {
            field.setBackground(new Color(0, 102, 0));
        } else {
            sb.append(errorMessage).append("\n");
            field.setBackground(Color.yellow);
            field.setForeground(Color.red);
            field.requestFocus();
        }
    }

    //Check điểm
    public static void validateCheck(JTextField field, StringBuilder sb, String errorMessage) {
        if (Integer.parseInt(field.getText()) < 0 || Integer.parseInt(field.getText()) > 10) {
            sb.append(errorMessage).append("\n");
            field.setBackground(Color.yellow);
            field.setForeground(Color.red);
            field.requestFocus();
        } else {
            field.setBackground(new Color(0, 102, 0));
        }
    }
}
