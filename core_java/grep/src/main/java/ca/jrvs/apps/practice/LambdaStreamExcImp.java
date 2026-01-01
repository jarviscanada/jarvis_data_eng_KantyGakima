package ca.jrvs.apps.practice;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements  LambdaStreamExc{


    @Override
    public Stream<String> createStrStream(String... strings) {
        return Arrays.stream(strings);
    }

    @Override
    public Stream<String> toUpperCase(String... strings) {
        return createStrStream(strings).map(String::toUpperCase);
    }

    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        return stringStream.filter(s -> !s.contains(pattern));
    }

    @Override
    public IntStream createIntstream(int[] arr) {
        return Arrays.stream(arr);
    }

    @Override
    public <E> List<E> toList(Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }

    @Override
    public List<Integer> toList(IntStream intStream) {
        return intStream.boxed().collect(Collectors.toList());
    }

    @Override
    public IntStream createIntStream(int start, int end) {
        return IntStream.rangeClosed(start, end);
    }

    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return intStream.mapToDouble(Math::sqrt);
    }

    @Override
    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(i -> i %2 != 0);
    }

    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        return msg -> System.out.println(prefix + msg + suffix);
    }

    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {
        Arrays.stream(messages).forEach(printer);
    }

    @Override
    public void printOdd(IntStream intStream, Consumer<String> printer) {
        getOdd(intStream)
                .forEach(i -> printer.accept(String.valueOf(i)));
    }

    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return ints
                .flatMap(Collection::stream)
                .map(i -> i * i);
    }

    public static void main(String[] args) {
        LambdaStreamExcImp lse = new LambdaStreamExcImp();

        //Test toUpperCase
        System.out.println("---toUpperCase---");
        lse.toUpperCase("hello", "world").forEach(System.out::println);

        //Test filter
        System.out.println("---filter:remove strings containing 'a'---");
        lse.filter(lse.createStrStream("cat", "dog", "pan"), "a")
                .forEach(System.out::println);

        //Test createIntStream + toList
        System.out.println("---createIntStream(0,5)---");
        List <Integer> nums = lse.toList(lse.createIntStream(1, 5));
        System.out.println(nums);

        //Test squareRootIntStream
        System.out.println("---sqrt of 1 to 5--- ");
        lse.squareRootIntStream(lse.createIntStream(1, 5))
                .forEach(System.out::println);

        //Test getOdd
        System.out.println("---get odd numbers from 1 to 10");
        lse.getOdd(lse.createIntStream(1, 10))
                .forEach(System.out::println);

        //Test getLambdaPrinter + printMessages
        System.out.println("--- print messages---");
        String[] messages = {"a", "b", "c"};
        lse.printMessages(messages, lse.getLambdaPrinter("msg", "!"));

        //Test PrintOdd
        System.out.println("---print odd numbers from 1 to 10--- ");
        lse.printOdd(lse.createIntStream(1, 10), lse.getLambdaPrinter("odd number:", "!") );

        //Test flatNestedInt
        System.out.println("---flatNestedInt--- ");
        List<Integer> list1 = Arrays.asList(0,1);
        List<Integer> list2 = Arrays.asList(2,3);
        lse.flatNestedInt(Stream.of(list1, list2))
                .forEach(System.out::println);

    }
}
