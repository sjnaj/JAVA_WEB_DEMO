package com.controllers;

import java.util.List;
import java.util.Optional;

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

    private String add(String name, Float purchasePrice, Float salePrice, Float stock, String remark) {

        // String name = request.getParameter("name");
        // Float purchasePrice =
        // Float.parseFloat(request.getParameter("purchasePrice"));
        // Float salePrice = Float.parseFloat(request.getParameter("salePrice"));
        // Float stock = Float.parseFloat(request.getParameter("stock"));
        // String remark = request.getParameter("remark");

        BaseInventory inventory = new BaseInventory(0, name, purchasePrice, salePrice, stock, remark);

        fDao.add(inventory);
        return "redirect:inventory.do";
    }

    private String index(Integer pageNo, String oper, String keyword, String branch, HttpSession session) {

        String branchInfo = ((StaffInfo) session.getAttribute("info")).getBranch();

        if (fDao.Table == null) // 默认界面
            if (!branchInfo.equals("admin"))
                fDao.Table = branchInfo + "inventory";

            else
                fDao.Table = "fruitinventory";

        // 设置当前页，默认值1
        // Integer pageNo = 1;

        // String oper = request.getParameter("oper");

        // 如果oper!=null 说明 通过表单的查询按钮点击过来的
        // 如果oper是空的，说明 不是通过表单的查询按钮点击过来的
        // String keyword = null;

        if (!isEmpty(oper) && "search".equals(oper)) {
            // 说明是点击表单查询发送过来的请求
            // 此时，pageNo应该还原为1 ， keyword应该从请求参数中获取
            pageNo = 1;
            // keyword = request.getParameter("keyword");
            if (isEmpty(keyword)) {
                keyword = "";
            }
            // 将keyword保存（覆盖）到session中
            session.setAttribute("keyword", keyword);
            System.out.println("branch=" + branch);
            session.setAttribute("branch", branch);

            if (branchInfo.equals("admin")) {
                branchInfo = branch;
                fDao.Table = branchInfo + "inventory";// admin级别可以切换数据库
            }
            session.setAttribute("branch", branchInfo);

        } else {
            // 说明此处不是点击表单查询发送过来的请求（比如点击下面的上一页下一页或者直接在地址栏输入网址）
            // 此时keyword应该从session作用域获取
            pageNo = Optional.ofNullable(pageNo).orElse(1);
            // 如果不是点击的查询按钮，那么查询是基于session中保存的现有keyword进行查询
            keyword = Optional.ofNullable((String) session.getAttribute("keyword")).orElse("");
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

    private String edit(Integer id, String name, Float purchasePrice, Float salePrice, Float stock, String remark) {

        BaseInventory inventory = new BaseInventory(id, name, purchasePrice, salePrice, stock, remark);
        fDao.update(inventory);
        return "redirect:inventory.do";// 更新页面
    }

    private String del(Integer id) {
        fDao.del(id);
        return "redirect:inventory.do";// 更新页面

    }

    private String select(Integer id, HttpSession session) {
        session.setAttribute("selected", fDao.getByid(id));
        // super.processTemplate("editinventory", request, response);
        return "editinventory";//
    }
}
