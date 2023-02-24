/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;

/**
 *
 * @author ACER
 */
abstract class EntityDAO<EnityType, KeyType> {

    //Thêm
    public abstract int insert(EnityType entity);

    //Cập nhập
    public abstract int update(EnityType entity);

    //Xoá
    public abstract int delete(KeyType primarykey);

    //Danh sách entity
    public abstract List<EnityType> selectAll();

    //Danh sách Top3 entity
    public abstract List<EnityType> selectTop3();

    //Tìm kiếm theo id
    public abstract EnityType selectById(KeyType primarykey);

    //Lấy entity theo vị trí
    public abstract EnityType getEnityByPossition(KeyType index);

}
