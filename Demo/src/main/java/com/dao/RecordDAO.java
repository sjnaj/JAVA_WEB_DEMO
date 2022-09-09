package com.dao;

import java.util.List;

import com.action.BaseAction;
import com.store.BaseRecord;

public class RecordDAO extends BaseDAO<BaseRecord> implements BaseAction<BaseRecord> {

    public String Table;// 数据表命名规定：kind+“records”

    public RecordDAO() {
    }

    @Override
    public List<BaseRecord> getList() {
        return super.executeQuery("select * from " + Table);
    }

    public List<BaseRecord> getList(String keyword, Integer pageNo, Integer pageSize) {

        return super.executeQuery(
                "select * from " + Table + " where name like ? or seller like ?  limit ? , "
                        + pageSize,
                "%" + keyword + "%", "%" + keyword + "%", (pageNo - 1) * pageSize);

    }

    public List<BaseRecord> getList(String timeFilter, String keyword, Integer pageNo, Integer pageSize) {
       

        return super.executeQuery(
                "select * from " + Table + " where " + timeFilter + " and ( name like ? or seller like ? ) limit ? , "
                        + pageSize,
                "%" + keyword + "%", "%" + keyword + "%", (pageNo - 1) * pageSize);
    }

    @Override
    public int getCount(String keyword) {
        return ((Long) super.executeComplexQuery(
                "select count(*) from " + Table + " where name like ? or seller like ?", "%" + keyword + "%",
                "%" + keyword + "%")[0]).intValue();
    }

    public int getCount(String timeFilter, String keyword) {
        return ((Long) super.executeComplexQuery(
                "select count(*) from " + Table + " where " + timeFilter + " and  (name like ? or seller like ?)",
                "%" + keyword + "%",
                "%" + keyword + "%")[0]).intValue();
    }

    // public Float getFieldSum() {

    // return super.executeComplexQuery("select sum(*) from " + Table + " where name
    // like ? ","%" + + "%")
    // }

    @Override
    public BaseRecord getByid(Integer id) {
        return super.load("select * from " + Table + " where id = ? ", id);
    }

    @Override
    public BaseRecord getByFloatField(String field, Float f) {
        return super.load("select * from " + Table + " where " + field + " = ? ", f);

    }

    @Override
    public BaseRecord getByIntField(String field, Integer i) {
        return super.load("select * from " + Table + " where " + field + " = ? ", i);
    }

    @Override
    public BaseRecord getByStringField(String field, String str) {
        return super.load("select * from " + Table + " where " + field + " = ? ", str);

    }

    @Override
    public void update(BaseRecord record) {
        String sql = "update " + Table
                + " set name = ? , price = ? , count = ? , time = ? , seller = ? , total = ? ,  remark = ? where id = ? ";
        super.executeUpdate(sql, record.getname(), record.getPrice(), record.getcount(), record.getTime(),
                record.getRemark(), record.getSeller(), record.getTotal(), record.getid());

    }

    @Override
    public void add(BaseRecord record) {
        String sql = "insert into " + Table + " values(0,?,?,?,?,?,?,?)";
        super.executeUpdate(sql, record.getname(), record.getPrice(), record.getcount(), record.getTime(),
                record.getSeller(),
                record.getTotal(),
                record.getRemark());

    }

    @Override
    public void del(Integer id) {
        super.executeUpdate("delete from " + Table + " where id = ? ", id);

    }
}
