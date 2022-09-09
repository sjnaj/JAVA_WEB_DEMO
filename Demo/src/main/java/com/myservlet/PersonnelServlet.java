package com.myservlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.StaffDAO;
import com.store.StaffInfo;

@WebServlet("/personnel.do")
public class PersonnelServlet extends ViewBaseServlet {
    StaffDAO fDao = new StaffDAO();
    // new RecordDAO("fruit");
    public int pageSize = 10;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // 设置编码
        request.setCharacterEncoding("UTF-8");

        String operate = request.getParameter("operate");
        if (isEmpty(operate)) {
            operate = "index";
        }
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (operate.equals(method.getName())) {
                try {
                    method.invoke(this, request, response);// *增删改查
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    private void index(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
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

        super.processTemplate("personnel", request, response);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

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
        response.sendRedirect("personnel.do");// 更新页面

    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Integer id =Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String title = request.getParameter("title");
        String branch = request.getParameter("branch");
        String gender = request.getParameter("gender");
        Integer age = Integer.parseInt(request.getParameter("age"));
        String phoneNumber = request.getParameter("phoneNumber");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Float basicSalary = Float.parseFloat(request.getParameter("basicSalary"));
        StaffInfo info = new StaffInfo(id,username, password, title, branch, name, gender, age, phoneNumber,basicSalary);
        fDao.update(info);
        response.sendRedirect("personnel.do");// 更新页面

    }

    private void del(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        fDao.del(Integer.parseInt((String) request.getParameter("id")));
        response.sendRedirect("personnel.do");// 更新页面

    }

    private void select(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.getSession().setAttribute("selected", fDao.getByid(Integer.parseInt(request.getParameter("id"))));
        super.processTemplate("editperson", request, response);
    }

}
