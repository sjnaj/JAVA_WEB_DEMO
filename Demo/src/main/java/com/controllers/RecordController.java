package com.controllers;

import com.dao.RecordDAO;
import com.services.RecordService;
import com.services.RecordServiceImpl;
import com.store.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;

// @WebServlet("/record.do")
public class RecordController {
  //   RecordDAO fDao = new RecordDAO();

  RecordService recordService = new RecordServiceImpl();

  public int pageSize = 10;

  public boolean isEmpty(String str) {
    return str == null || "".equals(str);
  }

  private String add(
    String name,
    Float price,
    Float count,
    String seller,
    String remark
  ) {
    BaseRecord record = new BaseRecord(
      name,
      price,
      count,
      LocalDateTime.now(),
      seller,
      remark
    );

    // fDao.add(record);
    recordService.addRecord(record);
    return "redirect:record.do";
  }

  private String index(
    Integer pageNo,
    String oper,
    String keyword,
    String branch,
    String timeFilter,
    String datetype,
    String date,
    HttpSession session
  ) {
    String branchInfo = ((StaffInfo) session.getAttribute("info")).getBranch();

    System.out.println("branch: " + branchInfo);

    if (recordService.getTable().equals("empty")) { // 默认界面
      if (!branchInfo.equals("admin")) {
        recordService.setTable(branchInfo);
      } else {
        // fDao.Table = "fruitrecords";
        recordService.setTable("fruit");
      }
    }

    if (!isEmpty(oper) && "search".equals(oper)) {
      pageNo = 1;
      if (isEmpty(keyword)) {
        keyword = "";
      }
      session.setAttribute("keyword", keyword);
      if (branchInfo.equals("admin")) {
        branchInfo = branch;
        // fDao.Table = branchInfo + "records"; // admin级别可以切换数据库
        recordService.setTable(branchInfo);
      }

      LocalDate dateObj = LocalDate.parse(date);

      if (datetype.equals("month")) {
        timeFilter =
          "year(time)='" +
          dateObj.getYear() +
          "' and month(time)='" +
          dateObj.getMonthValue() +
          "'";
      } else if (datetype.equals("day")) {
        timeFilter =
          "year(time)='" +
          dateObj.getYear() +
          "' and month(time)='" +
          dateObj.getMonthValue() +
          "' and day(time)='" +
          dateObj.getDayOfMonth() +
          "'";
      } else if (datetype.equals("year")) {
        timeFilter = "year(time)='" + dateObj.getYear() + "'";
      }
      System.out.println("datetype" + datetype);
      System.out.println("timeFilter::" + timeFilter);
      // 保证页面刷新后预设值仍保留
      session.setAttribute("timeFilter", timeFilter);
      session.setAttribute("datetype", datetype);
      session.setAttribute("date", date);
      session.setAttribute("branch", branchInfo);
    } else {
      pageNo = Optional.ofNullable(pageNo).orElse(1);
      timeFilter = (String) session.getAttribute("timeFilter");
      keyword =
        Optional
          .ofNullable((String) session.getAttribute("keyword"))
          .orElse("");
    }

    // 重新更新当前页的值
    session.setAttribute("pageNo", pageNo);
    List<BaseRecord> records;
    StaffInfo info = (StaffInfo) session.getAttribute("info");
    int pageCount;
    if (info.getTitle().equals("employee")) { // 普通员工查询
      if (isEmpty(timeFilter)) timeFilter =
        "seller = '" + info.getName() + "'"; else timeFilter +=
        " and seller = '" + info.getName() + "'";

      System.out.println("timeFilter" + timeFilter);
      System.out.println("branch" + recordService.getTable());

      records =
        recordService.getRecordList(timeFilter, keyword, pageNo, pageSize);
      pageCount = recordService.getpageCount(timeFilter, keyword, pageNo);
    } else {
      if (timeFilter != null) {
        records =
          recordService.getRecordList(timeFilter, keyword, pageNo, pageSize);
        pageCount = recordService.getpageCount(timeFilter, keyword, pageNo);
      } else {
        records = recordService.getRecordList(keyword, pageNo, pageSize);
        pageCount = recordService.getPageCount(keyword, pageNo);
      }
    }

    session.setAttribute("RecordList", records);

    // 总页数

    session.setAttribute("pageCount", pageCount);

    System.out.println("pageCount=" + pageCount);
    return "record";
  }
}
