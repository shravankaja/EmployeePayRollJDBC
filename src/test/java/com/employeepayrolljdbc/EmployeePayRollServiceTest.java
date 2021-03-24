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
    void addNewRecord() throws SQLException, EmployeePayRollException {
        EmployeePayRollDBService employeePayRollDBService = new EmployeePayRollDBService();
        Assertions.assertEquals(6, employeePayRollService.writeData("Demo", 123456,
                "2018-02-01", 55000, "M", "Development", 123, "TCS",
                "RamNagar", "Tealanagan", "Hydderabad", "India", 50074, "Home", 456));
    }

    @Test
    void givenStringValueAndTableNameWeShouleBeAbleToGetNumberOfRowsUpdated() throws EmployeePayRollException {
        Assertions.assertEquals(1, employeePayRollService.upateRecord(41, "19971",
                "address", "zip"));
    }

    @Test
    void givenStartDateFindNumberOfEmployeesJoined() {
        Assertions.assertEquals(32, employeePayRollDBService
                .findEmployeesJoinedInSpecificDateRange("2018-01-01"));
    }

    @Test
    void givenOperationWeShouldBeAbleToGetResultAccordingtoGender() {
        HashMap<String, Integer> list = employeePayRollService
                .returnResultOfOperationPerformedOnSalaryBasedOnGender("avg");
        int result = list.get("M");
        Assertions.assertEquals(54642
                , result);
    }

    @Test
    void deleteRecordFromDatabase() {
        Assertions.assertEquals(1, employeePayRollService.deleteARecordromDatabase(47));
    }

    @Test
    void givenMultipleRecordsShouldBeAddedToDatabaseAndRecordTimeWithOutThreading() {
        ArrayList<EmployeePayRollService> employees = new ArrayList<>(Arrays.asList(
                new EmployeePayRollService("Shravan",
                        "2018-02-01", 55000, "Development", 123, 852852933,
                        "TCS", "Ramnaagar", "Tealnagnan", "Hyderabad","India",50047, "Home"
                        ,"M" ,456),
                new EmployeePayRollService("James",
                        "2018-02-01", 51000, "Development", 123, 852852933,
                        "TCS", "Ramnaagar", "Tealnagnan", "Hyderabad","India",50047, "Home"
                        ,"M" ,456),
                new EmployeePayRollService("Mares",
                        "2018-02-01", 57000, "Development", 123, 852852933,
                        "TCS", "Ramnaagar", "Tealnagnan", "Hyderabad","India",50047, "Home"
                        ,"M" ,456)
        ));
        Instant start = Instant.now();
        employeePayRollService.addMultipleEmployees(employees);
        Instant end = Instant.now();
        System.out.println("Duration : "+Duration.between(start,end));
        Assertions.assertEquals(16,employeePayRollDBService.countNoOfEmployees());
    }
}
