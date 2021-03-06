package com.example.cardealer.employees.boundary;

import lombok.Data;

@Data
public class CreateEmployeeRequest {
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String address;
    boolean isActive;
    String roles;
}
