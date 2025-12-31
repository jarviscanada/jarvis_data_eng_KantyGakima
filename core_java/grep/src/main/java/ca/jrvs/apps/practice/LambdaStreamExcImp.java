package ca.jrvs.apps.practice;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements  LambdaStreamExc{


    @Override
    public Stream<String> createStrStream(String... strings) {
        return Stream.empty();
    }

    @Override
    public Stream<String> toUpperCase(String... strings) {
        return Stream.empty();
    }

    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        return Stream.empty();
    }

    @Override
    public IntStream createIntstream(int[] arr) {
        return IntStream.empty();
    }

    @Override
    public <E> List<E> toList(Stream<E> stream) {
        return List.of();
    }

    @Override
    public List<Integer> toList(IntStream intStream) {
        return List.of();
    }

    @Override
    public IntStream createIntStream(int start, int end) {
        return IntStream.empty();
    }

    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return DoubleStream.empty();
    }

    @Override
    public IntStream getOdd(IntStream intStream) {
        return IntStream.empty();
    }

    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        return null;
    }

    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {

    }

    @Override
    public void printOdd(IntStream intStream, Consumer<String> printer) {

    }

    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return Stream.empty();
    }
}
