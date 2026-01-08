package ca.jrvs.apps.practice.dataStructure.List;

import java.util.Comparator;

public class EmployeeComparators {
    //sort by salary ascending

    public static Comparator<Employee> bySalary =
            (e1, e2) ->  Long.compare(e1.getSalary(), e2.getSalary());

    //sort by name alphabetically

    public static Comparator<Employee> byName =
            (e1, e2) -> e1.getName().compareTo(e2.getName());
}
