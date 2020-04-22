package com.korges.vavr;


import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.vavr.API.List;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

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


}
