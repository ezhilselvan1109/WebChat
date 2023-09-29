package com.webchat.home;

import com.webchat.database.DatabaseConnection;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
    public static String get(String email,String password){
        boolean flag= isExists(email);
        String response;
        boolean flagValue=true;
        if (flag){
            try {
                Connection connection = DatabaseConnection.getDbConnection();
                PreparedStatement ps = connection.prepareStatement("select * from membership");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getString(5).equals(email) && rs.getString(6).equals(password)) {
                        flagValue=false;
                        break;
                    }
                }
                connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(flagValue){
                response="wrong";
            }else{
                response="true";
            }
        }else{
            response="false";
        }
        return response;
    }

    public static String update(String email,String password){
        String response;
        boolean flag= isExists(email);
        if (flag){
            try{
                Connection connection = DatabaseConnection.getDbConnection();
                PreparedStatement ps=connection.prepareStatement("update membership set member_password=? where member_email=?");
                ps.setString(1,password);
                ps.setString(2,email);
                ps.executeUpdate();
                connection.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
            response="true";
        }else {
            response="false";
        }
        return response;
    }
    public static String post(String first_name,String last_name,String email,String password,InputStream inputStream){
        boolean flag= isExists(email);
        String response=null;
        if (!flag){
            try{
                Connection connection = DatabaseConnection.getDbConnection();
                PreparedStatement ps=connection.prepareStatement("insert into membership(member_first_name,member_last_name,member_email,member_password,member_photo) values (?,?,?,?,?)");
                ps.setString(1,first_name);
                ps.setString(2,last_name);
                ps.setString(3,email);
                ps.setString(4,password);
                ps.setBinaryStream(5, inputStream);
                if(ps.executeUpdate()==1){
                    response="Register";
                }
                connection.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }else {
            response="AlreadyExist";
        }
        return response;
    }
    private static boolean isExists(String email){
        try{
            Connection connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps=connection.prepareStatement("select * from membership");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                if(rs.getString(5).equals(email)){
                    return true;
                }
            }
            connection.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
