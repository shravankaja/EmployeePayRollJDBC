package com.employeepayrolljdbc;

import com.mysql.jdbc.exceptions.*;

import java.awt.desktop.*;
import java.sql.*;
import java.time.*;
import java.util.*;

public class EmployeePayRollDBService {
    int noOfEmployees = 0;
    //Defining Singleton object by making a private constructor
    private static EmployeePayRollDBService employeePayRollDBService;
    private PreparedStatement getEmployeePayRollData;

    public EmployeePayRollDBService() {

    }

    // method to create a new singleton object

    public ArrayList<HashMap<Integer, List<EmployeePayRollService>>> readData() {
        ArrayList<EmployeePayRollService> listOfDataObjects = new ArrayList<>();
        String sql = "select ed.*,p.net_pay,p.taxabale_pay,p.deductions,p.income_tax,edd.department_id,c.name,a.street_name,a.house_no,a.state,a.city,a.country,a.zip,a.address_type from employee_details as ed inner join payroll as p on ed.employee_id=p.employee_id   \n" +
                " inner join employee_departments as edd on ed.employee_id=edd.employee_id inner join company as c on c.employee_id=ed.employee_id inner join address as a on a.employee_id=ed.employee_id;";
        return this.getListFromResultSet(sql);
    }

    private ArrayList<HashMap<Integer, List<EmployeePayRollService>>> getListFromResultSet(String sql) {
        ArrayList<HashMap<Integer, List<EmployeePayRollService>>> list = new ArrayList<>();
        List<EmployeePayRollService> listOfTablesAsObjects = new ArrayList<>();
        HashMap<Integer, List<EmployeePayRollService>> listOfTablesOfEachEmployee = new HashMap<>();
        try {
            Connection connection = this.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("employee_id");
                double phone = resultSet.getDouble("phone");
                String gender = resultSet.getString("gender");
                int salary = resultSet.getInt("salary");
                LocalDate startdate = resultSet.getDate("start_date").toLocalDate();
                String nameEmployee = resultSet.getString("name");
                int netPay = resultSet.getInt("net_pay");
                int taxabalePay = resultSet.getInt("taxabale_pay");
                int deductions = resultSet.getInt("deductions");
                int incomeTax = resultSet.getInt("income_tax");
                int departentId = resultSet.getInt("department_id");
                String companyName = resultSet.getString("name");
                String streetName = resultSet.getString("street_name");
                int houseNo = resultSet.getInt("house_no");
                String state = resultSet.getString("state");
                String city = resultSet.getString("city");
                String country = resultSet.getString("country");
                int zip = resultSet.getInt("zip");
                String addressType = resultSet.getString("address_type");
                listOfTablesAsObjects.add(new EmployeePayRollService(nameEmployee, phone, startdate, salary, id, gender));
                listOfTablesAsObjects.add(new EmployeePayRollService(id, departentId));
                listOfTablesAsObjects.add(new EmployeePayRollService(id, companyName));
                listOfTablesAsObjects.add(new EmployeePayRollService(streetName, state, city, country, zip, id, addressType
                        , houseNo));
                listOfTablesOfEachEmployee.put(id, listOfTablesAsObjects);
            }
            noOfEmployees = listOfTablesAsObjects.size();
            list.add(listOfTablesOfEachEmployee);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public int getNoOfEmployees() {
        return noOfEmployees;
    }


    public Connection connectToDatabase() {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_services?allowPublicKeyRetrieval=true&useSSL=false";
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

    public int entryOfNewEmployeeDetails(String name, double phone, String startDate, int salary, String gender,
                                         String departmentName, int departmentID, String companyName,
                                         String street_name, String state, String city, String country, int zip, String
                                                 addressType, int houseNo) throws SQLException, EmployeePayRollException {
        int result = 0;
        int employeeID = 0;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = this.connectToDatabase();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql = String.format("insert into employee_details (phone,gender,salary,start_date,name) " +
                "values (%s,'%s',%d,cast('%s' as date),'%s'" +
                ")", phone, gender, salary, startDate, name);
        try {
            result = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            result = result + 1;
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) employeeID = resultSet.getInt(1);
            System.out.println("auto generated :" + employeeID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            double deductions = salary * 0.2;
            double taxable_pay = salary - deductions;
            double tax = taxable_pay * 0.1;
            double net_pay = salary - tax;
            String sqlTwo = String.format("insert into payroll (employee_id,net_pay,taxabale_pay,deductions,income_tax) " +
                    "values (%s,%s,%s,%s,%s)", employeeID, salary, taxable_pay, deductions, tax);
            int resultTwo = statement.executeUpdate(sqlTwo);
            result = result + 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            int department_id = 0;
            String sqlFive = String.format("select department_id from  departments where name='%s'", departmentName);
            ResultSet resultSet = statement.executeQuery(sqlFive);
            while (resultSet.next()) {
                department_id = resultSet.getInt("department_id");
            }
            if (department_id == 0) {
                String sqlThree = String.format("insert into departments (name,department_id) " +
                        "values ('%s',%s)", departmentName, departmentID);
                int resultTwo = statement.executeUpdate(sqlThree);
                result = result + 1;
                String sqlFour = String.format("insert into employee_departments (employee_id,department_id)" +
                        "values (%s,%s)", employeeID, departmentID);
                int resulThree = statement.executeUpdate(sqlFour);
            } else if (department_id > 0) {
                String sqlFour = String.format("insert into employee_departments (employee_id,department_id)" +
                        "values (%s,%s)", employeeID, department_id);
                int resulThree = statement.executeUpdate(sqlFour);
                result = result + 1;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            ArrayList<Integer> employeeIds = new ArrayList<>();
            String sqlFive = String.format("select employee_id from company where  name ='%s'", companyName);
            ResultSet resultSet = statement.executeQuery(sqlFive);
            while (resultSet.next()) {
                int employeeIdTest = resultSet.getInt("employee_ID");
                employeeIds.add(employeeIdTest);

            }
            if (employeeIds.contains(employeeID)) {
                System.out.println("Employee has already been asigned");
            } else if (!employeeIds.contains(employeeID)) {
                String sqlSix = String.format("insert into company (name,employee_id) values ('%s',%s)",
                        companyName, employeeID);
                int resultSeven = statement.executeUpdate(sqlSix);
                result = result + 1;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            String sqlEight = String.format(" insert into address (employee_id,street_name,house_no," +
                            "state,city,country,zip,address_type) values (%s,'%s',%s,'%s','%s','%s',%s,'%s') ",
                    employeeID, street_name, houseNo, state, city, country, zip, addressType);
            int resultFour = statement.executeUpdate(sqlEight);
            result = result + 1;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        try {
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;
    }
}
