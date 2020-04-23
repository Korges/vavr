package com.korges.vavr;

import com.korges.vavr.dto.Person;
import com.korges.vavr.validator.PersonValidator;
import io.vavr.Function0;
import io.vavr.Function2;
import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Introduction to Vavr
 * https://www.baeldung.com/vavr
 */
public class VavrBaeldung {

    // Vavr - OPTION

    /**
     * The main goal of Option is to eliminate null checks in our code by leveraging the Java type system.
     * Option is an object container in Vavr with a similar end goal like Optional in Java 8.
     * Vavr's Option implements Serializable, Iterable, and has a richer API.
     * Since any object reference in Java can have a null value, we usually have to check for nullity
     * with if statements before using it. These checks make the code robust and stable.
     */
    @Test
    public void givenValue_whenNullCheckNeeded_thenCorrect() {
        Object possibleNullObject = null;
        if (possibleNullObject == null) {
            possibleNullObject = "defaultValue";
        }
        assertNotNull(possibleNullObject);
    }

    /**
     * Without checks, the application can crash due to a simple NPE.
     * However, the checks make the code verbose and not so readable,
     * especially when the if statements end up being nested multiple times.
     */
    @Test(expected = NullPointerException.class)
    public void givenValue_whenNullCheckNeeded_thenCorrect2() {
        Object possibleNullObject = null;
        possibleNullObject.toString();
    }

    /**
     * Option solves this problem by totally eliminating nulls and replacing them with a valid object reference
     * for each possible scenario.
     * With Option a null value will evaluate to an instance of None,
     * while a non-null value will evaluate to an instance of Some.
     */
    @Test
    public void givenValue_whenCreatesOption_thenCorrect() {
        Option<Object> noneOption = Option.of(null);
        Option<Object> someOption = Option.of("value");

        assertEquals("None", noneOption.toString());
        assertEquals("Some(value)", someOption.toString());
    }

    /**
     * Therefore, instead of using object values directly,
     * it's advisable to wrap them inside an Option instance as shown above.
     * Notice, that we did not have to do a check before calling toString
     * yet we did not have to deal with a NullPointerException as we had done before.
     * Option's toString returns us meaningful values in each call.
     * In the second snippet of this section, we needed a null check,
     * in which we would assign a default value to the variable, before attempting to use it.
     * Option can deal with this in a single line, even if there is a null:
     */
    @Test
    public void givenValue_whenCreatesOption_thenCorrect2() {
        String name = null;
        Option<String> nameOption = Option.of(name);

        assertEquals("else", nameOption.getOrElse("else"));
    }

    /**
     * Or a non-null:
     */
    @Test
    public void givenValue_whenCreatesOption_thenCorrect3() {
        String name = "notNull";
        Option<String> nameOption = Option.of(name);

        assertEquals("notNull", nameOption.getOrElse("else"));
    }


    // Vavr - Tuple
    // Tuples are immutable and can hold multiple objects of different types in a type-safe manner.

    /**
     * Vavr brings tuples to Java 8.
     * Tuples are of type Tuple1, Tuple2 to Tuple8 depending on the number of elements they are to take.
     * There is currently an upper limit of eight elements.
     * We access elements of a tuple like tuple._n where n is similar to the notion of an index in arrays:
     */
    @Test
    public void whenCreatesTuple_thenCorrect1() {
        Tuple2<String, Integer> tuple = Tuple.of("Java", 8);
        String elementFirst = tuple._1;
        int elementSecond = tuple._2;

        assertEquals("Java", elementFirst);
        assertEquals(8, elementSecond);
    }


    // Vavr - Try
    // Try is a container for a computation which may result in an exception.

    /**
     * Try wraps a computation so that we don't have to explicitly take care of exceptions with try-catch blocks.
     *
     * Take the following code for example:
     */
    @Test(expected = ArithmeticException.class)
    public void givenBadCode_whenThrowsException_thenCorrect() {
        int i = 1 / 0;
    }

    /**
     * Without try-catch blocks, the application would crash.
     * In order to avoid this, you would need to wrap the statement in a try-catch block. 
     * With Vavr, we can wrap the same code in a Try instance and get a result:
     */
    @Test
    public void givenBadCode_whenTryHandles_thenCorrect() {
        Try<Integer> result = Try.of(() -> 1 / 0);

        assertTrue(result.isFailure());
    }

    /**
     * We can also choose to return a default value:
     */
    @Test
    public void givenBadCode_whenTryHandles_thenCorrect2() {
        Try<Integer> computation = Try.of(() -> 1 / 0);
        int result = computation.getOrElse(() -> -1);

        assertEquals(-1, result);
    }

    /**
     * Or even to explicitly throw an exception of our choice:
     */
    @Test(expected = ArithmeticException.class)
    public void givenBadCode_whenTryHandles_thenCorrect3() {
        String result = Try.of(() -> 1 / 0)
                .map(Object::toString)
                .getOrElseThrow(x -> new ArithmeticException());
    }


    // Vavr - Functional Interfaces
    // Java 8 provides only two basic functions. One takes only a single parameter and the second takes two parameters
    // and produces a result. 
    // On the flip side, Vavr extends the idea of functional interfaces in Java 
    // further by supporting up to a maximum of eight parameters and spicing up the API 
    // with methods for memoization, composition, and currying.

    /**
     * Just like tuples, these functional interfaces are named according to the number of parameters they take:
     * Function0, Function1, Function2 etc.
     * With Vavr, we would have written the above two functions like this:
     */
    @Test
    public void givenVavrFunction_whenWorks_thenCorrect() {
        Function<Integer, Integer> square = num -> num * num;
        int result = square.apply(2);

        assertEquals(4, result);
    }

    @Test
    public void givenVavrBiFunction_whenWorks_thenCorrect() {
        Function2<Integer, Integer, Integer> sum =
                (num1, num2) -> num1 + num2;
        int result = sum.apply(1, 2);

        assertEquals(3, result);
    }

    /**
     * When there is no parameter but we still need an output,
     * in Java 8 we would need to use a Consumer type, in Vavr Function0 is there to help:
     */
    @Test
    public void whenCreatesFunction_thenCorrect0() {
        Function0<String> getClazzName = () -> this.getClass().getName();
        String clazzName = getClazzName.apply();

        assertEquals("com.korges.vavr.VavrBaeldung", clazzName);
    }

    /**
     * We can also combine the static factory method FunctionN.of for any of the functions to create 
     * a Vavr function from a method reference. Like if we have the following sum method:
     */
    private int sum(int a, int b) {
        return a + b;
    }

    @Test
    public void whenCreatesFunctionFromMethodRef_thenCorrect() {
        Function2<Integer, Integer, Integer> sum = Function2.of(this::sum);
        int summed = sum.apply(3, 9);

        assertEquals(12, summed);
    }

    
    // Vavr - Collections
    // Collections API that meets the requirements of functional programming i.e. persistence, immutability.


    @Test(expected = UnsupportedOperationException.class)
    public void whenImmutableCollectionThrows_thenCorrect() {
        java.util.List<String> wordList = Arrays.asList("one", "two");
        java.util.List<String> list = Collections.unmodifiableList(wordList);
        list.add("boom");
    }

    @Test
    public void whenCreatesVavrList_thenCorrect() {
        List<Integer> intList = List.of(1, 2, 3, 4, 5);
        
        assertEquals(5, intList.size());
        assertEquals(Integer.valueOf(1), intList.get(0));
        assertEquals(Integer.valueOf(3), intList.get(2));
        assertEquals(Integer.valueOf(5), intList.get(4));

        intList.push(10);
        assertEquals(5, intList.size());
        assertEquals(Integer.valueOf(5), intList.last());

        List<Integer> updated = intList.append(10);
        assertEquals(6, updated.size());
        assertEquals(Integer.valueOf(10), updated.get(5));
    }

    @Test
    public void whenSumsVavrList_thenCorrect() {
        int sum = List.of(1, 2, 3)
                .sum()
                .intValue();

        assertEquals(6, sum);
    }


    // Vavr - Validation
    // Vavr brings the concept of Applicative Functor to Java from the functional programming world. 
    // In the simplest of terms, an Applicative Functor enables us to perform a sequence of actions while accumulating 
    // the results.

    /**
     * The rule for age is that it should be an integer greater than 0 and the rule for name is that it
     * should contain no special characters:
     */
    @Test
    public void whenValidationWorks_thenCorrect() {
        PersonValidator personValidator = new PersonValidator();

        Validation<Seq<String>, Person> valid =
                personValidator.validatePerson("John Doe", 30);

        Validation<Seq<String>, Person> invalid =
                personValidator.validatePerson("John? Doe!4", -1);

        assertEquals(
                "Valid(Person [name=John Doe, age=30])",
                valid.toString());

        assertEquals(
                "Invalid(List(Invalid characters in name: ?!4, Age must be at least 0))",
        invalid.toString());
    }


    // Vavr - Lazy
    // Lazy is a container which represents a value computed lazily i.e. computation is deferred until 
    // the result is required. Furthermore, the evaluated value is cached or memoized and returned 
    // again and again each time it is needed without repeating the computation


    @Test
    public void givenFunction_whenEvaluatesWithLazy_thenCorrect() {
        Lazy<Double> lazy = Lazy.of(Math::random);
        assertFalse(lazy.isEvaluated());

        double val1 = lazy.get();
        System.out.println(val1);
        assertTrue(lazy.isEvaluated());

        double val2 = lazy.get();
        System.out.println(val2);

        assertEquals(val1, val2, 0.1);
    }
}
