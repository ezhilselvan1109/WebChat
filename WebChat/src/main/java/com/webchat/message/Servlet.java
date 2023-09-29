package com.webchat.message;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/message")
public class Servlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Servlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String res=null;
        String lastmsg=request.getParameter("key");
        if (lastmsg.equals("lastmsg")){
            int id=Integer.parseInt(request.getParameter("id"));
            res=Database.getLastMessages(id);
        }else {
            int incoming_id=Integer.parseInt(request.getParameter("incoming_id"));
            int outgoing_id=Integer.parseInt(request.getParameter("outgoing_id"));
            res= Database.get(incoming_id,outgoing_id);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": " + res + "}");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int incoming_id=Integer.parseInt(request.getParameter("incoming_id"));
        int outgoing_id=Integer.parseInt(request.getParameter("outgoing_id"));
        String messages=request.getParameter("messages");
        Database.post(incoming_id,outgoing_id,messages);
        response.getWriter().print("Inserted");
    }
}


