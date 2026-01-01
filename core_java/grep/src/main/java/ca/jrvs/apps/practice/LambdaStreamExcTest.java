package ca.jrvs.apps.practice;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class LambdaStreamExcTest {

    private final LambdaStreamExcImp lse = new LambdaStreamExcImp();

    @Test
    public void testCreateStrStream(){
        Stream<String> stream = lse.createStrStream("a", "b", "c");
        List<String> result = stream.toList();
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testToUpperCase() {
        Stream<String> stream = lse.toUpperCase("hello", "world");
        List<String> result = stream.toList();
        assertEquals(Arrays.asList("HELLO", "WORLD"), result);
    }

    @Test
    public void testFilter() {
        Stream<String> input = lse.createStrStream("string", "int", "long", "double");
        Stream<String> filtered = lse.filter(input, "n");
        List<String> result = filtered.toList();
        assertEquals(Collections.singletonList("double"), result);
    }

    @Test
    public void testCreateIntStreamArray() {
        int[] arr = {1, 2, 3};
        IntStream intStream = lse.createIntstream(arr);
        List<Integer> result = intStream.boxed().toList();
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testToListIntStream(){
        int[] arr = {5, 6, 7};
        IntStream intStream = lse.createIntstream(arr);
        List<Integer> result = lse.toList(intStream);
        assertEquals(Arrays.asList(5, 6, 7), result);
    }

    @Test
    public void  testCreateIntStreamRange(){
        IntStream intStream = lse.createIntStream(1, 5);
        List<Integer> result = intStream.boxed().toList();
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    public void testSquareRootIntStream() {
        IntStream intStream = lse.createIntStream(1, 5);
        double [] result = lse.squareRootIntStream(intStream).toArray();
        double [] expectedResult = {Math.sqrt(1),Math.sqrt(2), Math.sqrt(3), Math.sqrt(4), Math.sqrt(5)};
        assertArrayEquals(expectedResult, result, 1e-6);
    }

    @Test
    public  void testGetOdd(){
        IntStream intStream = lse.createIntStream(1, 5);
        List<Integer> result = lse.getOdd(intStream).boxed().toList();
        assertEquals(Arrays.asList(1, 3, 5), result);
    }

    @Test
    public void testGetLambdaPrinter(){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(outContent));
            Consumer<String> printer = lse.getLambdaPrinter("msg:", "!");
            printer.accept("hello");

            String output = outContent.toString().trim();
            assertEquals("msg:hello!", output);
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testPrintMessages() {
        String[] messages = {"a", "b", "c"};
        List<String> collected = new ArrayList<>();

        Consumer<String> collector = collected::add;
        lse.printMessages(messages, collector);

        assertEquals(Arrays.asList("a", "b", "c"), collected);
    }

    @Test
    public void testPrintOdd() {
        IntStream intStream = lse.createIntStream(1, 10);
        List<String> collected = new ArrayList<>();

        Consumer<String> collector = collected::add;
        lse.printOdd(intStream, collector);

        // Assuming getOdd returns 1,3,5,7,9 (after you fix it)
        assertEquals(Arrays.asList("1", "3", "5", "7", "9"), collected);
    }

    @Test
    public void testFlatNestedInt() {
        List<Integer> list1 = Arrays.asList(0, 1);
        List<Integer> list2 = Arrays.asList(2, 3);

        Stream<List<Integer>> nested = Stream.of(list1, list2);
        List<Integer> result = lse.flatNestedInt(nested).toList();

        // squares: 0,1,4,9
        assertEquals(Arrays.asList(0, 1, 4, 9), result);
    }
}

