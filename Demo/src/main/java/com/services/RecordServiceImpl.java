package com.services;

import com.dao.RecordDAO;
import com.store.BaseRecord;
import java.util.List;

public class RecordServiceImpl implements RecordService {
  private RecordDAO recordDAO=new RecordDAO();

  public void setTable(String table) {
    recordDAO.setTable(table);
  }

  @Override
  public List<BaseRecord> getRecordList(
    String keyword,
    Integer pageNo,
    Integer pageSize
  ) {
    return recordDAO.getList(keyword, pageNo, pageSize);
  }

  @Override
  public List<BaseRecord> getRecordList(
    String timeFilter,
    String keyword,
    Integer pageNo,
    Integer pageSize
  ) {
    return recordDAO.getList(timeFilter, keyword, pageNo, pageSize);
  }

  @Override
  public void addRecord(BaseRecord record) {
    recordDAO.add(record);
  }

  @Override
  public BaseRecord getRecordById(Integer id) {
    return recordDAO.getByid(id);
  }

  @Override
  public void delRecord(Integer id) {
    recordDAO.del(id);
  }

  @Override
  public Integer getPageCount(String keyword, Integer pageSize) {
    return (recordDAO.getCount(keyword) + pageSize - 1) / pageSize;
  }

  @Override
  public Integer getpageCount(
    String timeFilter,
    String keyword,
    Integer pageSize
  ) {
    return (recordDAO.getCount(timeFilter, keyword) + pageSize - 1) / pageSize;
  }

  @Override
  public String getTable() {
    return recordDAO.getTable();
  }
}
