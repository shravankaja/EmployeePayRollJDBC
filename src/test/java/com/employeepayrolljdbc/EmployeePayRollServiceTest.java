package com.employeepayrolljdbc;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.*;
import java.util.*;

public class EmployeePayRollServiceTest {

    EmployeePayRollService employeePayRollService = new EmployeePayRollService();

    @Test
    void whenInitlializedProgramWeShouldBeAbleToLoadSqlDriverClass() {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_services?useSSL=false";
        String userName = "root";
        String password = "Addtexthere25";
        Connection result = EmployeePayRollDBService.getInstance().connectToDatabase();
        Assertions.assertEquals("com.mysql.jdbc.JDBC4Connection@34251ec", result);
    }


    @Test
    void whenJdbcDriverClassIsLoadedWeShouldBeAbleToObtainListOfDrivers() {
        ArrayList<String> listOfDriversTest = new ArrayList<>();
        listOfDriversTest.add("com.mysql.jdbc.Driver");
        listOfDriversTest.add("com.mysql.fabric.jdbc.FabricMySQLDriver");
        Assertions.assertEquals(listOfDriversTest, EmployeePayRollDBService.getInstance().listDrivers());
    }

    @Test
    void givenDataBaseTableWeShouldBeAbleToReadAllTheRecords() {
        ArrayList<EmployeePayRollService> result = employeePayRollService.readData();
        Assertions.assertEquals(3, result.size());
    }

    @Test
    void givenNameAndSalaryWeShouldBeAbleToUpdateRecordAndReturnNumberOfRecordsUpdated() throws EmployeePayRollException {
        Assertions.assertEquals(1, employeePayRollService.updateSalary("Shravan1", 300000));
    }

    @Test
    void givenNewSalaryObjectInDataBaseAndProgramMemoryShouldBeSame() throws EmployeePayRollException {
        ArrayList<EmployeePayRollService> list = employeePayRollService.readData();
        employeePayRollService.updateSalary("Shravan", 1500);
        boolean result = employeePayRollService.checkIfDBIsInSyncWithMemory("Shravan");
        Assertions.assertTrue(result);
    }

    @Test
    void givenDataRangeWeShouldBeAbleToFindAllTheEmployessJoined() throws EmployeePayRollException {
        Assertions.assertEquals(3, employeePayRollService.
                findEmployeesJoinedInDateRange("2018-01-01", "2018-12-12"));
    }

    @Test
    void givenOperationToPerformOnSalaryAccordingToGender() {
        Assertions.assertEquals(49000.0, employeePayRollService.performOperationOnSalaryOfEmployees("avg"));
    }

    @Test
    void givenNewEmployeeRecordWeShouldBeAbleToAddNewRecordToDB() throws EmployeePayRollException {
        Assertions.assertEquals(1, employeePayRollService.
                addNewEmployeeRecordToDB(126, "Marcqus", 47999, "M", "2018-03-02"));
        boolean result = employeePayRollService.checkIfDBIsInSyncWithMemory("Marcqus");
        Assertions.assertTrue(result);
    }
}
