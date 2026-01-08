package ca.jrvs.apps.practice.dataStructure.List;

import java.util.LinkedList;
import java.util.ListIterator;

public class LinkedListAPIs {

    public static void main(String[] args) {

        LinkedList<String> names = new LinkedList<>();

        // Add elements
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");

        // Add to front and end
        names.addFirst("Start");
        names.addLast("End");

        // Insert at index
        names.add(2, "Inserted");

        System.out.println("List: " + names);

        // Access elements
        System.out.println("First: " + names.getFirst());
        System.out.println("Last: " + names.getLast());
        System.out.println("Index 2: " + names.get(2));

        // Remove
        names.removeFirst();
        names.removeLast();
        names.remove("Bob");

        System.out.println("After removals: " + names);

        // Check if exists
        System.out.println("Contains Charlie? " + names.contains("Charlie"));

        // ----- Iterating -----

        System.out.println("\nFor-each loop:");
        for (String n : names) System.out.println(n);

        System.out.println("\nIterator:");
        ListIterator<String> it = names.listIterator();
        while (it.hasNext()) System.out.println(it.next());

        System.out.println("\nIndex loop:");
        for (int i = 0; i < names.size(); i++) System.out.println(names.get(i));
    }
}
;