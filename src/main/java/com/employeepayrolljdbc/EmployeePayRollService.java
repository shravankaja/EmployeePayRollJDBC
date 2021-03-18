package com.employeepayrolljdbc;

import java.sql.*;
import java.time.*;
import java.util.*;

public class EmployeePayRollService {

    String name;
    LocalDate startDate;
    int salary;
    int id;

    public EmployeePayRollService() {

    }

    public EmployeePayRollService(String name, LocalDate startDate, int salary, int id) {
        this.name = name;
        this.startDate = startDate;
        this.salary = salary;
        this.id = id;
    }

    public ArrayList<EmployeePayRollService> readData(String url, String userName, String password) {
        ArrayList<EmployeePayRollService> listOfDataObjects = new ArrayList<>();
        String sql = "select * from employee_payroll";
        try {
            Connection connection = this.connectToDatabase(url, userName, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int salary = resultSet.getInt("salary");
                LocalDate startdate = resultSet.getDate("start").toLocalDate();
                listOfDataObjects.add(new EmployeePayRollService(name, startdate, salary, id));
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listOfDataObjects;
    }

    // Connection connection;  //database connection
    public Connection connectToDatabase(String url, String userName, String password) {
        Connection connection = null;  //database connection
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
        return connection;
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
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll_service?allowPublicKeyRetrieval=true&useSSL=false";
        String userName = "root";
        String password = "Addtexthere25";
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        employeePayRollService.connectToDatabase(jdbcURL, userName, password);
        employeePayRollService.listDrivers();
    }
}



