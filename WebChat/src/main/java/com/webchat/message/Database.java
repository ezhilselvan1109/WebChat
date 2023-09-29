package com.webchat.message;

import com.google.gson.Gson;
import com.webchat.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static String get(int incoming_id,int outgoing_id){
        Gson gson=new Gson();
        List<Message> list=new ArrayList<>();
        try{
            Connection connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps=connection.prepareStatement("select * from messages where ( incoming_id=? and outgoing_id=? ) or ( incoming_id=? and outgoing_id=? )");
            ps.setInt(1, incoming_id);
            ps.setInt(2, outgoing_id);
            ps.setInt(3, outgoing_id);
            ps.setInt(4, incoming_id);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                list.add(new Message(rs.getInt(2),rs.getInt(3), rs.getString(4)));
            }
            connection.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return gson.toJson(list);
    }
    public static void post(int incoming_id,int outgoing_id,String messages){
        try{
            Connection connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps=connection.prepareStatement("insert into messages(incoming_id,outgoing_id,messages) values (?,?,?)");
            ps.setInt(1, incoming_id);
            ps.setInt(2, outgoing_id);
            ps.setString(3,messages);
            ps.executeUpdate();
            connection.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public static String getLastMessages(int id){
        Gson gson=new Gson();
        List<Message> list=new ArrayList<>();
        try{
            Connection connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps=connection.prepareStatement("SELECT t1.* FROM messages t1\n" +
                    "JOIN (\n" +
                    "    SELECT incoming_id ,outgoing_id, MAX(message_id) as message_id\n" +
                    "    FROM messages\n" +
                    "    GROUP BY incoming_id ,outgoing_id\n" +
                    ") t2 ON t1.message_id = t2.message_id where t2.incoming_id=? or t2.outgoing_id=?\n");
            ps.setInt(1, id);
            ps.setInt(2, id);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                list.add(new  Message(rs.getInt(1),rs.getInt(2), rs.getInt(3), rs.getString(4)));
            }
            connection.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return gson.toJson(list);
    }
}
