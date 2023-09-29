package com.webchat.home;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/home")
@MultipartConfig
public class Servlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Servlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email=request.getParameter("email");
        String password=request.getParameter("password");
        String res=Database.get(email,password);
        response.getWriter().print(res);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String first_name=request.getParameter("first-name");
        String last_name=request.getParameter("last-name");
        String email=request.getParameter("email");
        String password=request.getParameter("password");
        Part filePart = request.getPart("photo");
        InputStream inputStream = filePart.getInputStream();
        String res=Database.post(first_name,last_name,email,password,inputStream);
        response.getWriter().print(res);
    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email=request.getParameter("email");
        String password=request.getParameter("password");
        String res=Database.update(email,password);
        response.getWriter().print(res);
    }
}
