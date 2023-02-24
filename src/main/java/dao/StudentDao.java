/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.DatabaseHelper;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.serial.SerialBlob;
import model.Student;

/**
 *
 * @author ACER
 */
public class StudentDao extends EntityDAO<Student, String> {

    //Tạo biến final là hằng
    private final String INSERT = "INSERT INTO [dbo].[STUDENTS] ([MaSV] ,[HoTen] ,[Email] ,[SoDT] ,[GioiTinh] ,[DiaChi] ,[Hinh]) VALUES (?,?,?,?,?,?,?)";
    private final String UPDATE = "UPDATE [dbo].[STUDENTS] SET [HoTen] = ?,[Email] = ?,[SoDT] = ?,[GioiTinh] = ?,[DiaChi] = ? ,[Hinh] = ? WHERE [MaSV] = ?";
    private final String DELETE = "DELETE FROM [dbo].[STUDENTS] WHERE [MaSV] = ?";
    private final String SELECT_ALL = "SELECT * FROM [dbo].[STUDENTS]";
    private final String SELECT_BY_PK = "SELECT * FROM [dbo].[STUDENTS] WHERE [MaSV] = ?";

    private static List<Student> list = new ArrayList<>();
    Connection conn = null;
    PreparedStatement pstmt = null;

    @Override
    public int insert(Student entity) {
        try {
            conn = DatabaseHelper.openConnection();
            pstmt = conn.prepareStatement(INSERT);
            pstmt.setString(1, entity.getMaSV());
            pstmt.setString(2, entity.getHoTen());
            pstmt.setString(3, entity.getEmail());
            pstmt.setString(4, entity.getSoDT());
            pstmt.setInt(5, entity.getGioiTinh());
            pstmt.setString(6, entity.getDiaChi());
            //Lưu trữ dưới dạng nhị phân
            if (entity.getHinh() != null) {
                Blob hinh = new SerialBlob(entity.getHinh());
                pstmt.setBlob(7, hinh);
            } else {
                Blob hinh = null;
                pstmt.setBlob(7, hinh);
            }
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("-> Insert (StudentDao) - " + e);
        }
        return 1;
    }

    @Override
    public int update(Student entity) {
        try {
            conn = DatabaseHelper.openConnection();
            pstmt = conn.prepareStatement(UPDATE);
            pstmt.setString(7, entity.getMaSV());
            pstmt.setString(1, entity.getHoTen());
            pstmt.setString(2, entity.getEmail());
            pstmt.setString(3, entity.getSoDT());
            pstmt.setInt(4, entity.getGioiTinh());
            pstmt.setString(5, entity.getDiaChi());
            //Lưu trữ dưới dạng nhị phân
            if (entity.getHinh() != null) {
                Blob hinh = new SerialBlob(entity.getHinh());
                pstmt.setBlob(6, hinh);
            } else {
                Blob hinh = null;
                pstmt.setBlob(6, hinh);
            }
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("-> Update (StudentDao) - " + e);
        }
        return 1;
    }

    @Override
    public int delete(String primarykey) {
        try {
            conn = DatabaseHelper.openConnection();
            pstmt = conn.prepareStatement(DELETE);
            pstmt.setString(1, primarykey);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("-> Delete (StudentDao) - " + e);
        }
        return 1;
    }

    @Override
    public List<Student> selectAll() {
        list.clear();
        try {
            conn = DatabaseHelper.openConnection();
            pstmt = conn.prepareStatement(SELECT_ALL);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Student entity = new Student();
                entity.setMaSV(rs.getString("MaSV"));
                entity.setHoTen(rs.getString("HoTen"));
                entity.setEmail(rs.getString("Email"));
                entity.setSoDT(rs.getString("SoDT"));
                entity.setGioiTinh(rs.getInt("GioiTinh"));
                entity.setDiaChi(rs.getString("DiaChi"));
                Blob blob = rs.getBlob("Hinh");
                if (blob != null) {
                    entity.setHinh(blob.getBytes(1, (int) blob.length()));
                }
                list.add(entity);
            }
        } catch (Exception e) {
            System.out.println("-> Select_All (StudentDao) - " + e);
        }
        return list;
    }

    @Override
    public List<Student> selectTop3() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Student selectById(String primarykey) {
        try {
            conn = DatabaseHelper.openConnection();
            pstmt = conn.prepareStatement(SELECT_BY_PK);
            pstmt.setString(1, primarykey);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Student entity = new Student();
                entity.setMaSV(rs.getString("MaSV"));
                entity.setHoTen(rs.getString("HoTen"));
                entity.setEmail(rs.getString("Email"));
                entity.setSoDT(rs.getString("SoDT"));
                entity.setGioiTinh(rs.getInt("GioiTinh"));
                entity.setDiaChi(rs.getString("DiaChi"));
                Blob blob = rs.getBlob("Hinh");
                if (blob != null) {
                    entity.setHinh(blob.getBytes(1, (int) blob.length()));
                }
                return entity;
            }
        } catch (Exception e) {
            System.out.println("-> Select_By_PK (StudentDao) - " + e);
        }
        return null;
    }

    @Override
    public Student getEnityByPossition(String index) {
        int id = Integer.parseInt(index);
        if (id >= 0 && id < list.size()) {
            return list.get(id);
        }
        return null;
    }

//    public static void main(String[] args) {
//        List<Student> list = new ArrayList<>();
//        StudentDao dao = new StudentDao();
//        list = dao.selectAll();
//        for (Student grade : list) {
//            System.out.println(grade.getHoTen());
//        }
//    }
}
