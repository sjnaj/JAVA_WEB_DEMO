package com.services;

import com.store.BaseRecord;
import java.util.List;

//基本操作的组合
public interface RecordService {
  List<BaseRecord> getRecordList(String keyword, Integer pageNo,Integer pageSize);
  List<BaseRecord> getRecordList(String timeFilter,String keyword, Integer pageNo,Integer pageSize);

  void addRecord(BaseRecord record);
  BaseRecord getRecordById(Integer id);
  void delRecord(Integer id);
  Integer getPageCount(String keyword,Integer  pageSize);  
  Integer getpageCount(String timeFilter,String keyword,Integer pageSize);
  String getTable();
  void setTable(String table);
}
