package sample;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class trx {

    public static Connection conn;

    public long getBalance() {
        long human_readable = 0;
        String url = "https://apilist.tronscan.org/api/account?address=TLkyqyLBhLqYC3tvMRb41RmcudGSbxXYUH";

            try {
                URL obj = new URL(url);
                BufferedReader reader = new BufferedReader(new InputStreamReader(obj.openStream()));

                String line = "";
                String message = "";

                while ((line = reader.readLine()) != null)
                    message += line;

                    JSONObject json = new JSONObject(message);
                    long balance = Long.parseLong(json.getJSONArray("tokenBalances").getJSONObject(0).get("balance").toString());
                    human_readable = balance / 1000000;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return human_readable;
        }

    public void inputInSqlite() throws ClassNotFoundException, SQLException {
        trx myClass = new trx();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

            long balance = myClass.getBalance();
            String sql2 = String.format("INSERT INTO trx(Balance, Date) VALUES(%d, '%s')", balance, dateFormat.format(date));


            try {
                Statement stmt = conn.createStatement(); //команда которая принимает запросы
                ResultSet rs = stmt.executeQuery(sql2);//выполнение запроса
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

    }

    public HashMap outputFromSqlite(){

        String sql = "SELECT * FROM trx";
        HashMap<String, Integer> pointsMap = new HashMap<String, Integer>();
        getBalance();
        try {
            Statement stmt  = conn.createStatement(); //команда которая принимает запросы
            ResultSet rs = stmt.executeQuery(sql);//выполнение запроса

            while (rs.next()) {
                pointsMap.put(rs.getString("Date"), rs.getInt("Balance"));

            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return pointsMap;
    }

    trx()  throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:db/db.db");
    }
}
