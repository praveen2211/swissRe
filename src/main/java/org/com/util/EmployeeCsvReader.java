package org.com.util;


import org.com.model.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.List;

public class EmployeeCsvReader {
    public static List<Employee> readEmployees(String resourceFileName) throws IOException {
        List<Employee> employees = new ArrayList<>();

        InputStream inputStream = EmployeeCsvReader.class.getClassLoader().getResourceAsStream(resourceFileName);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourceFileName);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.toLowerCase().contains("id") && line.toLowerCase().contains("firstname")) {
                        continue;
                    }
                }

                String[] parts = line.split(",", -1);
                if (parts.length != 5) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                int id = Integer.parseInt(parts[0].trim());
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                double salary = Double.parseDouble(parts[3].trim());
                Integer managerId = parts[4].trim().isEmpty() ? null : Integer.parseInt(parts[4].trim());

                employees.add(new Employee(id, firstName, lastName, salary, managerId));

            }
        }
        return employees;
    }


}
