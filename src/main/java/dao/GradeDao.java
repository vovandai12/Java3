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
import java.util.ArrayList;
import java.util.List;
import model.Grade;

/**
 *
 * @author ACER
 */
public class GradeDao extends EntityDAO<Grade, String> {

    //Tạo biến final là hằng
    private final String INSERT = "INSERT INTO [dbo].[GRADE] ([MaSV] ,[TiengAnh] ,[TinHoc] , [GDTC]) VALUES (?,?,?,?)";
    private final String UPDATE = "UPDATE [dbo].[GRADE] SET [TiengAnh] = ? ,[TinHoc] = ? ,[GDTC] = ? WHERE [MaSV] = ?";
    private final String DELETE = "DELETE FROM [dbo].[GRADE] WHERE [MaSV] = ?";
    private final String SELECT_ALL = "SELECT * FROM [dbo].[GRADE]";
    private final String SELECT_TOP3 = "SELECT TOP 3 * ,([TiengAnh]+[TinHoc]+[GDTC])/3 AS DTB FROM [dbo].[GRADE] ORDER BY DTB DESC";
    private final String SELECT_BY_PK = "SELECT * FROM [dbo].[GRADE] WHERE [MaSV] = ?";

    private static List<Grade> list = new ArrayList<>();
    Connection conn = null;
    PreparedStatement pstmt = null;

    @Override
    public int insert(Grade entity) {
        try {
            conn = DatabaseHelper.openConnection();
            pstmt = conn.prepareStatement(INSERT);
            pstmt.setString(1, entity.getMaSV());
            pstmt.setInt(2, entity.getTiengAnh());
            pstmt.setInt(3, entity.getTinHoc());
            pstmt.setInt(4, entity.getGDTC());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("-> Insert (GradeDao) - " + e);
            return -1;
        }
        return 1;
    }

    @Override
    public int update(Grade entity) {
        try {
            conn = DatabaseHelper.openConnection();
            pstmt = conn.prepareStatement(UPDATE);
            pstmt.setString(4, entity.getMaSV());
            pstmt.setInt(1, entity.getTiengAnh());
            pstmt.setInt(2, entity.getTinHoc());
            pstmt.setInt(3, entity.getGDTC());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("-> Update (GradeDao) - " + e);
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
            System.out.println("-> Delete (GradeDao) - " + e);
        }
        return 1;
    }

    @Override
    public List<Grade> selectAll() {
        list.clear();
        try {
            conn = DatabaseHelper.openConnection();
            pstmt = conn.prepareStatement(SELECT_ALL);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Grade entity = new Grade();
                entity.setMaSV(rs.getString("MaSV"));
                entity.setTiengAnh(Integer.parseInt(rs.getString("TiengAnh")));
                entity.setTinHoc(Integer.parseInt(rs.getString("TinHoc")));
                entity.setGDTC(Integer.parseInt(rs.getString("GDTC")));
                list.add(entity);
            }
        } catch (Exception e) {
            System.out.println("-> Select_All (GradeDao) - " + e);
        }
        return list;
    }

    @Override
    public Grade selectById(String primarykey) {
        try {
            conn = DatabaseHelper.openConnection();
            pstmt = conn.prepareStatement(SELECT_BY_PK);
            pstmt.setString(1, primarykey);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Grade entity = new Grade();
                entity.setMaSV(rs.getString("MaSV"));
                entity.setTiengAnh(Integer.parseInt(rs.getString("TiengAnh")));
                entity.setTinHoc(Integer.parseInt(rs.getString("TinHoc")));
                entity.setGDTC(Integer.parseInt(rs.getString("GDTC")));
                return entity;
            }
        } catch (Exception e) {
            System.out.println("-> Select_By_PK (GradeDao) - " + e);
        }
        return null;
    }

    @Override
    public Grade getEnityByPossition(String index) {
        int id = Integer.parseInt(index);
        if (id >= 0 && id < list.size()) {
            return list.get(id);
        }
        return null;
    }

    @Override
    public List<Grade> selectTop3() {
        list.clear();
        try {
            conn = DatabaseHelper.openConnection();
            pstmt = conn.prepareStatement(SELECT_TOP3);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Grade entity = new Grade();
                entity.setMaSV(rs.getString("MaSV"));
                entity.setTiengAnh(Integer.parseInt(rs.getString("TiengAnh")));
                entity.setTinHoc(Integer.parseInt(rs.getString("TinHoc")));
                entity.setGDTC(Integer.parseInt(rs.getString("GDTC")));
                list.add(entity);
            }
        } catch (Exception e) {
            System.out.println("-> Select_Top3 (GradeDao) - " + e);
        }
        return list;
    }
    
//    public static void main(String[] args) {
//        List<Grade> list = new ArrayList<>();
//        GradeDao dao = new GradeDao();
//        list = dao.selectAll();
//        for (Grade grade : list) {
//            System.out.println(grade.getMaSV());
//        }
//    }
}
