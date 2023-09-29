package com.webchat.member;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/member")
public class Servlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Servlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String res=null;
        if(request.getParameter("key").equals("full")){
            String email=request.getParameter("email");
            res=Database.getFullList(email);
        }else if (request.getParameter("key").equals("email")){
            String email=request.getParameter("email");
            res=Database.getOneList(0,email);
        }else if (request.getParameter("key").equals("id")){
            int unique_id=Integer.parseInt(request.getParameter("unique_id"));
            res=Database.getOneList(unique_id,null);
        }else {
            String email=request.getParameter("email");
            String name=request.getParameter("name");
            res=Database.getSearchingList(email,name);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"members\": " + res + "}");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("type").equals("false")){
            int id=Integer.parseInt(request.getParameter("id"));
            Database.setStatusFalse(id);
        }else{
            String email=request.getParameter("email");
            Database.setStatusTrue(email);
        }
    }
}