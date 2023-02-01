package com.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.dao.StaffDAO;
import com.store.StaffInfo;
import com.myssm.myspringmvc.*;

public class PersonnelController extends ViewBaseServlet {
    StaffDAO fDao = new StaffDAO();
    public int pageSize = 10;

    public boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    private String index(Integer pageNo, String oper, String keyword, String branch, HttpSession session) {

        // 如果oper!=null 说明 通过表单的查询按钮点击过来的
        // 如果oper是空的，说明 不是通过表单的查询按钮点击过来的
        // String keyword = null;

        if (!isEmpty(oper) && "search".equals(oper)) {
            pageNo = 1;
            if (isEmpty(keyword)) {
                keyword = "";
            }
            // 将keyword保存（覆盖）到session中
            session.setAttribute("keyword", keyword);
            session.setAttribute("branch", branch);

        } else {
            // 说明此处不是点击表单查询发送过来的请求（比如点击下面的上一页下一页或者直接在地址栏输入网址）
            // 此时keyword应该从session作用域获取
           
            pageNo=Optional.ofNullable(pageNo).orElse(1);
            // 如果不是点击的查询按钮，那么查询是基于session中保存的现有keyword进行查询
            keyword = Optional.ofNullable((String) session.getAttribute("keyword")).orElse("");

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

    private String add(String name, String title, String branch, String gender, Integer age, String phoneNumber,
            String username, String password, Float basicSalary) {

        StaffInfo info = new StaffInfo(0, username, password, title, branch, name, gender, age, phoneNumber,
                basicSalary);
        fDao.add(info);
        return "redirect:personnel.do";

    }

    private String edit(Integer id, String name, String title, String branch, String gender, Integer age,
            String phoneNumber, String username, String password, Float basicSalary) {

        StaffInfo info = new StaffInfo(id, username, password, title, branch, name, gender, age, phoneNumber,
                basicSalary);
        fDao.update(info);
        return "redirect:personnel.do";

    }

    private String del(Integer id) {
        fDao.del(id);
        return "redirect:personnel.do";

    }

    private String select(Integer id, HttpServletRequest request) throws IOException {
        request.getSession().setAttribute("selected", fDao.getByid(id));
        return "editperson";
    }

}
