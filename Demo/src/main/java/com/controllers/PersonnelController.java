package com.controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.dao.StaffDAO;
import com.store.StaffInfo;
import com.myssm.myspringmvc.*;

// @WebServlet("/personnel.do")
public class PersonnelController extends ViewBaseServlet {
    StaffDAO fDao = new StaffDAO();
    // new RecordDAO("fruit");
    public int pageSize = 10;

    public boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    private String index(HttpServletRequest request)
            throws IOException {
        HttpSession session = request.getSession();

        // 设置当前页，默认值1
        Integer pageNo = 1;

        String oper = request.getParameter("oper");

        // 如果oper!=null 说明 通过表单的查询按钮点击过来的
        // 如果oper是空的，说明 不是通过表单的查询按钮点击过来的
        String keyword = null;

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

            String branch = request.getParameter("branch");

            session.setAttribute("branch", branch);

        } else {
            // 说明此处不是点击表单查询发送过来的请求（比如点击下面的上一页下一页或者直接在地址栏输入网址）
            // 此时keyword应该从session作用域获取
            String pageNoStr = request.getParameter("pageNo");
            if (!isEmpty(pageNoStr)) {
                pageNo = Integer.parseInt(pageNoStr); // 如果从请求中读取到pageNo，则类型转换。否则，pageNo默认就是1
            }
            // 如果不是点击的查询按钮，那么查询是基于session中保存的现有keyword进行查询
            Object keywordObj = session.getAttribute("keyword");
            if (keywordObj != null) {
                keyword = (String) keywordObj;
            } else {
                keyword = "";
            }

        }

        System.out.println(keyword);

        // 重新更新当前页的值

        session.setAttribute("pageNo", pageNo);
        List<StaffInfo> records;
        int fruitCount;
        records = fDao.getList(keyword, pageNo, pageSize);
        // 总记录条数
        fruitCount = fDao.getCount(keyword);

        session.setAttribute("staffList", records);

        // 总页数
        int pageCount = (fruitCount + pageSize - 1) / pageSize;

        session.setAttribute("pageCount", pageCount);

        System.out.println("pageCount=" + pageCount);

        return "personnel";
    }

    private String  add(HttpServletRequest request) throws  IOException {

        String name = request.getParameter("name");
        String title = request.getParameter("title");
        String branch = request.getParameter("branch");
        String gender = request.getParameter("gender");
        Integer age = Integer.parseInt(request.getParameter("age"));
        String phoneNumber = request.getParameter("phoneNumber");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Float basicSalary = Float.parseFloat(request.getParameter("basicSalary"));
        StaffInfo info = new StaffInfo(0, username, password, title, branch, name, gender, age, phoneNumber,
                basicSalary);
        fDao.add(info);
        return "redirect:personnel.do";

    }

    private String  edit(HttpServletRequest request) throws IOException {

        Integer id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String title = request.getParameter("title");
        String branch = request.getParameter("branch");
        String gender = request.getParameter("gender");
        Integer age = Integer.parseInt(request.getParameter("age"));
        String phoneNumber = request.getParameter("phoneNumber");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Float basicSalary = Float.parseFloat(request.getParameter("basicSalary"));
        StaffInfo info = new StaffInfo(id, username, password, title, branch, name, gender, age, phoneNumber,
                basicSalary);
        fDao.update(info);
        return "redirect:personnel.do";

    }

    private String del(HttpServletRequest request) throws IOException {
        fDao.del(Integer.parseInt((String) request.getParameter("id")));
        return "redirect:personnel.do";

    }

    private String select(HttpServletRequest request) throws  IOException {
        request.getSession().setAttribute("selected", fDao.getByid(Integer.parseInt(request.getParameter("id"))));
        return "editperson";
    }

}
