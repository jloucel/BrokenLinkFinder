package com.brokenlinkfinder.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by loucelj on 10/11/2016.
 */
public class DBUtilities {

    private Connection getDBConnection(){

        String dbPath = "jdbc:sqlite:./DB_Utils/runresults.db";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(dbPath);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void saveTestResults(String url, String result){

        String sql = ("INSERT INTO testlog (url, dtstamp, results) " +
                     "VALUES ( ?,datetime('now', 'localtime')," +
                     "?)");

        try(Connection conn = this.getDBConnection();
            PreparedStatement insertRecord  = conn.prepareStatement(sql)){
            insertRecord.setString(1, url);
            insertRecord.setString(2,result);
            insertRecord.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> getUrlList(){

        String sql = "SELECT testid, url, dtstamp, results FROM testlog";
        List<String> urls = new ArrayList<String>();

        try (Connection conn = this.getDBConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            try {
                while(rs.next()){
                    String url = rs.getString("url");
                    if(!urls.contains(url))
                        urls.add(url);
                }
                return urls;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<String> getRunDates(String url){

        String sql = "SELECT dtstamp FROM testlog WHERE url = '" + url + "'";
        List<String> urls = new ArrayList<String>();

        try (Connection conn = this.getDBConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            try {
                while(rs.next()){
                    urls.add(rs.getString("dtstamp"));
                }
                return urls;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public String getResults(String url, String date){

        String sql = ("SELECT results FROM testlog WHERE url = '"
                     + url +"' and dtstamp = '" + date + "'");

        try (Connection conn = this.getDBConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            return rs.getString("results");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
