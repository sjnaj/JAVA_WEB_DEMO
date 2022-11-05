package com.dao;

import java.util.List;
import com.myssm.basedao.*;

import com.action.IBaseAction;
import com.store.StaffInfo;

public class StaffDAO extends BaseDAO<StaffInfo> implements IBaseAction<StaffInfo> {

    private final String Table = "staff";

    @Override
    public List<StaffInfo> getList() {
        return super.executeQuery("select * from " + Table);
    }

    @Override
    public StaffInfo getByid(Integer id) {
        return super.load("select * from " + Table + " where id = ? ", id);
    }

    @Override
    public StaffInfo getByFloatField(String field, Float f) {
        return super.load("select * from " + Table + " where " + field + " = ? ", f);

    }

    @Override
    public StaffInfo getByIntField(String field, Integer i) {
        return super.load("select * from " + Table + " where " + field + " = ? ", i);
    }

    @Override
    public StaffInfo getByStringField(String field, String str) {
        return super.load("select * from " + Table + " where " + field + " = '" + str + "'");
    }

    @Override
    public void update(StaffInfo record) {

        String sql = "update " + Table
                + " set username = ? , password = ? , title = ? , branch = ? , name = ? , gender = ? , age = ? , phoneNumber = ? , basicSalary = ? where id = ? ";
        super.executeUpdate(sql, record.getUsername(), record.getPassword(), record.getTitle(), record.getBranch(),
                record.getName(),
                record.getGender(), record.getAge(), record.getPhoneNumber(), record.getBasicSalary(), record.getId());

    }

    @Override
    public void add(StaffInfo record) {
        String sql = "insert into " + Table + " values(0,?,?,?,?,?,?,?,?,?)";
        super.executeUpdate(sql, record.getUsername(), record.getPassword(), record.getTitle(), record.getBranch(),
                record.getName(),
                record.getGender(), record.getAge(), record.getPhoneNumber(), record.getBasicSalary());

    }

    @Override
    public void del(Integer id) {
        super.executeUpdate("delete from " + Table + " where id = ? ", id);

    }

    @Override
    public int getCount(String keyword) {
        return ((Long) super.executeComplexQuery(
                "select count(*) from " + Table + " where name like ? or branch like ?", "%" + keyword + "%",
                "%" + keyword + "%")[0]).intValue();
    }

    public List<StaffInfo> getList(String keyword, Integer pageNo, Integer pageSize) {
        return super.executeQuery(
                "select * from " + Table + " where name like ? or branch like ? limit ? , " + pageSize,
                "%" + keyword + "%", "%" + keyword + "%", (pageNo - 1) * pageSize);

    }
}
