package com.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dao.RecordDAO;
import com.store.*;

// @WebServlet("/record.do")
public class RecordController {

    RecordDAO fDao = new RecordDAO();
    // new RecordDAO("fruit");

    public int pageSize = 10;

    public boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    private String add(HttpServletRequest request) throws IOException {

        String name = request.getParameter("name");
        Float price = Float.parseFloat(request.getParameter("price"));
        Float count = Float.parseFloat(request.getParameter("count"));
        String seller = request.getParameter("seller");
        String remark = request.getParameter("remark");

        BaseRecord record = new BaseRecord(name, price, count, LocalDateTime.now(), seller, remark);

        fDao.add(record);
        return "redirect:record.do";
    }

    private String index(HttpServletRequest request)
            throws IOException {

        HttpSession session = request.getSession();
        String branch = ((StaffInfo) session.getAttribute("info")).getBranch();

        System.out.println("branch: " + branch);

        if (fDao.Table == null) {// 默认界面
            if (!branch.equals("admin")) {
                fDao.Table = branch + "records";

            } else {
                fDao.Table = "fruitrecords";
            }
        }
        // 设置当前页，默认值1
        Integer pageNo = 1;

        String oper = request.getParameter("oper");

        // 如果oper!=null 说明 通过表单的查询按钮点击过来的
        // 如果oper是空的，说明 不是通过表单的查询按钮点击过来的
        String keyword = null;
        String timeFilter = null;

        if (!isEmpty(oper) && "search".equals(oper)) {
            // 说明是点击表单查询发送过来的请求
            // 此时，pageNo应该还原为1 ， keyword应该从请求参数中获取
            pageNo = 1;
            keyword = request.getParameter("keyword");
            if (isEmpty(keyword)) {
                keyword = "";
            }
            // 将keyword保存（覆盖）到session中
            session.setAttribute("keyword", keyword);

            // 时间筛选语句构造
            String datetype = request.getParameter("datetype");
            String dateStr = request.getParameter("date");
            if (branch.equals("admin")) {
                branch = (String) request.getParameter("branch");
                fDao.Table = branch + "records";// admin级别可以切换数据库
            }
            System.out.print(fDao.Table);

            LocalDate date = LocalDate.parse(dateStr);

            if (datetype.equals("month")) {
                timeFilter = "year(time)='" + date.getYear() + "' and month(time)='" + date.getMonthValue() + "'";
            } else if (datetype.equals("day")) {
                timeFilter = "year(time)='" + date.getYear() + "' and month(time)='" + date.getMonthValue()
                        + "' and day(time)='" + date.getDayOfMonth() + "'";

            } else if (datetype.equals("year")) {
                timeFilter = "year(time)='" + date.getYear() + "'";
            }
            System.out.println("datetype" + datetype);
            System.out.println("timeFilter::" + timeFilter);
            // 保证页面刷新后预设值仍保留
            session.setAttribute("timeFilter", timeFilter);
            session.setAttribute("datetype", datetype);
            session.setAttribute("date", dateStr);
            session.setAttribute("branch", branch);

        } else {
            // 说明此处不是点击表单查询发送过来的请求（比如点击下面的上一页下一页或者直接在地址栏输入网址）
            // 此时keyword应该从session作用域获取
            String pageNoStr = request.getParameter("pageNo");
            if (!isEmpty(pageNoStr)) {
                pageNo = Integer.parseInt(pageNoStr); // 如果从请求中读取到pageNo，则类型转换。否则，pageNo默认就是1
            }
            timeFilter = (String) session.getAttribute("timeFilter");

            // 如果不是点击的查询按钮，那么查询是基于session中保存的现有keyword进行查询
            Object keywordObj = session.getAttribute("keyword");
            if (keywordObj != null) {
                keyword = (String) keywordObj;
            } else {
                keyword = "";
            }

        }

        // 重新更新当前页的值
        session.setAttribute("pageNo", pageNo);
        List<BaseRecord> records;
        int recordCount;
        StaffInfo info = (StaffInfo) session.getAttribute("info");

        if (info.getTitle().equals("employee")) {// 普通员工查询
            if (isEmpty(timeFilter))
                timeFilter = "seller = '" + info.getName() + "'";
            else
                timeFilter += " and seller = '" + info.getName() + "'";

            System.out.println("timeFilter" + timeFilter);
            System.out.println("branch" + fDao.Table);

            records = fDao.getList(timeFilter, keyword, pageNo, pageSize);
            // 总记录条数
            recordCount = fDao.getCount(timeFilter, keyword);
        } else {
            if (timeFilter != null) {
                records = fDao.getList(timeFilter, keyword, pageNo, pageSize);
                // 总记录条数
                recordCount = fDao.getCount(timeFilter, keyword);
            } else {
                records = fDao.getList(keyword, pageNo, pageSize);
                // 总记录条数
                recordCount = fDao.getCount(keyword);
            }
        }

        session.setAttribute("RecordList", records);

        // 总页数
        int pageCount = (recordCount + pageSize - 1) / pageSize;

        session.setAttribute("pageCount", pageCount);

        System.out.println("pageCount=" + pageCount);
        // 此处的视图名称是 record
        // 那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图 名称上去
        // 逻辑视图名称 ： index
        // 物理视图名称 ： view-prefix + 逻辑视图名称 + view-suffix
        // 所以真实的视图名称是： / record.html
        return "record";
    }
}
