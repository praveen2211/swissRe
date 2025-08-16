src/
 └── main/
     ├── java/
     │   └── org/com/
     │       ├── Main.java
     │       ├── model/
     │       │   └── Employee.java
     │       ├── service/
     │       │   └── EmployeeService.java
     │       └── util/
     │           └── EmployeeCsvReader.java
     └── resources/
         └── employee.csv   # Input file
How It Works
1. org.com.util.EmployeeCsvReader

Reads employee.csv from src/main/resources.

Parses each row into an Employee object.

Handles null manager IDs.

2. org.com.service.EmployeeService

Core service for analysis.

Uses two maps internally for fast lookups:

employeeMap → employee ID → employee object

subordinatesMap → manager ID → list of direct reports

analyzeManagerSalaries()

For each manager, computes the average salary of direct reports.

Defines acceptable salary band (20–50% above average).

Prints whether the manager is:

UNDERPAID by X

OVERPAID by Y

Nothing if within band.

analyzeReportingLines()

Finds the CEO (the employee with managerId = null).

Traverses up each employee’s chain of managers until reaching the CEO.

If there are more than 4 managers in between → flagged.

Reports exactly how many extra managers beyond the limit.

Detects possible circular references and warns.

3. org.com.Main

Entry point.

Loads employees from CSV, builds EmployeeService, and runs analyses.

Run with:
mvn clean package
java -cp target/employee-analyzer-1.0-SNAPSHOT.jar org.com.Main src/main/resources/employee.csv

Sample Output
Loaded employees: 25

--- Manager Salary Analysis ---
David White (ID: 5) -> UNDERPAID by 24200.00 (salary=25000.00, avg reports=41000.00)
Grace Hall (ID: 8) -> OVERPAID by 56500.00 (salary=100000.00, avg reports=29000.00)

--- Long Reporting Line Analysis ---
Nina Anderson (ID: 15) has a reporting line too long by 2 manager(s) [total managers=6]
Oscar Thomas (ID: 16) has a reporting line too long by 3 manager(s) [total managers=7]


ests

A JUnit test suite covers:

CEO detection (findCeoId)

Manager salary analysis (UNDERPAID/OVERPAID detection)

Reporting line depth calculation

Safety check for circular references

🚀 Next Steps

Export analysis results to a CSV/Excel report (future enhancement).

Add integration tests with large datasets (1000+ employees).

Wrap into a REST API (Spring Boot) for interactive use.
