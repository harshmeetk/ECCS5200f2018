package edu.northeastern.cs5200;

import java.sql.*;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;


public class BaseDao {
    JsonParser parser = new JsonParser();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet results = null;
    int res = 0;

    public BaseDao(Connection connection) {
        this.connection = connection;
    }

    public String checkTable(String tableName, List keys) throws SQLException {
        Statement stmtOBj = connection.getConnectionInstance().createStatement();
        ResultSet rs = null;
        try {
            rs = stmtOBj.executeQuery("select * from " + tableName);
        } catch (Exception e) {
            return "noTable";
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        System.out.println("No. of columns : " + rsmd.getColumnCount());
        Set<String> keySet = new HashSet<>(keys);
        for (int i = 2; i <= rsmd.getColumnCount(); i++) {
            if (keySet.contains(rsmd.getColumnName(i))) keySet.remove(rsmd.getColumnName(i));
        }
        return keySet.size() == 0 ? "sameTable" : alterTable(tableName, keySet);
    }

    public void createNewTable(String tableName, List<String> keys) throws SQLException {
        String CREATE_TABLE_QUERY = "CREATE TABLE " + tableName + " (id INT(11) NOT NULL AUTO_INCREMENT ";
        //emp_fname VARCHAR(20) DEFAULT NULL, " +
        //	"emp_lname VARCHAR(20) DEFAULT NULL, PRIMARY KEY (emp_id));";
        for (String k : keys) {
            CREATE_TABLE_QUERY += ", " + k + " VARCHAR(1024)";
        }
        CREATE_TABLE_QUERY += " ,PRIMARY KEY (id) )";
        //System.out.println(CREATE_TABLE_QUERY);
        Statement stmtOBj = connection.getConnectionInstance().createStatement();

        // DDL Statement 1 - Create Database Schema!
        System.out.println("\n=======CREATE " + tableName + " TABLE=======");
        stmtOBj.executeUpdate(CREATE_TABLE_QUERY);
        System.out.println("\n=======Table IS SUCCESSFULLY CREATED=======\n");
    }

    public void insertintoTable(String tableName, List<String> keys, List<String> values) throws SQLException {
        System.out.println("\"\\n=======Inserting into" + tableName + " TABLE=======");
        String insertIntoTable = "INSERT INTO " + tableName + "( ";
        for (int i = 0; i < keys.size(); i++) {
            insertIntoTable += keys.get(i) + ", ";
        }
        insertIntoTable = insertIntoTable.substring(0, insertIntoTable.length() - 2);
        insertIntoTable += ") VALUES ( ";

        for (int i = 0; i < values.size(); i++) {
            insertIntoTable += values.get(i) + ", ";
        }
        insertIntoTable = insertIntoTable.substring(0, insertIntoTable.length() - 2);
        insertIntoTable += ")";
        Statement stmtOBj = connection.getConnectionInstance().createStatement();
        stmtOBj.executeUpdate(insertIntoTable);
    }

    public String alterTable(String tableName, Set<String> keys) throws SQLException {
        String ALTER_TABLE_QUERY = "ALTER TABLE " + tableName;// ADD COLUMN  emp_joining_date VARCHAR(20) DEFAULT NULL;";
        for (String k : keys) {
            ALTER_TABLE_QUERY += " ADD COLUMN " + k + "  VARCHAR(1024),";
        }
        ALTER_TABLE_QUERY = ALTER_TABLE_QUERY.substring(0, ALTER_TABLE_QUERY.length() - 1);
        Statement stmtOBj = connection.getConnectionInstance().createStatement();
        stmtOBj.executeUpdate(ALTER_TABLE_QUERY);
        System.out.println(ALTER_TABLE_QUERY);
        return "alterTable";
    }

    public String createTable(String tableName, String body) throws SQLException {
        JsonObject h = parser.parse(body).getAsJsonObject();
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (Map.Entry<String, JsonElement> e : h.entrySet()) {
            keys.add(e.getKey());
            values.add(e.getValue().toString());
        }
        String tableCheck = checkTable(tableName, keys);
        if (tableCheck.equals("noTable")) {
            createNewTable(tableName, keys);
        } else if (tableCheck.equals("alterTable")) {
            System.out.println("Altering Table " + tableName);
        }
        insertintoTable(tableName, keys, values);
        return findRecord(tableName,keys,values);
    }
    public String findRecord(String tableName, List<String> keys, List<String> values) throws SQLException {
        Statement stmtOBj = connection.getConnectionInstance().createStatement();
        ResultSet rs = null;
        String find = "select * from " + tableName + " where ";

        for (int i = 0; i < keys.size(); i++) {
            find += keys.get(i) + "=" + values.get(i) + " and ";
        }
        find = find.substring(0,find.length() - 4);
        System.out.println(find);
        try {
            rs = stmtOBj.executeQuery(find);
        } catch (Exception e) {
            return null;
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        System.out.println("No. of columns : " + rsmd.getColumnCount());
        Set<String> e = new HashSet<>();
        for (int i = 2; i <= rsmd.getColumnCount(); i++) {
            e.add(rsmd.getColumnName(i));
        }
        JsonArray res = new JsonArray();
        while(rs.next()){
            JsonObject j = new JsonObject();
            String temp = "{";
            for(int i = 2; i <= rsmd.getColumnCount(); i++){
                temp +=  "\"" + rsmd.getColumnName(i) + "\":\"" + rs.getString(rsmd.getColumnName(i)) + "\",";
            }
            temp = temp.substring(0,temp.length() - 1);
            temp += "}";
            System.out.println(temp);
            res.add(parser.parse(temp).getAsJsonObject());
        }
        return res.size() == 0 ? null : res.get(0).toString();
    }

    public String updateTable(String tableName, String body,String id) throws SQLException {
        JsonObject h = parser.parse(body).getAsJsonObject();
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (Map.Entry<String, JsonElement> e : h.entrySet()) {
            keys.add(e.getKey());
            values.add(e.getValue().toString());
        }
        String tableCheck = checkTable(tableName, keys);
        if (tableCheck.equals("noTable") || getByid(tableName,id) == null) {
            return null;
        } else if (tableCheck.equals("alterTable")) {
            System.out.println("Altering Table " + tableName);
        }
        updateRecords(tableName, keys, values,id);
        return findRecord(tableName,keys,values);
    }
    public void updateRecords(String tableName, List<String> keys, List<String> values, String id) throws SQLException {
        System.out.println("\"\\n=======updating into" + tableName + " TABLE=======");
        String insertIntoTable = "UPDATE " + tableName + " set ";
        for (int i = 0; i < keys.size(); i++) {
            insertIntoTable += keys.get(i) + "= " + values.get(i) + ",";
        }
        insertIntoTable = insertIntoTable.substring(0, insertIntoTable.length() - 1);
        insertIntoTable += " where id = " + id;
        System.out.println(insertIntoTable);
        Statement stmtOBj = connection.getConnectionInstance().createStatement();
        stmtOBj.executeUpdate(insertIntoTable);
    }
    public String getFromTable(String tableName) throws SQLException {
        Statement stmtOBj = connection.getConnectionInstance().createStatement();
        ResultSet rs = null;
        try {
            rs = stmtOBj.executeQuery("select * from " + tableName);
        } catch (Exception e) {
            return null;
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        System.out.println("No. of columns : " + rsmd.getColumnCount());
        Set<String> e = new HashSet<>();
        for (int i = 2; i <= rsmd.getColumnCount(); i++) {
           e.add(rsmd.getColumnName(i));
        }
        JsonArray res = new JsonArray();
        while(rs.next()){
           JsonObject j = new JsonObject();
           String temp = "{";
           for (String key : e){
             temp +=  "\"" + key + "\":\"" + rs.getString(key) + "\",";
              // System.out.println(temp);
           }
           temp = temp.substring(0,temp.length() - 1);
           temp += "}";
            System.out.println(temp);
           res.add(parser.parse(temp).getAsJsonObject());
        }
        return res.toString();
    }
    public String getByid(String tableName, String id) throws SQLException {
        Statement stmtOBj = connection.getConnectionInstance().createStatement();
        ResultSet rs = null;
        try {
            rs = stmtOBj.executeQuery("select * from " + tableName + " where id =  " + id);
        } catch (Exception e) {
            return null;
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        System.out.println("No. of columns : " + rsmd.getColumnCount());
        Set<String> e = new HashSet<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            e.add(rsmd.getColumnName(i));
        }
        JsonArray res = new JsonArray();
        while(rs.next()){
            JsonObject j = new JsonObject();
            String temp = "{";
            for (String key : e){
                temp +=  "\"" + key + "\":\"" + rs.getString(key) + "\",";
                // System.out.println(temp);
            }
            temp = temp.substring(0,temp.length() - 1);
            temp += "}";
            System.out.println(temp);
            res.add(parser.parse(temp).getAsJsonObject());
        }
        return res.size() == 0 ? null : res.get(0).toString();
    }
    public Integer deleteByid(String tableName, String id) throws SQLException {
        Statement stmtOBj = connection.getConnectionInstance().createStatement();
        int rs = -1;
        try {
            rs = stmtOBj.executeUpdate("delete  from " + tableName + " where id =  " + id);
        } catch (Exception e) {
            return null;
        }
        return rs == 0 ? null : rs;
    }
    public Integer deleteTable(String tableName) throws SQLException {
        Statement stmtOBj = connection.getConnectionInstance().createStatement();
        int rs = -1;
        try {
            rs = stmtOBj.executeUpdate("drop  table " + tableName);
        } catch (Exception e) {
            return null;
        }
        return rs;
    }
}