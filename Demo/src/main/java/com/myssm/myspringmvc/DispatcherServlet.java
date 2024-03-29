package com.myssm.myspringmvc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import javax.servlet.*;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//value属性等价于 urlPatterns 属性
//均可在xml中配置
@WebServlet(urlPatterns = "*.do", initParams = {
        @WebInitParam(name = "applicationContextPath", value = "applicationContext.xml")
})
public class DispatcherServlet extends ViewBaseServlet {

    private Map<String, Object> beanMap = new HashMap<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() throws ServletException {
        super.init();
        String applicationContextPath = getServletConfig().getInitParameter("applicationContextPath");
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(applicationContextPath);

        // 1.创建DocumentBuilderFactory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        NodeList beanNodeList = null;

        try {
            // 2.创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            // 3.创建Document对象
            Document document = documentBuilder.parse(inputStream);
            beanNodeList = document.getElementsByTagName("bean");
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        // *流式写法
        // beanMap = Stream
        // .iterate(0, i -> i + 1)
        // .limit(beanNodeList.getLength())
        beanMap = IntStream.range(0, beanNodeList.getLength())
                .mapToObj(beanNodeList::item)
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .collect(Collectors.toMap(node -> ((Element) node).getAttribute("id"), node -> {
                    try {
                        return Class.forName(((Element) node).getAttribute("class")).getDeclaredConstructor()
                                .newInstance();
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException
                            | ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } 
                    return null;
                    
                }));
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置编码
        request.setCharacterEncoding("UTF-8");
        // 假设url是： http://localhost:8080/pro15/hello.do
        // 那么servletPath是： /hello.do
        // 第1步： /hello.do -> hello 或者 /fruit.do -> fruit
        // 第2步： hello -> HelloController 或者 fruit -> FruitController
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(1, servletPath.length() - ".do".length());
        Object controllerBeanObj = beanMap.get(servletPath);
        String operate = request.getParameter("operate");
        if (operate == null || operate.equals("")) {
            operate = "index";
        }
        try {
            Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
            final String tempOperate = operate;
            int methodIndex = IntStream.range(0, methods.length)
                    .filter(i -> tempOperate.equals(methods[i].getName()))
                    .findAny().orElse(-1);// 假设没有重载函数
            Method method = methods[methodIndex];
            method.setAccessible(true);
            Parameter[] parameters = method.getParameters();
            Object[] parametersValues = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                System.out.print(parameters[i].getName());
                String paraName = parameters[i].getName();
                if (paraName.equals("session"))
                    parametersValues[i] = request.getSession();
                else if (request.getParameter(paraName) != null)
                    switch (parameters[i].getType().getSimpleName()) {
                        case "Integer":
                            parametersValues[i] = Integer.parseInt(request.getParameter(paraName));
                            break;
                        case "Float":
                            parametersValues[i] = Float.parseFloat(request.getParameter(paraName));
                            break;
                        case "Double":
                            parametersValues[i] = Double.parseDouble(request.getParameter(paraName));
                        default:
                            parametersValues[i] = request.getParameter(paraName);
                    }
                else
                    parametersValues[i] = null;

            }
            String returnStr = Optional.ofNullable((String) method.invoke(controllerBeanObj, parametersValues))
                    .orElse(null);
            if (returnStr != null)
                if (returnStr.startsWith("redirect:")) {// 重定向
                    response.sendRedirect(returnStr.substring("redirect:".length()));
                } else {// 渲染
                    super.processTemplate(returnStr, request, response);
                }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("operate值非法!");
        }
    }
}
