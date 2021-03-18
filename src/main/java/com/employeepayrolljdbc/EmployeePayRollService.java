package com.employeepayrolljdbc;

import java.sql.*;
import java.util.*;

public class EmployeePayRollService {
    // Connection connection;  //database connection
    public String connectToDatabase(String url, String userName, String password) {
        Connection connection;  //database connection
        String connectionString = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No drivers loaded ", e);
        }
        try {
            System.out.println("Connectin to " + url);
            connection = DriverManager.getConnection(url, userName, password);
            System.out.println(connection);
            connectionString = connection.toString();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connectionString;
    }

    public ArrayList<String> listDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        ArrayList<String> listOfDrivers = new ArrayList<>();
        while (driverList.hasMoreElements()) {
            Driver driver = (Driver) driverList.nextElement();
            System.out.println("  " + driver.getClass().getName());
            listOfDrivers.add(driver.getClass().getName());
        }
        return listOfDrivers;
    }

    public static void main(String Args[]) {
        System.out.println("Welcome to payroll service");
        // localhost is the server name where database exsists or it can be IP address
        // 3306 is the port number , payroll_services is the database name
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_services?allowPublicKeyRetrieval=true&useSSL=false";
        String userName = "root";
        String password = "Addtexthere25";
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        employeePayRollService.connectToDatabase(jdbcURL, userName, password);
        employeePayRollService.listDrivers();
    }
}



