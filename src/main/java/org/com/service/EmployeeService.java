package org.com.service;

import org.com.model.Employee;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeService {

    private final Map<Integer, Employee> employeeMap; // For quick lookup by ID
    private final Map<Integer, List<Employee>> subordinatesMap; // Manager ID -> List of direct reports
    private final Set<Integer> ceoIds; // Handle multiple CEOs

    public EmployeeService(List<Employee> employees) {
        // Initialize employeeMap
        this.employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, emp -> emp));

        // Initialize subordinatesMap to check direct report
        this.subordinatesMap = new HashMap<>();
        for (Employee emp : employees) {
            if (emp.getManagerId() != null) {
                subordinatesMap
                        .computeIfAbsent(emp.getManagerId(), k -> new ArrayList<>())
                        .add(emp);
            }
        }

        // Detect all CEOs (employees with null manager)
        this.ceoIds = employees.stream()
                .filter(emp -> emp.getManagerId() == null)
                .map(Employee::getId)
                .collect(Collectors.toSet());
    }

    /**
     * Result object for salary analysis.
     */
    public static class SalaryCheck {
        public final Employee manager;
        public final double avgDirectReportSalary;
        public final double minAllowed;
        public final double maxAllowed;
        public final double diff;
        public final String status; // UNDERPAID, OVERPAID, OK

        public SalaryCheck(Employee manager, double avg, double min, double max,
                           double diff, String status) {
            this.manager = manager;
            this.avgDirectReportSalary = avg;
            this.minAllowed = min;
            this.maxAllowed = max;
            this.diff = diff;
            this.status = status;
        }

        @Override
        public String toString() {
            return String.format("Manager %s %s (ID: %d) salary=%.2f avgDR=%.2f [%s by %.2f]",
                    manager.getFirstName(), manager.getLastName(), manager.getId(),
                    manager.getSalary(), avgDirectReportSalary, status, diff);
        }
    }

    /**
     * Result object for reporting-line analysis.
     */
    public static class ReportingLineCheck {
        public final Employee employee;
        public final int managersBetween;
        public final int overBy;
        public final List<Integer> chain; // employee -> ... -> CEO path

        public ReportingLineCheck(Employee emp, int managersBetween, int overBy, List<Integer> chain) {
            this.employee = emp;
            this.managersBetween = managersBetween;
            this.overBy = overBy;
            this.chain = chain;
        }

        @Override
        public String toString() {
            return String.format("Employee %s %s (ID: %d) has %d managers between CEO (too long by %d) chain=%s",
                    employee.getFirstName(), employee.getLastName(), employee.getId(),
                    managersBetween, overBy, chain);
        }
    }

    private Integer findCeoId() {
        return employeeMap.values().stream()
                .filter(emp -> emp.getManagerId() == null)
                .map(Employee::getId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Analyze managers against salary rules (20%-50% above avg direct reports).
     */
    /**
     * Identifies managers who earn less or more than they should
     * based on subordinate salaries, and prints by how much.
     */
    public void analyzeManagerSalaries() {
        System.out.println("\n--- Manager Salary Analysis ---");
        for (Employee manager : employeeMap.values()) {
            if (subordinatesMap.containsKey(manager.getId())) {
                List<Employee> subordinates = subordinatesMap.get(manager.getId());
                double avg = subordinates.stream()
                        .mapToDouble(Employee::getSalary)
                        .average()
                        .orElse(0.0);

                double minAllowed = avg * 1.20;
                double maxAllowed = avg * 1.50;
                double salary = manager.getSalary();

                if (salary < minAllowed) {
                    double deficit = minAllowed - salary;
                    System.out.printf("%s %s (ID: %d) -> UNDERPAID by %.2f (salary=%.2f, avg reports=%.2f)%n",
                            manager.getFirstName(), manager.getLastName(), manager.getId(),
                            deficit, salary, avg);
                } else if (salary > maxAllowed) {
                    double excess = salary - maxAllowed;
                    System.out.printf("%s %s (ID: %d) -> OVERPAID by %.2f (salary=%.2f, avg reports=%.2f)%n",
                            manager.getFirstName(), manager.getLastName(), manager.getId(),
                            excess, salary, avg);
                }
            }
        }
    }


    /**
     * Analyze employees with > 4 managers between them and any CEO.
     */
    /**
     * Identifies employees with reporting lines longer than 4 managers
     * between them and the CEO, and prints by how much.
     */
    public void analyzeReportingLines() {
        System.out.println("\n--- Long Reporting Line Analysis ---");
        Integer ceoId = findCeoId();
        if (ceoId == null) {
            System.out.println("CEO not found in the data (no employee with null managerId).");
            return;
        }

        for (Employee employee : employeeMap.values()) {
            if (employee.getId() == ceoId) {
                continue; // skip CEO
            }

            int managerCount = 0;
            Integer currentId = employee.getManagerId();

            while (currentId != null && employeeMap.containsKey(currentId)) {
                managerCount++;
                Employee current = employeeMap.get(currentId);
                currentId = current.getManagerId();

                if (current.getManagerId() == null) { // reached CEO
                    break;
                }

                // break in case of loop
                if (managerCount > employeeMap.size()) {
                    System.err.printf("Warning: Possible circular reference for employee %s %s (ID: %d)%n",
                            employee.getFirstName(), employee.getLastName(), employee.getId());
                    break;
                }
            }

            if (managerCount > 4) {
                int excess = managerCount - 4;
                System.out.printf("%s %s (ID: %d) has a reporting line too long by %d manager(s) [total managers=%d]%n",
                        employee.getFirstName(), employee.getLastName(), employee.getId(),
                        excess, managerCount);
            }
        }
    }

}
