package com.controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.dao.InventoryDAO;
import com.store.*;

// @WebServlet("/inventory.do")
public class InventoryController {

    InventoryDAO fDao = new InventoryDAO();

    public int pageSize = 10;

    public boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    private String add(HttpServletRequest request) throws IOException {

        String name = request.getParameter("name");
        Float purchasePrice = Float.parseFloat(request.getParameter("purchasePrice"));
        Float salePrice = Float.parseFloat(request.getParameter("salePrice"));
        Float stock = Float.parseFloat(request.getParameter("stock"));
        String remark = request.getParameter("remark");

        BaseInventory inventory = new BaseInventory(0, name, purchasePrice, salePrice, stock, remark);

        fDao.add(inventory);
        return "redirect:inventory.do";
    }

    private String index(HttpServletRequest request)
            throws IOException {
        HttpSession session = request.getSession();

        String branch = ((StaffInfo) session.getAttribute("info")).getBranch();

        if (fDao.Table == null) {// 默认界面
            if (!branch.equals("admin")) {
                fDao.Table = branch + "inventory";

            } else {
                fDao.Table = "fruitinventory";
            }
        }

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
            System.out.println("branch=" + branch);
            session.setAttribute("branch", branch);

            if (branch.equals("admin")) {
                branch = (String) request.getParameter("branch");
                fDao.Table = branch + "inventory";// admin级别可以切换数据库
            }
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

        // 重新更新当前页的值
        session.setAttribute("pageNo", pageNo);
        List<BaseInventory> inventorys;
        int inventoryCount;

        inventorys = fDao.getList(keyword, pageNo, pageSize);
        // 总记录条数
        inventoryCount = fDao.getCount(keyword);

        session.setAttribute("inventoryList", inventorys);

        // 总页数
        int pageCount = (inventoryCount + pageSize - 1) / pageSize;

        session.setAttribute("pageCount", pageCount);

        System.out.println("pageCount=" + pageCount);
        // super.processTemplate("inventory", request, response);
        return "inventory";
    }

    private String edit(HttpServletRequest request) throws IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        Float purchasePrice = Float.parseFloat(request.getParameter("purchasePrice"));
        Float salePrice = Float.parseFloat(request.getParameter("salePrice"));
        Float stock = Float.parseFloat(request.getParameter("stock"));
        String remark = request.getParameter("remark");
        BaseInventory inventory = new BaseInventory(id, name, purchasePrice, salePrice, stock, remark);

        fDao.update(inventory);
        return "redirect:inventory.do";// 更新页面
    }

    private String del(HttpServletRequest request) throws IOException {
        fDao.del(Integer.parseInt((String) request.getParameter("id")));
        return "redirect:inventory.do";// 更新页面

    }

    private String select(HttpServletRequest request) throws IOException {
        request.getSession().setAttribute("selected", fDao.getByid(Integer.parseInt(request.getParameter("id"))));
        // super.processTemplate("editinventory", request, response);
        return "editinventory";//
    }
}
