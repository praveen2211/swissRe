package org.com.service;

import org.com.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


public class EmployeeServiceTest {

    private EmployeeService service;

    @BeforeEach
    void setUp() {
        List<Employee> employees = Arrays.asList(
                new Employee(1, "John", "CEO", 200000, null), // CEO
                new Employee(2, "Alice", "Smith", 60000, 1),  // Manager
                new Employee(3, "Bob", "Jones", 30000, 2),   // Report
                new Employee(4, "Carol", "Brown", 32000, 2), // Report
                new Employee(5, "David", "White", 25000, 1), // Underpaid manager
                new Employee(6, "Eva", "Black", 40000, 5),   // Report
                new Employee(7, "Frank", "Gray", 42000, 5)   // Report
        );
        service = new EmployeeService(employees);
    }


    @Test
    void testManagerSalaryAnalysis() {
        // Alice should be OVERPAID (60k vs avg reports ~31k)
        // David should be UNDERPAID (25k vs avg reports ~41k)
        service.analyzeManagerSalaries();
        // No assertion here, but you can capture System.out with a library if needed
    }

    @Test
    void testReportingLineAnalysis() {
        service.analyzeReportingLines();
        // Again, capture System.out for validation if required
    }
}
