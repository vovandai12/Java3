/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dao.StudentDao;

/**
 *
 * @author ACER
 */
public class Grade {
    private int TiengAnh, TinHoc, GDTC;
    private String MaSV, TenSV;
    private float DTB;

    
    public float getDTB() {
        DTB = (getTiengAnh() + getTinHoc() + getGDTC())/3;
        return DTB;
    }
    
    public String getTenSV() {
        try {
            StudentDao dao = new StudentDao();
            Student student = dao.selectById(MaSV);
            if(student != null){
                TenSV = student.getHoTen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TenSV;
    }

    public Grade(int TiengAnh, int TinHoc, int GDTC, String MaSV, String TenSV, float DTB) {
        this.TiengAnh = TiengAnh;
        this.TinHoc = TinHoc;
        this.GDTC = GDTC;
        this.MaSV = MaSV;
        this.TenSV = TenSV;
        this.DTB = DTB;
    }

    public Grade() {
    }

    public int getTiengAnh() {
        return TiengAnh;
    }

    public void setTiengAnh(int TiengAnh) {
        this.TiengAnh = TiengAnh;
    }

    public int getTinHoc() {
        return TinHoc;
    }

    public void setTinHoc(int TinHoc) {
        this.TinHoc = TinHoc;
    }

    public int getGDTC() {
        return GDTC;
    }

    public void setGDTC(int GDTC) {
        this.GDTC = GDTC;
    }

    public String getMaSV() {
        return MaSV;
    }

    public void setMaSV(String MaSV) {
        this.MaSV = MaSV;
    }
    
    
}
