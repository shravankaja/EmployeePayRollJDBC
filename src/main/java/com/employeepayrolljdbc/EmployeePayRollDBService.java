package com.employeepayrolljdbc;

import java.sql.*;
import java.time.*;
import java.util.*;

public class EmployeePayRollDBService {

    //Defining Singleton object by making a private constructor
    private static EmployeePayRollDBService employeePayRollDBService;
    private PreparedStatement getEmployeePayRollData;

    private EmployeePayRollDBService() {

    }

    // method to create a new singleton object
    public static EmployeePayRollDBService getInstance() {
        if (employeePayRollDBService == null) {
            employeePayRollDBService = new EmployeePayRollDBService();
        }
        return employeePayRollDBService;
    }

    private void preparedStatmentForWholeTableData() {
        try {
            Connection connection = this.connectToDatabase();
            String sql = "select * from employee_payroll where name = ?";
            getEmployeePayRollData = connection.prepareStatement(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<EmployeePayRollService> getDataFromEmployeePayRollTable(String name) {
        ArrayList<EmployeePayRollService> list = new ArrayList<>();
        if (getEmployeePayRollData == null) this.preparedStatmentForWholeTableData();
        try {
            getEmployeePayRollData.setString(1, name);
            ResultSet resultSet = getEmployeePayRollData.executeQuery();
            list = this.getListFromResultSet(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    private ArrayList<EmployeePayRollService> getListFromResultSet(ResultSet resultSet) {
        ArrayList<EmployeePayRollService> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int salary = resultSet.getInt("salary");
                LocalDate startdate = resultSet.getDate("start").toLocalDate();
                list.add(new EmployeePayRollService(name, startdate, salary, id));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public ArrayList<EmployeePayRollService> readData() {
        ArrayList<EmployeePayRollService> listOfDataObjects = new ArrayList<>();
        String sql = "select * from employee_payroll";
        return this.executeSqlAndReturnList(sql);
    }

    // Connection connection;  //database connection
    public Connection connectToDatabase() {
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll_service?allowPublicKeyRetrieval=true&useSSL=false";
        String userName = "root";
        String password = "Addtexthere25";
        Connection connection = null;  //database connection
        String connectionString = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No drivers loaded ", e);
        }
        try {
            System.out.println("Connectin to " + jdbcURL);
            connection = DriverManager.getConnection(jdbcURL, userName, password);
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

    public int updateSalary(String name, int salary) {
        String sql = String.format("update  employee_payroll set salary=%d where name='%s'", salary, name);
        try {
            Connection connection = this.connectToDatabase();
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public ArrayList<EmployeePayRollService> findEmployesJoinesForDateRange(String dateStart, String dateEnd) {
        String sql = String.format("select * from employee_payroll where start between cast('%s' as date) " +
                "and cast('%s' as date)", dateStart, dateEnd);
        return this.executeSqlAndReturnList(sql);
    }

    public ArrayList<EmployeePayRollService> executeSqlAndReturnList(String sql) {
        ArrayList<EmployeePayRollService> list = new ArrayList<>();
        try {
            Connection connection = this.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            list = this.getListFromResultSet(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public HashMap<String, Integer> performOperationsOnSalaryOf(String operation) {
        HashMap<String, Integer> resultList = new HashMap<>();
        String sql = String.format("select gender,%s(salary) from employee_payroll group by gender", operation);
        try {
            Connection connection = this.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                String coloumnName = operation + "(salary)";
                int salary = resultSet.getInt(coloumnName);
                resultList.put(gender, salary);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultList;
    }
}
