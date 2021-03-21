package com.employeepayrolljdbc;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.*;
import java.util.*;

public class EmployeePayRollServiceTest {

    EmployeePayRollService employeePayRollService = new EmployeePayRollService();
    EmployeePayRollDBService employeePayRollDBService = new EmployeePayRollDBService();

    @Test
    void whenInitlializedProgramWeShouldBeAbleToLoadSqlDriverClass() {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_services?useSSL=false";
        String userName = "root";
        String password = "Addtexthere25";
        Connection result = employeePayRollDBService.connectToDatabase();
        Assertions.assertEquals("com.mysql.jdbc.JDBC4Connection@34251ec", result);
    }

    @Test
    void whenJdbcDriverClassIsLoadedWeShouldBeAbleToObtainListOfDrivers() {
        ArrayList<String> listOfDriversTest = new ArrayList<>();
        listOfDriversTest.add("com.mysql.jdbc.Driver");
        listOfDriversTest.add("com.mysql.fabric.jdbc.FabricMySQLDriver");
        Assertions.assertEquals(listOfDriversTest, employeePayRollDBService.listDrivers());
    }

    @Test
    void givenDataBaseTableWeShouldBeAbleToReadAllTheRecords() {
        Assertions.assertEquals(16, employeePayRollService.readData());
    }

    @Test
    void add() throws SQLException, EmployeePayRollException {
        EmployeePayRollDBService employeePayRollDBService = new EmployeePayRollDBService();
        Assertions.assertEquals(6, employeePayRollService.writeData("Tavan", 123456,
                "2018-02-01", 55000, "M", "Development", 123, "TCS",
                "RamNagar", "Tealanagan", "Hydderabad", "India", 50074, "Home", 456));
    }
}
