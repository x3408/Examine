package com.xc.Servlet;

import com.xc.Service.TeacherService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/TeacherServiceServlet")
public class TeacherServiceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String [] str = new String[] {"课程名","课程","章节","标题","显示时间提交时间"};
        TeacherService ts = new TeacherService("1");
        ArrayList<String> arr = ts.showCurrentHomework();
        for (int i = 0; i < 5; i++) {
            request.getSession(false). setAttribute(str[i], arr.get(i));
        }

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
