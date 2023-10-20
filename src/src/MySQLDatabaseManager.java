
import java.sql.Connection;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oguzy
 */
public class MySQLDatabaseManager extends BaseDatabaseManager{
        
    @Override
    public Connection connect(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/todolist";
        
        Connection conn;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            
            conn = null;
        }
        
        return conn;    
    }
    
    boolean checkListCount(Connection conn){
        try{
            Statement statement = conn.createStatement();
            
            int sizeOfStatements;
            
            ResultSet countOfList = statement.executeQuery("SELECT COUNT(*) FROM lists");
            
            countOfList.next();
            sizeOfStatements = countOfList.getInt(1);
            
            statement.close();
            
            if(sizeOfStatements == 0)
                return false;
            
            return true;
        }catch(Exception e){System.out.println(e.toString());}
        
        return false;
    }
    
    boolean checkListCount(Connection conn, String listName){
        try{
            Statement statement = conn.createStatement();
            
            int sizeOfStatements;
            
            ResultSet countOfList = statement.executeQuery("SELECT COUNT(*) FROM lists WHERE listName = '" 
                    + listName + "'");
            
            countOfList.next();
            sizeOfStatements = countOfList.getInt(1);
            
            statement.close();
            
            if(sizeOfStatements == 0)
                return false;
            
            return true;
        }catch(Exception e){System.out.println(e.toString());}
        
        return false;
    }
    
    @Override
    public String[] getList(Connection conn) {
        try {            
            if(!checkListCount(conn))
                return new String[]{"", "", ""};
            
            Statement statement = conn.createStatement();
            
            ResultSet listItems = statement.executeQuery("SELECT * FROM lists");
            
            listItems.next();
            
            String willItems = listItems.getString(2);
            String nowItems = listItems.getString(3);
            String doneItems = listItems.getString(4);
            
            statement.close();
            
            return new String[]{willItems, nowItems, doneItems};
            
            
        } catch (SQLException ex) {
            System.out.println(ex.toString());
           // Logger.getLogger(MySQLDatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new String[]{"", "", ""};
    }

    @Override
    public String[] getList(Connection conn, String listName) {
        try{
            if(!checkListCount(conn))
                return new String[]{"", "", ""};
            
            Statement statement = conn.createStatement();
            
            ResultSet listItems = statement.executeQuery("SELECT * FROM lists WHERE listName = '" + listName + "'");
            
            listItems.next();
            
            String willItems = listItems.getString(2);
            String nowItems = listItems.getString(3);
            String doneItems = listItems.getString(4);
            
            statement.close();
            
            return new String[]{willItems, nowItems, doneItems};
            
        }catch(Exception e){System.out.println(e.toString());}
        
        return new String[]{"", "", ""};
    }

    @Override
    public void saveList(Connection conn, String listName, String[] items) {
        if(checkListCount(conn, listName)){
            String updateSQL = "UPDATE lists " +
                  "SET will = ?, now = ?, done = ? " +
                  "WHERE listName = ?";
            try{
                PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);
                
                preparedStatement.setString(1, items[0]);
                preparedStatement.setString(2, items[1]);
                preparedStatement.setString(3, items[2]);
                preparedStatement.setString(4, listName);
                
                preparedStatement.executeUpdate();
                
                preparedStatement.close();
                
                return;
                
            }catch(Exception e){System.out.println(e.toString());}
            
            return;
        }
        
        String insertSQL = "INSERT INTO lists(will,now,done,listName) "
                + "VALUES(?,?,?,?)";
        
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);

            preparedStatement.setString(1, items[0]);
            preparedStatement.setString(2, items[1]);
            preparedStatement.setString(3, items[2]);
            preparedStatement.setString(4, listName);

            int columns = preparedStatement.executeUpdate();
            
            System.out.println(columns);
            
            preparedStatement.close();
            
            return;
                
        }catch(Exception e){System.out.println(e.toString());
        }
        
    }

    @Override
    public void removeList(Connection conn, String listName) {
        System.out.println("Girdi");
        try{
            if(!checkListCount(conn, listName))
                return;
            
            PreparedStatement statement = conn.prepareStatement("DELETE FROM lists WHERE listName = ?");
            
            statement.setString(1, listName);
            
            statement.executeUpdate();
            
            statement.close();
            
        }catch(Exception e){System.out.println(e.toString());}
        
    }
    
    
    
}
