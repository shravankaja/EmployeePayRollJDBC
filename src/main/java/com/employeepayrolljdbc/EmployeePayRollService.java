package com.employeepayrolljdbc;

import java.sql.*;
import java.time.*;
import java.util.*;

public class EmployeePayRollService {
    private List<EmployeePayRollService> empArray;
    String value;
    String columnToBeUpdated;
    String tableName;
    String name;
    String startDate;
    int salary;
    int id;
    String departmentName;
    int departmentID;
    double phone;
    String companyName;
    String street_name;
    String state;
    String city;
    String country;
    int zip;
    String addressType;
    String gender;
    int houseNo;

    ArrayList<ArrayList<HashMap<Integer, List<EmployeePayRollService>>>> nestedList = new ArrayList<>();
    ArrayList<EmployeePayRollService> listOfEmployeeObjects;
    ArrayList<EmployeePayRollService> listOfEmployeeAddress;
    ArrayList<EmployeePayRollService> listOfCompany;
    ArrayList<EmployeePayRollService> listOfEmployeeDepartments;
    ArrayList<EmployeePayRollService> listOfDepartments;

    public EmployeePayRollService() {

    }

    EmployeePayRollDBService employeePayRollDBService = new EmployeePayRollDBService();

    public EmployeePayRollService(String street_name, String state, String city, String country, int zip, int id, String
            addressType, int houseNo) {
        this.street_name = street_name;
        this.state = state;
        this.city = city;
        this.country = country;
        this.zip = zip;
        this.id = id;
        this.addressType = addressType;
        this.houseNo = houseNo;
        listOfEmployeeAddress = new ArrayList<>();
    }

    public EmployeePayRollService(int id, String companyName) {
        this.id = id;
        this.companyName = companyName;
        listOfCompany = new ArrayList<>();
    }

    public EmployeePayRollService(String departmentName, int departmentID) {
        this.departmentName = departmentName;
        this.departmentID = departmentID;
        listOfDepartments = new ArrayList<>();
    }

    public EmployeePayRollService(int id, int departmentID) {
        this.id = id;
        this.departmentID = departmentID;
        listOfEmployeeDepartments = new ArrayList<>();
    }

    public EmployeePayRollService(String name, double phone, String startDate, int salary, int id, String gender) {
        this.name = name;
        this.startDate = startDate;
        this.salary = salary;
        this.id = id;
        this.phone = phone;
        this.gender = gender;
        listOfEmployeeObjects = new ArrayList<>();
    }

    public EmployeePayRollService(String name, String startDate, int salary, String departmentName, int departmentID,
                                  double phone, String companyName, String street_name, String state, String city,
                                  String country, int zip, String addressType, String gender, int houseNo) {
        this.name = name;
        this.startDate = startDate;
        this.salary = salary;
        this.departmentName = departmentName;
        this.departmentID = departmentID;
        this.phone = phone;
        this.companyName = companyName;
        this.street_name = street_name;
        this.state = state;
        this.city = city;
        this.country = country;
        this.zip = zip;
        this.addressType = addressType;
        this.gender = gender;
        this.houseNo = houseNo;
    }

    public EmployeePayRollService(int id, String name, String startDate, int salary, String departmentName, int departmentID,
                                  double phone, String companyName, String street_name, String state, String city,
                                  String country, int zip, String addressType, String gender, int houseNo) {


        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.salary = salary;
        this.departmentName = departmentName;
        this.departmentID = departmentID;
        this.phone = phone;
        this.companyName = companyName;
        this.street_name = street_name;
        this.state = state;
        this.city = city;
        this.country = country;
        this.zip = zip;
        this.addressType = addressType;
        this.gender = gender;
        this.houseNo = houseNo;
    }


    public EmployeePayRollService(List<EmployeePayRollService> empArray) {
        this();
        this.empArray = new ArrayList<>(empArray);

    }

    public int readData() {
        ArrayList<HashMap<Integer, List<EmployeePayRollService>>> nestedList = new ArrayList<>();
        nestedList = employeePayRollDBService.readData();
        return employeePayRollDBService.noOfEmployees;
    }

    public int writeData(String name, double phone, String startDate, int salary, String gender,
                         String departmentName, int departmentID, String companyName,
                         String street_name, String state, String city, String country, int zip, String
                                 addressType, int houseNo) throws SQLException, EmployeePayRollException {
        return employeePayRollDBService.entryOfNewEmployeeDetails(name, phone,
                startDate, salary, gender, departmentName, departmentID, companyName,
                street_name, state, city, country, zip, addressType, houseNo);
    }

    public int upateRecord(int employeeID, String value, String tableName, String columnToBeUpdated) throws EmployeePayRollException {
        int result = EmployeePayRollDBService.getInstance()
                .updateEmployeeDatabase(employeeID, value, tableName, columnToBeUpdated);
        if (result == 0) throw new EmployeePayRollException("No records");
        return result;
    }

    public int returnNumberOfEmployeesJoinedInSpeciicDateRange(String startDate) throws EmployeePayRollException {
        int result = employeePayRollDBService.findEmployeesJoinedInSpecificDateRange(startDate);
        if (result == 0) throw new EmployeePayRollException("No records");
        return result;
    }

    public EmployeePayRollService(int id, String value, String tableName, String columnToBeUpdated) {
        this.id = id;
        this.value = value;
        this.tableName = tableName;
        this.columnToBeUpdated = columnToBeUpdated;
    }

    public HashMap<String, Integer> returnResultOfOperationPerformedOnSalaryBasedOnGender(String operaation) {
        HashMap<String, Integer> list = employeePayRollDBService.performOperationsOnSalaryOf(operaation);
        return list;
    }

    public int deleteARecordromDatabase(int employee_id) {
        return employeePayRollDBService.deleteRecordOnCascade(employee_id);
    }

    public void addMultipleEmployees(ArrayList<EmployeePayRollService> employees) {
        employees.stream().forEach(employeePayRollService -> {
            try {
                this.writeData(employeePayRollService.name, employeePayRollService.phone,
                        employeePayRollService.startDate, employeePayRollService.salary, employeePayRollService.gender,
                        employeePayRollService.departmentName, employeePayRollService.departmentID, employeePayRollService.companyName,
                        employeePayRollService.street_name, employeePayRollService.state, employeePayRollService.city, employeePayRollService.country,
                        employeePayRollService.zip, employeePayRollService.addressType, employeePayRollService.houseNo);
            } catch (SQLException exception) {
                exception.printStackTrace();
            } catch (EmployeePayRollException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gender, salary, startDate);
    }

    public void addMultipleEmployeesWithThreads(ArrayList<EmployeePayRollService> employees) {
        Map<Integer, Boolean> employeeAdditionStatus = new HashMap<>();
        employees.stream().forEach(employeePayRollService -> {
            Runnable task = () -> {
                employeeAdditionStatus.put(employeePayRollService.hashCode(), false);
                System.out.println("Employee Beign Added: " + Thread.currentThread().getName());
                try {
                    this.writeData(employeePayRollService.name, employeePayRollService.phone,
                            employeePayRollService.startDate, employeePayRollService.salary, employeePayRollService.gender,
                            employeePayRollService.departmentName, employeePayRollService.departmentID, employeePayRollService.companyName,
                            employeePayRollService.street_name, employeePayRollService.state, employeePayRollService.city, employeePayRollService.country,
                            employeePayRollService.zip, employeePayRollService.addressType, employeePayRollService.houseNo);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                } catch (EmployeePayRollException e) {
                    e.printStackTrace();
                }
                employeeAdditionStatus.put(employeePayRollService.hashCode(), true);
                System.out.println("After addition :" + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeePayRollService.name);
            thread.start();
        });
        while (employeeAdditionStatus.containsValue(false)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updaateMultipleEmployees(ArrayList<EmployeePayRollService> employees) {
        employees.stream().forEach(employeePayRollService -> {
            try {
                this.upateRecord(
                        employeePayRollService.id, employeePayRollService.value, employeePayRollService.tableName,
                        employeePayRollService.columnToBeUpdated);
            } catch (EmployeePayRollException e) {
                e.printStackTrace();
            }
        });


    }

    public int updateultipleEmployeesWithThreads(ArrayList<EmployeePayRollService> employees) {
        int numberOfEmployess = 0;
        Map<Integer, Boolean> employeeAdditionStatus = new HashMap<>();
        employees.stream().forEach(employeePayRollService -> {
            Runnable task = () -> {
                employeeAdditionStatus.put(employeePayRollService.hashCode(), false);
                try {
                    System.out.println("Thread before update : " + Thread.currentThread().getName());
                    this.upateRecord(
                            employeePayRollService.id, employeePayRollService.value, employeePayRollService.tableName,
                            employeePayRollService.columnToBeUpdated);
                } catch (EmployeePayRollException e) {
                    e.printStackTrace();
                }
                employeeAdditionStatus.put(employeePayRollService.hashCode(), true);
                System.out.println("After Update :" + Thread.currentThread().getName());

            };
            Thread thread = new Thread(task, employeePayRollService.tableName);
            thread.start();

        });
        while (employeeAdditionStatus.containsValue(false)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return employees.size();
    }

    public int countEntriesFromJson() {
        return empArray.size();
    }

    public void addEmployeeToList(EmployeePayRollService employeePayRollService) {
        this.empArray.add(employeePayRollService);
    }
}



