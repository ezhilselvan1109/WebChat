package com.webchat.member;

import com.webchat.database.DatabaseConnection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Database {
    public static String getOneList(int unique_id,String email){
        List<Membership> list=new ArrayList<>();
        try{
            Connection connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps=null;
            if (email==null){
                ps=connection.prepareStatement("select * from membership  where member_uniqeid=?");
                ps.setInt(1, unique_id);
            }else {
                ps=connection.prepareStatement("select * from membership  where member_email=?");
                ps.setString(1,email);
            }
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                InputStream imageStream = rs.getBinaryStream(7);
                String status="Active";
                if(rs.getBoolean(8)==false){
                    status="Sleeping";
                };
                byte[] imageData = imageStream.readAllBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageData);
                list.add(new Membership(rs.getInt(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),base64Image,status));
            }
            connection.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return list.toString();
    }
    public static String getFullList(String email){
        List<Membership> list=new ArrayList<>();
        try{
            Connection connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps=connection.prepareStatement("select * from membership where not member_email=? order by member_id");
            ps.setString(1,email);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                InputStream imageStream = rs.getBinaryStream(7);
                String status="Active";
                if(rs.getBoolean(8)==false){
                    status="Sleeping";
                };
                byte[] imageData = imageStream.readAllBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageData);
                list.add(new Membership(rs.getInt(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),base64Image,status));
            }
            connection.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return list.toString();
    }
    public static String getSearchingList(String email,String name){
        List<Membership> list=new ArrayList<>();
        try{
            Connection connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps=connection.prepareStatement("select * from membership where not member_email=? order by member_id");
            ps.setString(1,email);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                String first_name=rs.getString(3).toLowerCase();
                String last_name=rs.getString(4).toLowerCase();
                if(first_name.contains(name.toLowerCase()) || last_name.contains(name.toLowerCase()) ) {
                    InputStream imageStream = rs.getBinaryStream(7);
                    String status="Active";
                    if(rs.getBoolean(8)==false){
                        status="Sleeping";
                    };
                    byte[] imageData = imageStream.readAllBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageData);
                    list.add(new Membership(rs.getInt(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),base64Image,status));
                }
            }
            connection.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return list.toString();
    }

    public static void setStatusFalse(int id){
        try {
            Connection connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps = connection.prepareStatement("update membership set is_active=false where member_uniqeid=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void setStatusTrue(String email){
        try {
            Connection connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps = connection.prepareStatement("update membership set is_active=true where member_email=?");
            ps.setString(1, email);
            ps.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
