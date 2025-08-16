package org.com;

import org.com.model.Employee;
import org.com.service.EmployeeService;
import org.com.util.EmployeeCsvReader;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        try {
            List<Employee> employees = EmployeeCsvReader.readEmployees("employee.csv");
            System.out.println("Loaded employees: " + employees.size());
            employees.stream().limit(5).forEach(System.out::println);
            if (employees.isEmpty()) {
                System.out.println("No employee data found or parsed from the file. Exiting.");
                return;
            }


            EmployeeService service = new EmployeeService(employees);

            service.analyzeManagerSalaries();
            service.analyzeReportingLines();
            //service.printAllManagerSalaryStatus();

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}