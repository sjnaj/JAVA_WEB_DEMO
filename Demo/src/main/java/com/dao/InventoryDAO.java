package com.dao;

import java.util.List;

import com.action.BaseAction;
import com.store.BaseInventory;

public class InventoryDAO extends BaseDAO<BaseInventory> implements BaseAction<BaseInventory> {

    public String Table;// 数据表命名规定：kind+“inventorys”

    public InventoryDAO() {
    }

    @Override
    public List<BaseInventory> getList() {
        return super.executeQuery("select * from " + Table);
    }

    public List<BaseInventory> getList(String keyword, Integer pageNo, Integer pageSize) {

        return super.executeQuery(
                "select * from " + Table + " where name like ?  limit ? , "
                        + pageSize,
                "%" + keyword + "%", (pageNo - 1) * pageSize);
    }

    public List<BaseInventory> getList(String timeFilter, String keyword, Integer pageNo, Integer pageSize) {

        return super.executeQuery(
                "select * from " + Table + " where " + timeFilter + " and name like ?  limit ? , "
                        + pageSize,
                "%" + keyword + "%", (pageNo - 1) * pageSize);
    }

    @Override
    public int getCount(String keyword) {
        System.out.println(keyword);
        return ((Long) super.executeComplexQuery(
                "select count(*) from " + Table + " where name like ? ", "%" + keyword + "%")[0]).intValue();
    }

    public int getCount(String timeFilter, String keyword) {
        return ((Long) super.executeComplexQuery(
                "select count(*) from " + Table + " where " + timeFilter + " and  name like ? ",
                "%" + keyword + "%")[0]).intValue();
    }

    // public Float getFieldSum() {

    // return super.executeComplexQuery("select sum(*) from " + Table + " where name
    // like ? ","%" + + "%")
    // }

    @Override
    public BaseInventory getByid(Integer id) {
        return super.load("select * from " + Table + " where id = ? ", id);
    }

    @Override
    public BaseInventory getByFloatField(String field, Float f) {
        return super.load("select * from " + Table + " where " + field + " = ? ", f);

    }

    @Override
    public BaseInventory getByIntField(String field, Integer i) {
        return super.load("select * from " + Table + " where " + field + " = ? ", i);
    }

    @Override
    public BaseInventory getByStringField(String field, String str) {
        return super.load("select * from " + Table + " where " + field + " = ? ", str);

    }

    @Override
    public void update(BaseInventory inventory) {
        String sql = "update " + Table
                + " set name = ? , purchasePrice = ? , salePrice = ? , stock = ? , remark = ? where id = ? ";
        super.executeUpdate(sql, inventory.getName(), inventory.getPurchasePrice(), inventory.getSalePrice(),
                inventory.getStock(), inventory.getRemark(), inventory.getId());
    }

    @Override
    public void add(BaseInventory inventory) {
        String sql = "insert into " + Table + " values(0,?,?,?,?,?)";
        super.executeUpdate(sql, inventory.getName(), inventory.getPurchasePrice(), inventory.getSalePrice(),
                inventory.getStock(), inventory.getRemark());

    }

    @Override
    public void del(Integer id) {
        super.executeUpdate("delete from " + Table + " where id = ? ", id);

    }
}
