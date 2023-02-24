/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.DatabaseHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.User;

/**
 *
 * @author ACER
 */
public class UserDao {

    public User checkLogin(String tenDangNhap, String matKhau) throws Exception {
        String sql = "SELECT [TenDangNhap],[MatKhau],[VaiTro] FROM [dbo].[USERS] WHERE TenDangNhap=? AND MatKhau=?";
        try (
                 Connection con = DatabaseHelper.openConnection();  PreparedStatement pstmt = con.prepareStatement(sql);) {
            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhau);
            try (
                     ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    User user = new User();
                    user.setTenDangNhap(tenDangNhap);
                    user.setVaiTro(rs.getInt("VaiTro"));
                    return user;
                }
            }
        }
        return null;
    }
}
