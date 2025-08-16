package org.com.model;

import java.util.Objects;

import lombok.Data;// For equals and hashCode

@Data
public class Employee {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final double salary;
    private final Integer managerId; // Use Integer to allow for null (CEO)

    public Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    // Optional: Override toString for easy printing
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId=" + managerId +
                '}';
    }

    // Important for collections like Maps if you use Employee objects as keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id; // ID is usually sufficient for equality
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
