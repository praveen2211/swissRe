# Employee Analyzer

A Java-based tool to analyze employee hierarchies and salaries from a CSV file.  

It identifies:
- Managers who are **underpaid** or **overpaid** relative to their direct reports.
- Employees whose reporting lines are **too long** (more than 4 managers between them and the CEO).

---

## 📂 Project Structure

```
src/
 └── main/
     ├── java/
     │   └── org/com/
     │       ├── Main.java                # Entry point
     │       ├── model/
     │       │   └── Employee.java        # Employee entity
     │       ├── service/
     │       │   └── EmployeeService.java # Business logic
     │       └── util/
     │           └── EmployeeCsvReader.java # CSV reader utility
     └── resources/
         └── employee.csv                 # Input file
```

---

## ⚙️ How It Works

### 1. `EmployeeCsvReader`
- Reads `employee.csv` from `src/main/resources`.
- Parses each row into an `Employee` object.
- Handles null `managerId`.

### 2. `EmployeeService`
- Maintains two maps for fast lookups:
  - `employeeMap`: ID → Employee
  - `subordinatesMap`: Manager ID → Direct reports
- **`analyzeManagerSalaries()`**
  - For each manager, computes average salary of subordinates.
  - Defines valid range: **20–50% above average**.
  - Prints `UNDERPAID`, `OVERPAID`, or within band.
- **`analyzeReportingLines()`**
  - Detects the CEO (managerId = null).
  - Walks up each employee’s reporting chain.
  - Flags employees with more than 4 managers between them and the CEO.
  - Warns on circular references.

### 3. `Main`
- Loads employees from CSV.
- Builds `EmployeeService`.
- Runs salary and hierarchy analyses.

---

## 🚀 Running the Project

Compile and run:

```bash
mvn clean package
java -cp target/employee-analyzer-1.0-SNAPSHOT.jar org.com.Main src/main/resources/employee.csv
```

---

## 📊 Sample Output

```
Loaded employees: 25

--- Manager Salary Analysis ---
David White (ID: 5) -> UNDERPAID by 24200.00 (salary=25000.00, avg reports=41000.00)
Grace Hall (ID: 8) -> OVERPAID by 56500.00 (salary=100000.00, avg reports=29000.00)

--- Long Reporting Line Analysis ---
Nina Anderson (ID: 15) has a reporting line too long by 2 manager(s) [total managers=6]
Oscar Thomas (ID: 16) has a reporting line too long by 3 manager(s) [total managers=7]
```

---

## ✅ Tests

JUnit tests cover:
- CEO detection (`findCeoId`)
- Salary analysis (UNDERPAID/OVERPAID detection)
- Reporting line depth calculation
- Circular reference safety

---

## 🔮 Next Steps

- Export results to CSV/Excel report  
- Add integration tests with 1000+ records  
- Wrap into a REST API (Spring Boot) for interactive use  
