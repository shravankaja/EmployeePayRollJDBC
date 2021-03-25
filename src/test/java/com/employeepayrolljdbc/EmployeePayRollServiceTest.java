package com.employeepayrolljdbc;

import com.google.gson.*;
import io.restassured.*;
import io.restassured.response.Response;
import io.restassured.specification.*;
import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.*;

import java.awt.*;
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
    void givenMultipleRecordsShouldBeAddedToDatabaseAndRecordTimeWithOutThreadingAndWithThreading() {
        ArrayList<EmployeePayRollService> employees = new ArrayList<>(Arrays.asList(
                new EmployeePayRollService("Shravan",
                        "2018-02-01", 55000, "Development", 123, 852852933,
                        "TCS", "Ramnaagar", "Tealnagnan", "Hyderabad", "India", 50047, "Home"
                        , "M", 456),
                new EmployeePayRollService("James",
                        "2018-02-01", 51000, "Development", 123, 852852933,
                        "TCS", "Ramnaagar", "Tealnagnan", "Hyderabad", "India", 50047,
                        "Home", "M", 456),
                new EmployeePayRollService("Mares",
                        "2018-02-01", 57000, "Development", 123, 852852933,
                        "TCS", "Ramnaagar", "Tealnagnan", "Hyderabad", "India", 50047, "Home"
                        , "M", 456)
        ));
        Instant start = Instant.now();
        employeePayRollService.addMultipleEmployees(employees);
        Instant end = Instant.now();
        System.out.println("Duration : " + Duration.between(start, end));
        Instant startWithThread = Instant.now();
        employeePayRollService.addMultipleEmployeesWithThreads(employees);
        Instant endWithThread = Instant.now();
        System.out.println("Duration with thread: " + Duration.between(startWithThread, endWithThread));
        Assertions.assertEquals(7, employeePayRollDBService.countNoOfEmployees());
    }

    @Test
    void givenDataOfMultipleEmployeesToUpdate() {
        ArrayList<EmployeePayRollService> employees = new ArrayList<>(Arrays.asList(
                new EmployeePayRollService(130, "Shrvan", "employee_details", "name"),
                new EmployeePayRollService(134, "Shrvan", "employee_details", "name")));
        Instant start = Instant.now();
        employeePayRollService.updaateMultipleEmployees(employees);
        Instant end = Instant.now();
        System.out.println("Duration : " + Duration.between(start, end));
        Instant startWithThread = Instant.now();
        Assertions.assertEquals(2, employeePayRollService.updateultipleEmployeesWithThreads(employees));
        Instant endWithThread = Instant.now();
        System.out.println("Duration with thread: " + Duration.between(startWithThread, endWithThread));
        employeePayRollDBService.getEmployeeObject(130);
    }

    @Test
    void givenDataInJasonServerWhenRetrievedShouldMatchTheCount() {
        EmployeePayRollService[] arrayOfEmps = getEmployeeList();
        EmployeePayRollService employeePayRollService;
        employeePayRollService = new EmployeePayRollService(Arrays.asList(arrayOfEmps));
        int count = employeePayRollService.countEntriesFromJson();
        Assertions.assertEquals(2, count);

    }

    private EmployeePayRollService[] getEmployeeList() {
        Response response = RestAssured.get("http://localhost:3000/employees");
        System.out.println("Employee payroll entries in JsonServer: " + response.asString());
        EmployeePayRollService[] arrayOfEmps = new Gson().fromJson(response.asString(), EmployeePayRollService[].class);
        return arrayOfEmps;
    }

    @Test
    void whenAddedNewRecordToJasonServerShouldMatchNumberOfEntries() {
        EmployeePayRollService[] arrayOfEmps = getEmployeeList();
        EmployeePayRollService employeePayRollService;
        employeePayRollService = new EmployeePayRollService(Arrays.asList(arrayOfEmps));
        ArrayList<EmployeePayRollService> employees = new ArrayList<>(Arrays.asList(
                new EmployeePayRollService(45, "Shravan",
                        "2018-02-01", 55000, "Development", 123, 852852933,
                        "TCS", "Ramnaagar", "Tealnagnan", "Hyderabad", "India", 50047, "Home"
                        , "M", 456),
                new EmployeePayRollService(54, "James",
                        "2018-02-01", 51000, "Development", 123, 852852933,
                        "TCS", "Ramnaagar", "Tealnagnan", "Hyderabad", "India", 50047,
                        "Home", "M", 456),
                new EmployeePayRollService(96, "Mares",
                        "2018-02-01", 57000, "Development", 123, 852852933,
                        "TCS", "Ramnaagar", "Tealnagnan", "Hyderabad", "India", 50047, "Home"
                        , "M", 456)
        ));
        for (EmployeePayRollService employeePayRollServicelist : employees) {
            employeePayRollService.addEmployeeToList(employeePayRollServicelist);
        }
        addEmployeeToPayRollData(employees);
        int count = employeePayRollService.countEntriesFromJson();
        Assertions.assertEquals(6, count);
    }

    private void addEmployeeToPayRollData(ArrayList<EmployeePayRollService> employeePayRollServiceObject) {
        for (EmployeePayRollService employeePayRollService : employeePayRollServiceObject) {
            String empJson = new Gson().toJson(employeePayRollService);
            RequestSpecification request = RestAssured.given();
            request.header("Content-Type", "application/json");
            request.body(empJson);
            request.post("http://localhost:3000/employees");
        }
    }
}
