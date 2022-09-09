package com.myservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.StaffDAO;
import com.store.StaffInfo;

@WebServlet("/check")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        request.setCharacterEncoding("utf-8");

        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();

        String userName = request.getParameter("username");
        String psw = request.getParameter("password");

        StaffInfo staff = staffDAO.getByStringField("username", userName);

        // out.write(userName+psw);
        if (psw.equals(staff.getPassword())) {
            out.write("登录成功！");
            request.getSession().setAttribute("info", staff);
            Cookie title = new Cookie("title", staff.getTitle());
            response.setCharacterEncoding("utf-8");
            response.addCookie(title);

        } else
            out.write("登录失败！");
        // out.flush();
        out.close();

    }

}
