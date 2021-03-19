package com.employeepayrolljdbc;

public class EmployeePayRollException extends Exception {
    String message;

    public EmployeePayRollException(String message) {
        super();
        this.message = message;
        System.out.println(message);
    }
}
