/**
 * Author: Tang Yuqian
 * Date: 2020/12/24
 */
package com.jdbc.learning;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 熟悉SPI模式
 */
public class JdbcTest {


    private static final String DB_URL = "jdbc:mysql://localhost:3306/eesy";
    private static final String USER = "root";
    private static final String PASS = "1234";

    public static void main(String[] args) throws ClassNotFoundException {

        JdbcTest.class.getClassLoader().loadClass("com.jvm.learning.ArrayObjectSize1");

        Connection conn = null;
        {
            try {
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                Statement stmt = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




}
