package ca.jrvs.apps.practice.dataStructure.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EmployeeSort {

    public static void main(String[] args) {

        List<Employee> employees = new ArrayList<>(Arrays.asList(
                new Employee(1, "Alice", 30, 70000),
                new Employee(2, "Bob", 25, 65000),
                new Employee(3, "Charlie", 35, 90000)
        ));

        System.out.println("Original:");
        employees.forEach(e -> System.out.println(e.getName() + " - " + e.getAge() + " - " + e.getSalary()));

        // ----- Using Comparable (age) -----
        Collections.sort(employees);
        System.out.println("\nSorted by age (Comparable):");
        employees.forEach(e -> System.out.println(e.getName() + " - " + e.getAge()));

        // ----- Using Comparator (salary) -----
        employees.sort(EmployeeComparators.bySalary);
        System.out.println("\nSorted by salary (Comparator):");
        employees.forEach(e -> System.out.println(e.getName() + " - " + e.getSalary()));
    }
}
