package com.korges.vavr;


import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import java.net.URI;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.List;
import static io.vavr.API.Match;
import static io.vavr.API.unchecked;
import static io.vavr.Predicates.instanceOf;

public class VavrApplicationTests {

    @Test
    public void vavr_1() {
        List<Integer> ints = List.of(1, 2, 3);
        List(1, 2, 3);
    }

    @Test
    public void vavr_2() {
        List<Integer> integers = List.of(Option.of(42), Option.of(24))
                .filter(Option::isDefined)
                .map(Option::get);

        System.out.println(integers);
    }

    @Test
    public void vavr_3() {
        List<Integer> integers = List.of(Option.of(42), Option.of(24))
                .flatMap(o -> o);

        System.out.println(integers);
    }

    @Test
    public void vavr_4() {
        String result = Try.of(() -> new URI(""))
                .map(uri -> uri.toString())
                .filter(i -> true)
                .getOrElse("default");

        System.out.println(result);
    }

    @Test
    public void vavr_5() {
        Supplier<Integer> sup = () -> {
            System.out.println("NOT LAZY");
            return 42;
        };

        sup.get();
        sup.get();
        sup.get();
    }

    @Test
    public void vavr_6() {
        Lazy<Integer> lazy = Lazy.of(() -> {
            System.out.println("LAZY");
            return 42;
        });

        lazy.get();
        lazy.get();
        lazy.get();
    }

    @Test
    public void vavr_7() {
        List<Integer> integers = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        Integer a = integers.head();
        List<Integer> b = integers.tail();
        List<Integer> c = integers.drop(1);
        List<Integer> d = integers.dropRight(1);

        System.out.println(integers);
    }

    @Test
    public void vavr_8() {
        List<Integer> integers = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        System.out.println(integers.zipWithIndex());
    }

    @Test
    public void vavr_9() {
        java.util.List<Integer> l = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                .collect(Collectors.toList());
    }

    @Test
    public void vavr_10() {
        Stream<Integer> iterate = Stream.iterate(0, i -> i + 1)
                .take(10);

        System.out.println(iterate.get(9));
    }

    @Test
    public void vavr_11() {
        List.of(1, 2)
                .asJava();
    }

    @Test
    public void vavr_12() {
        Tuple2<String, Integer> tuple = Tuple.of("Java", 11);

        String result = tuple.apply((x, integer) -> x + integer);

        System.out.println(result);
    }

    @Test
    public void vavr_13() {
        List.of("")
                .map(unchecked(s -> new URI(s)));
    }

    @Test
    public void vavr_14() {
        Object a = 42;

        String of = Match(a).of(
                Case($(instanceOf(String.class)), "string"),
                Case($(instanceOf(Integer.class)), "int"));

        System.out.println(of);
    }
}
