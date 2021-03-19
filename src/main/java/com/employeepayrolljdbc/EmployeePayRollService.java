package com.employeepayrolljdbc;

import javax.print.attribute.*;
import java.sql.*;
import java.time.*;
import java.util.*;

public class EmployeePayRollService {

    String name;
    LocalDate startDate;
    int salary;
    int id;
    ArrayList<EmployeePayRollService> listOfEmployeeObjects;

    public EmployeePayRollService() {

    }

    public EmployeePayRollService(String name, LocalDate startDate, int salary, int id) {
        this.name = name;
        this.startDate = startDate;
        this.salary = salary;
        this.id = id;
        listOfEmployeeObjects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<EmployeePayRollService> readData() {
        this.listOfEmployeeObjects = EmployeePayRollDBService.getInstance().readData();
        return this.listOfEmployeeObjects;
    }

    public int updateSalary(String name, int salary) throws EmployeePayRollException {
        int a = EmployeePayRollDBService.getInstance().updateSalary(name, salary);
        if (a == 0) throw new EmployeePayRollException("No records with given name");
        EmployeePayRollService employeePayRollService = this.getEmployeeObject(name);
        if (employeePayRollService != null) employeePayRollService.salary = salary;
        return a;
    }

    public EmployeePayRollService getEmployeeObject(String name) {
        return this.listOfEmployeeObjects.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean checkIfDBIsInSyncWithMemory(String name) throws EmployeePayRollException {
        ArrayList<EmployeePayRollService> list = EmployeePayRollDBService.getInstance().getDataFromEmployeePayRollTable(name);
        if (list == null) throw new EmployeePayRollException("No record with given name");
        return list.get(0).equals(this.getEmployeeObject(name));
    }

    public int findEmployeesJoinedInDateRange(String dateStart, String dateEnd) throws EmployeePayRollException {
        ArrayList<EmployeePayRollService> list = EmployeePayRollDBService.getInstance()
                .findEmployesJoinesForDateRange(dateStart, dateEnd);
        if (list == null) throw new EmployeePayRollException("No records");
        return list.size();
    }

    public int performOperationOnSalaryOfEmployees(String operation) {
        HashMap<String, Integer> resultMap = EmployeePayRollDBService.getInstance().performOperationsOnSalaryOf(operation);
        int result = resultMap.get("F");
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayRollService that = (EmployeePayRollService) o;
        return id == that.id && Integer.compare(that.salary, salary) == 0 && name.equals(that.name);
    }
}



