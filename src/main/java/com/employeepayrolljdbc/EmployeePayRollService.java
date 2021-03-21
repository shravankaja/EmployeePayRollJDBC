package com.employeepayrolljdbc;

import java.sql.*;
import java.time.*;
import java.util.*;

public class EmployeePayRollService {
    String name;
    LocalDate startDate;
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

    public EmployeePayRollService(String name, double phone, LocalDate startDate, int salary, int id, String gender) {
        this.name = name;
        this.startDate = startDate;
        this.salary = salary;
        this.id = id;
        this.phone = phone;
        this.gender = gender;
        listOfEmployeeObjects = new ArrayList<>();
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
        return employeePayRollDBService.entryOfNewEmployeeDetails("Tavan", 123456,
                "2018-02-01", 55000, "M", "Development", 123, "TCS",
                "RamNagar", "Tealanagan", "Hydderabad", "India", 50074, "Home", 456);
    }
}



